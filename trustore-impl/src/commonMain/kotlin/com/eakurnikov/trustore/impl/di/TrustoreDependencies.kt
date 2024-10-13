package com.eakurnikov.trustore.impl.di

import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Transactions
import com.eakurnikov.trustore.api.Trustore
import com.eakurnikov.trustore.impl.StoreImpl
import com.eakurnikov.trustore.impl.TransactionsImpl

class TrustoreDependencies : Trustore.Dependencies {
    private val store: Store = StoreImpl()
    private val transactions: Transactions = TransactionsImpl(store)

    override fun store(): Store = store
    override fun transactions(): Transactions = transactions
}
