package com.eakurnikov.trustore.impl.util

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ControlCommand
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Transactions
import com.eakurnikov.trustore.api.WriteCommand
import kotlinx.coroutines.delay

object SpyCommands {

    class Read(
        private val delayMs: Long,
        private val onExecute: (() -> Unit)? = null
    ) : ReadCommand {

        override suspend fun execute(store: Store.Read): CommandResult {
            delay(delayMs)
            onExecute?.invoke()
            return success("")
        }
    }

    class Write(
        private val delayMs: Long,
        private val onExecute: (() -> Unit)? = null
    ) : WriteCommand {

        override suspend fun execute(store: Store.Write): CommandResult {
            delay(delayMs)
            onExecute?.invoke()
            return success()
        }
    }

    class Control(
        private val delayMs: Long
    ) : ControlCommand {

        override suspend fun execute(transactions: Transactions): CommandResult {
            delay(delayMs)
            return success()
        }
    }
}
