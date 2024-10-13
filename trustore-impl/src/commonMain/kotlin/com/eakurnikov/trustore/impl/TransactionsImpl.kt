package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Transactions

/**
 * Note: this particular implementation is not thread-safe itself.
 * It's crucial to provide thread-safe access to this store on the call site.
 */
internal class TransactionsImpl(
    private val store: Store
) : Transactions {

    private val stack: MutableList<Transaction> = arrayListOf()

    override suspend fun begin(): Boolean {
        return Transaction(store.snapshot()).let(stack::add)
    }

    override suspend fun commit(): Boolean {
        if (stack.isEmpty()) {
            return false
        }
        popCurrentTransaction()
        return true
    }

    override suspend fun rollback(): Boolean {
        if (stack.isEmpty()) {
            return false
        }
        store.withWriteAccess.applySnapshot(popCurrentTransaction().backup)
        return true
    }

    override suspend fun isInTransaction(): Boolean {
        return stack.isNotEmpty()
    }

    private fun popCurrentTransaction(): Transaction {
        return stack.removeAt(stack.lastIndex)
    }
}
