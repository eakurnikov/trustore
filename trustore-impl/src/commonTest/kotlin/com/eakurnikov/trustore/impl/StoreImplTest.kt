package com.eakurnikov.trustore.impl

import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StoreImplTest {
    private lateinit var store: StoreImpl

    @BeforeTest
    fun setup() {
        store = StoreImpl()
    }

    @Test
    fun `when setting a value - then it can be retrieved`() = runTest {
        // When
        store.withWriteAccess.set("foo", "bar")

        // Then
        val value = store.withReadAccess.get("foo")
        assertEquals("bar", value)
    }

    @Test
    fun `when setting multiple values - then they can all be retrieved`() = runTest {
        // When
        store.withWriteAccess.set("key1", "value1")
        store.withWriteAccess.set("key2", "value2")

        // Then
        assertEquals("value1", store.withReadAccess.get("key1"))
        assertEquals("value2", store.withReadAccess.get("key2"))
    }

    @Test
    fun `when setting a value twice - then it overwrites the previous value`() = runTest {
        // When
        store.withWriteAccess.set("foo", "bar")
        store.withWriteAccess.set("foo", "new_bar")

        // Then
        val value = store.withReadAccess.get("foo")
        assertEquals("new_bar", value)
    }

    @Test
    fun `when retrieving a non-existent key - then it returns null`() = runTest {
        // When
        val value = store.withReadAccess.get("nonexistent")

        // Then
        assertNull(value)
    }

    @Test
    fun `when deleting a key - then it can no longer be retrieved`() = runTest {
        // When
        store.withWriteAccess.set("foo", "bar")
        store.withWriteAccess.delete("foo")

        // Then
        val value = store.withReadAccess.get("foo")
        assertNull(value)
    }

    @Test
    fun `when deleting a non-existent key - then it does not affect the store`() = runTest {
        // When
        store.withWriteAccess.delete("nonexistent")

        // Then
        assertNull(store.withReadAccess.get("nonexistent"))
    }

    @Test
    fun `when counting occurrences of a value - then the correct count is returned`() = runTest {
        // When
        store.withWriteAccess.set("foo", "123")
        store.withWriteAccess.set("bar", "456")
        store.withWriteAccess.set("baz", "123")

        // Then
        val count123 = store.withReadAccess.count("123")
        val count456 = store.withReadAccess.count("456")
        val count789 = store.withReadAccess.count("789")

        assertEquals(2, count123)
        assertEquals(1, count456)
        assertEquals(0, count789)
    }

    @Test
    fun `when creating a snapshot - then the store state is preserved`() = runTest {
        // When
        store.withWriteAccess.set("foo", "123")
        store.withWriteAccess.set("bar", "456")
        val snapshot = store.snapshot()

        // Then
        assertEquals("123", snapshot.get("foo"))
        assertEquals("456", snapshot.get("bar"))
    }

    @Test
    fun `when applying a snapshot - then the store is restored to that state`() = runTest {
        // Given
        store.withWriteAccess.set("foo", "123")
        val snapshot = store.snapshot()

        // When
        store.withWriteAccess.set("foo", "789")
        store.applySnapshot(snapshot)

        // Then
        val value = store.withReadAccess.get("foo")
        assertEquals("123", value)
    }

    @Test
    fun `when applying an empty snapshot - then the store becomes empty`() = runTest {
        // Given
        store.withWriteAccess.set("foo", "123")
        store.withWriteAccess.set("bar", "456")
        val emptySnapshot = store.snapshot().delete("foo").delete("bar")

        // When
        store.applySnapshot(emptySnapshot)

        // Then
        assertNull(store.withReadAccess.get("foo"))
        assertNull(store.withReadAccess.get("bar"))
    }

    @Test
    fun `when deleting all keys - then the store becomes empty`() = runTest {
        // Given
        store.withWriteAccess.set("foo", "123")
        store.withWriteAccess.set("bar", "456")

        // When
        store.withWriteAccess.delete("foo")
        store.withWriteAccess.delete("bar")

        // Then
        assertNull(store.withReadAccess.get("foo"))
        assertNull(store.withReadAccess.get("bar"))
    }

    @Test
    fun `when counting values in an empty store - then the count is zero`() = runTest {
        // When
        val count = store.withReadAccess.count("any_value")

        // Then
        assertEquals(0, count)
    }

    @Test
    fun `when setting a key to the same value multiple times - then the count reflects all occurrences`() =
        runTest {
            // When
            store.withWriteAccess.set("foo", "value")
            store.withWriteAccess.set("bar", "value")
            store.withWriteAccess.set("baz", "value")

            // Then
            val count = store.withReadAccess.count("value")
            assertEquals(3, count)
        }

    @Test
    fun `when a snapshot is applied multiple times - then the store remains consistent`() =
        runTest {
            // Given
            store.withWriteAccess.set("foo", "123")
            val snapshot = store.snapshot()

            // When
            store.applySnapshot(snapshot)
            store.applySnapshot(snapshot)

            // Then
            assertEquals("123", store.withReadAccess.get("foo"))
        }
}
