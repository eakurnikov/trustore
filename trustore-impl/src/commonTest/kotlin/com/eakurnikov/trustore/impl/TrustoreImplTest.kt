package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Trustore
import com.eakurnikov.trustore.impl.util.SpyCommands
import com.eakurnikov.trustore.impl.util.assertError
import com.eakurnikov.trustore.impl.util.assertSuccess
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class TrustoreImplTest {

    private lateinit var trustore: Trustore

    @BeforeTest
    fun setUp() {
        val store: Store = StoreImpl()
        trustore = TrustoreImpl(
            store = store,
            transactions = TransactionsImpl(store)
        )
    }

    @Test
    fun `when multiple concurrent reads then they succeed concurrently`() = runTest {
        // Given: Two concurrent read commands
        val readCompletable1 = CompletableDeferred<Unit>()
        val readCompletable2 = CompletableDeferred<Unit>()

        val readCommand1 = SpyCommands.Read(200) {
            readCompletable1.complete(Unit)
        }
        val readCommand2 = SpyCommands.Read(200) {
            readCompletable2.complete(Unit)
        }

        // When: Both reads are executed concurrently
        val readResult1: Deferred<CommandResult> = async {
            trustore.command(readCommand1)
        }
        val readResult2: Deferred<CommandResult> = async {
            trustore.command(readCommand2)
        }

        // Then: Both reads should complete successfully and concurrently
        assertSuccess(readResult1.await())
        assertSuccess(readResult2.await())
        assertTrue(readCompletable1.isCompleted)
        assertTrue(readCompletable2.isCompleted)
    }

    @Test
    fun `when write happens then reads are blocked until the write completes`() = runTest {
        // Given: A write command and a read command
        val writeCompletable = CompletableDeferred<Unit>()
        val readCompletable = CompletableDeferred<Unit>()

        val writeCommand = SpyCommands.Write(500) {
            writeCompletable.complete(Unit)
        }
        val readCommand = SpyCommands.Read(200) {
            assertTrue(writeCompletable.isCompleted)
            readCompletable.complete(Unit)
        }

        // When: Execute the write command and then the read command
        val writeResult: Deferred<CommandResult> = async {
            trustore.command(writeCommand)
        }
        val readResult: Deferred<CommandResult> = async {
            delay(100)
            trustore.command(readCommand)
        }

        // Then: Write completes first, followed by the read
        assertSuccess(writeResult.await())
        assertSuccess(readResult.await())
        assertTrue(readCompletable.isCompleted)
    }

    @Test
    fun `when concurrent reads and writes then writes are blocked until reads complete`() = runTest {
        // Given: A read command and a write command
        val readCompletable = CompletableDeferred<Unit>()
        val writeCompletable = CompletableDeferred<Unit>()

        val readCommand = SpyCommands.Read(200) {
            readCompletable.complete(Unit)
        }
        val writeCommand = SpyCommands.Write(500) {
            assertTrue(readCompletable.isCompleted)
            writeCompletable.complete(Unit)
        }

        // When: A read is started, followed by a write
        val readResult: Deferred<CommandResult> = async {
            trustore.command(readCommand)
        }
        val writeResult: Deferred<CommandResult> = async {
            delay(100)
            trustore.command(writeCommand)
        }

        // Then: The read should complete first, and then the write
        assertSuccess(readResult.await())
        assertSuccess(writeResult.await())
        assertTrue(writeCompletable.isCompleted)
    }

    @Test
    fun `when multiple writes happen then they execute one after another`() = runTest {
        // Given: Two write commands
        val writeCompletable1 = CompletableDeferred<Unit>()
        val writeCompletable2 = CompletableDeferred<Unit>()

        val writeCommand1 = SpyCommands.Write(500) {
            writeCompletable1.complete(Unit)
        }
        val writeCommand2 = SpyCommands.Write(500) {
            assertTrue(writeCompletable1.isCompleted)
            writeCompletable2.complete(Unit)
        }

        // When: Both writes are executed concurrently
        val writeResult1: Deferred<CommandResult> = async {
            trustore.command(writeCommand1)
        }
        val writeResult2: Deferred<CommandResult> = async {
            delay(100)
            trustore.command(writeCommand2)
        }

        // Then: Both writes should execute sequentially
        assertSuccess(writeResult1.await())
        assertSuccess(writeResult2.await())
        assertTrue(writeCompletable1.isCompleted)
        assertTrue(writeCompletable2.isCompleted)
    }

    @Test
    fun `when write occurs then subsequent reads wait until write is done`() = runTest {
        // Given: A write command and multiple read commands
        val writeCompletable = CompletableDeferred<Unit>()
        val readCompletable1 = CompletableDeferred<Unit>()
        val readCompletable2 = CompletableDeferred<Unit>()

        val writeCommand = SpyCommands.Write(500) {
            writeCompletable.complete(Unit)
        }
        val readCommand1 = SpyCommands.Read(200) {
            assertTrue(writeCompletable.isCompleted)
            readCompletable1.complete(Unit)
        }
        val readCommand2 = SpyCommands.Read(200) {
            assertTrue(writeCompletable.isCompleted)
            readCompletable2.complete(Unit)
        }

        // When: Write command is started, followed by multiple reads
        val writeResult: Deferred<CommandResult> = async {
            trustore.command(writeCommand)
        }
        val readResult1: Deferred<CommandResult> = async {
            delay(100)
            trustore.command(readCommand1)
        }
        val readResult2: Deferred<CommandResult> = async {
            delay(200)
            trustore.command(readCommand2)
        }

        // Then: Write completes first, followed by the reads
        assertSuccess(writeResult.await())
        assertSuccess(readResult1.await())
        assertSuccess(readResult2.await())
        assertTrue(readCompletable1.isCompleted)
        assertTrue(readCompletable2.isCompleted)
    }

    @Test
    fun `when mixed read and write commands then no deadlock occurs`() = runTest {
        // Given: A long-running read, a write, and another read
        val readCompletable1 = CompletableDeferred<Unit>()
        val writeCompletable = CompletableDeferred<Unit>()
        val readCompletable2 = CompletableDeferred<Unit>()

        val readCommand1 = SpyCommands.Read(200) {
            readCompletable1.complete(Unit)
        }
        val writeCommand = SpyCommands.Write(500) {
            assertTrue(readCompletable1.isCompleted)
            writeCompletable.complete(Unit)
        }
        val readCommand2 = SpyCommands.Read(200) {
            assertTrue(writeCompletable.isCompleted)
            readCompletable2.complete(Unit)
        }

        // When: A read is started, followed by a write and another read
        val readResult1: Deferred<CommandResult> = async {
            trustore.command(readCommand1)
        }
        val writeResult: Deferred<CommandResult> = async {
            delay(100)
            trustore.command(writeCommand)
        }
        val readResult2: Deferred<CommandResult> = async {
            delay(200)
            trustore.command(readCommand2)
        }

        // Then: No deadlock occurs, and all jobs complete successfully
        assertSuccess(readResult1.await())
        assertSuccess(writeResult.await())
        assertSuccess(readResult2.await())
        assertTrue(readCompletable1.isCompleted)
        assertTrue(writeCompletable.isCompleted)
        assertTrue(readCompletable2.isCompleted)
    }

    @Test
    fun `when a write fails then subsequent operations continue normally`() = runTest {
        // Given: A failing write command and a read command
        val readCompletable = CompletableDeferred<Unit>()

        val failingWriteCommand = SpyCommands.Write(500) {
            throw RuntimeException()
        }
        val readCommand = SpyCommands.Read(200) {
            readCompletable.complete(Unit)
        }

        // When: The write fails, and a read is executed afterward
        val writeResult: Deferred<CommandResult> = async {
            trustore.command(failingWriteCommand)
        }
        val readResult: Deferred<CommandResult> = async {
            delay(100)
            trustore.command(readCommand)
        }

        // Then: Write should fail, and read should succeed afterward
        assertError(writeResult.await())
        assertSuccess(readResult.await())
        assertTrue(readCompletable.isCompleted)
    }
}
