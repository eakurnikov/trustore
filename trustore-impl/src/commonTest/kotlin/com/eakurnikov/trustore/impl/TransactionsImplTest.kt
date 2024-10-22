package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.WriteCommand
import com.eakurnikov.trustore.impl.util.success
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TransactionsImplTest {
    private lateinit var store: StoreImpl
    private lateinit var transactions: TransactionsImpl

    @BeforeTest
    fun setup() {
        store = StoreImpl()
        transactions = TransactionsImpl(store)
    }

    @Test
    fun `when begin is called - then transaction starts successfully`() = runTest {
        // When
        val result = transactions.begin()

        // Then
        assertTrue(result)
        assertTrue(transactions.isInTransaction())
    }

    @Test
    fun `when command is executed with read access - then correct value is returned`() = runTest {
        // Given
        store.withWriteAccess.set("foo", "bar")
        val command = object : ReadCommand {
            override suspend fun execute(store: Store.Read): CommandResult = success(store.get("foo"))
        }

        // When
        val result: CommandResult = command.execute(store.withReadAccess)

        // Then
        assertEquals("bar", result.value)
    }

    @Test
    fun `when command is executed with write access - then store state is updated`() = runTest {
        // Given
        val command = object : WriteCommand {
            override suspend fun execute(store: Store.Write): CommandResult {
                return success(store.set("foo", "bar"))
            }
        }

        // When
        command.execute(store.withWriteAccess)

        // Then
        assertEquals("bar", store.withReadAccess.get("foo"))
    }

    @Test
    fun `when commit is called with no transactions - then returns false`() = runTest {
        // When
        val result = transactions.commit()

        // Then
        assertFalse(result)
    }

    @Test
    fun `when commit is called with active transaction - then it commits successfully`() = runTest {
        // Given
        transactions.begin()
        store.withWriteAccess.set("foo", "bar")

        // When
        val result = transactions.commit()

        // Then
        assertTrue(result)
        assertFalse(transactions.isInTransaction())
        assertEquals("bar", store.withReadAccess.get("foo"))
    }

    @Test
    fun `when rollback is called with no transactions - then returns false`() = runTest {
        // When
        val result = transactions.rollback()

        // Then
        assertFalse(result)
    }

    @Test
    fun `when rollback is called with active transaction - then state is restored to previous snapshot`() = runTest {
        // Given
        store.withWriteAccess.set("foo", "123")
        store.withWriteAccess.set("bar", "abc")

        transactions.begin()

        store.withWriteAccess.set("foo", "456")
        store.withWriteAccess.set("bar", "def")

        // Ensure the state has been changed inside the transaction
        assertEquals("456", store.withReadAccess.get("foo"))
        assertEquals("def", store.withReadAccess.get("bar"))

        // When
        val rollbackResult = transactions.rollback()

        // Then
        assertTrue(rollbackResult) // Rollback should succeed
        assertEquals("123", store.withReadAccess.get("foo"))
        assertEquals("abc", store.withReadAccess.get("bar"))
        assertFalse(transactions.isInTransaction()) // Ensure no active transactions remain
    }

    @Test
    fun `when multiple transactions are active - then commit affects only the current one`() = runTest {
        // Given
        transactions.begin() // First transaction
        store.withWriteAccess.set("foo", "bar")

        transactions.begin() // Nested transaction
        store.withWriteAccess.set("foo", "baz")

        // When
        transactions.commit() // Commit only the nested one

        // Then
        assertEquals("baz", store.withReadAccess.get("foo")) // Nested change is preserved
        assertTrue(transactions.isInTransaction()) // Still in the outer transaction
    }

    @Test
    fun `when multiple transactions are active - then rollback affects only the current one`() = runTest {
        // Given
        transactions.begin() // First transaction
        store.withWriteAccess.set("foo", "bar")

        transactions.begin() // Nested transaction
        store.withWriteAccess.set("foo", "baz")

        // When
        transactions.rollback() // Rollback the nested one

        // Then
        assertEquals("bar", store.withReadAccess.get("foo")) // Outer state is preserved
        assertTrue(transactions.isInTransaction()) // Still in the outer transaction
    }

    @Test
    fun `when all transactions are committed - then no transactions remain`() = runTest {
        // Given
        transactions.begin()
        transactions.begin() // Nested transaction

        // When
        transactions.commit()
        transactions.commit()

        // Then
        assertFalse(transactions.isInTransaction())
    }

    @Test
    fun `when all transactions are rolled back - then store returns to original state`() = runTest {
        // Given
        store.withWriteAccess.set("key", "initial")

        transactions.begin()
        store.withWriteAccess.set("key", "first")

        transactions.begin()
        store.withWriteAccess.set("key", "second")

        // When
        transactions.rollback() // Rollback to first transaction
        transactions.rollback() // Rollback to original state

        // Then
        assertEquals("initial", store.withReadAccess.get("key"))
        assertFalse(transactions.isInTransaction())
    }

    @Test
    fun `when checking isInTransaction - then returns true if in transaction`() = runTest {
        // Given
        transactions.begin()

        // When
        val result = transactions.isInTransaction()

        // Then
        assertTrue(result)
    }

    @Test
    fun `when checking isInTransaction - then returns false if not in transaction`() = runTest {
        // When
        val result = transactions.isInTransaction()

        // Then
        assertFalse(result)
    }
}
