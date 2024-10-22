package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ControlCommand
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.WriteCommand
import com.eakurnikov.trustore.impl.util.assertFailure
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommandsTest {
    private lateinit var store: StoreImpl
    private lateinit var transactions: TransactionsImpl

    @BeforeTest
    fun setup() {
        store = StoreImpl()
        transactions = TransactionsImpl(store)
    }

    @Test
    fun `when setting and getting a value - then the correct value is returned`() = runTest {
        Commands.Set("foo", "123")()
        assertEquals("123", Commands.Get("foo")().value)
    }

    @Test
    fun `when deleting a value - then the key is no longer set`() = runTest {
        Commands.Set("foo", "123")()
        Commands.Delete("foo")()
        assertNull(Commands.Get("foo")().value) // "key not set"
    }

    @Test
    fun `when counting occurrences of a value - then the correct count is returned`() = runTest {
        Commands.Set("foo", "123")()
        Commands.Set("bar", "456")()
        Commands.Set("baz", "123")()

        val count123 = Commands.Count("123")().value
        val count456 = Commands.Count("456")().value

        assertEquals("2", count123)
        assertEquals("1", count456)
    }

    @Test
    fun `when committing a transaction - then changes are persisted`() = runTest {
        Commands.Set("bar", "123")()
        assertEquals("123", Commands.Get("bar")().value)

        Commands.Begin()
        Commands.Set("foo", "456")()
        Commands.Delete("bar")()
        Commands.Commit()

        assertNull(Commands.Get("bar")().value)
        assertEquals("456", Commands.Get("foo")().value)

        assertFailure(Commands.Rollback()) // No active transaction to rollback
    }

    @Test
    fun `when rolling back a transaction - then state is restored to previous snapshot`() = runTest {
        Commands.Set("foo", "123")()
        Commands.Set("bar", "abc")()

        Commands.Begin()
        Commands.Set("foo", "456")()
        Commands.Set("bar", "def")()

        assertEquals("456", Commands.Get("foo")().value)
        assertEquals("def", Commands.Get("bar")().value)

        Commands.Rollback()

        assertEquals("123", Commands.Get("foo")().value)
        assertEquals("abc", Commands.Get("bar")().value)

        assertFailure(Commands.Commit()) // No active transaction to commit
    }

    @Test
    fun `when using nested transactions - then only inner transactions are rolled back or committed`() = runTest {
        Commands.Set("foo", "123")()
        Commands.Set("bar", "456")()

        Commands.Begin()
        Commands.Set("foo", "456")()

        Commands.Begin()

        assertEquals("2", Commands.Count("456")().value)
        assertEquals("456", Commands.Get("foo")().value)

        Commands.Set("foo", "789")()

        assertEquals("789", Commands.Get("foo")().value)

        Commands.Rollback()

        assertEquals("456", Commands.Get("foo")().value)

        Commands.Delete("foo")()

        assertNull(Commands.Get("foo")().value)

        Commands.Rollback()

        assertEquals("123", Commands.Get("foo")().value)
    }

    private suspend operator fun ControlCommand.invoke(): CommandResult<Unit> = execute(transactions)
    private suspend operator fun ReadCommand.invoke(): CommandResult<String?> = execute(store.withReadAccess)
    private suspend operator fun WriteCommand.invoke(): CommandResult<Unit> = execute(store.withWriteAccess)
}
