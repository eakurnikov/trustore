package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ControlCommand
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Transactions
import com.eakurnikov.trustore.api.WriteCommand
import com.eakurnikov.trustore.impl.util.failure
import com.eakurnikov.trustore.impl.util.success

object Commands {

    object Begin : ControlCommand {
        override suspend fun execute(transactions: Transactions): CommandResult {
            return when (transactions.begin()) {
                true -> success()
                else -> failure()
            }
        }
    }

    object Commit : ControlCommand {
        override suspend fun execute(transactions: Transactions): CommandResult {
            return when (transactions.commit()) {
                true -> success()
                else -> failure()
            }
        }
    }

    object Rollback : ControlCommand {
        override suspend fun execute(transactions: Transactions): CommandResult {
            return when (transactions.rollback()) {
                true -> success()
                else -> failure()
            }
        }
    }

    class Get(
        private val key: String
    ) : ReadCommand {

        override suspend fun execute(store: Store.Read): CommandResult {
            return when (val result: String? = store.get(key)) {
                null -> failure()
                else -> success(result)
            }
        }
    }

    class Count(
        private val value: String
    ) : ReadCommand {

        override suspend fun execute(store: Store.Read): CommandResult {
            return success(store.count(value).toString())
        }
    }

    class Delete(
        private val key: String
    ) : WriteCommand {

        override suspend fun execute(store: Store.Write): CommandResult {
            return success(store.delete(key))
        }
    }

    class Set(
        private val key: String,
        private val value: String
    ) : WriteCommand {

        override suspend fun execute(store: Store.Write): CommandResult {
            return success(store.set(key, value))
        }
    }
}
