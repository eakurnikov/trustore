package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ControlCommand
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Transactions
import com.eakurnikov.trustore.api.Trustore
import com.eakurnikov.trustore.api.WriteCommand
import com.eakurnikov.trustore.impl.util.error
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class TrustoreImpl(
    private val store: Store,
    private val transactions: Transactions
) : Trustore {

    private val mutex = Mutex()

    override suspend fun command(command: ControlCommand): CommandResult {
        return mutex.withLock {
            safeExecute {
                command.execute(transactions)
            }
        }
    }

    override suspend fun command(command: ReadCommand): CommandResult {
        return mutex.withLock {
            safeExecute {
                command.execute(store.withReadAccess)
            }
        }
    }

    override suspend fun command(command: WriteCommand): CommandResult {
        return mutex.withLock {
            safeExecute {
                command.execute(store.withWriteAccess)
            }
        }
    }

    private inline fun safeExecute(block: () -> CommandResult): CommandResult {
        return try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }
}
