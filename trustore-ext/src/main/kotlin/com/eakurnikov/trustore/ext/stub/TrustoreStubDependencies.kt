package com.eakurnikov.trustore.ext.stub

import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Transactions
import com.eakurnikov.trustore.api.Trustore

class TrustoreStubDependencies : Trustore.Dependencies {

    private val transactionsStub: Transactions = object : Transactions {
        override suspend fun begin(): Boolean = false
        override suspend fun commit(): Boolean = false
        override suspend fun rollback(): Boolean = false
        override suspend fun isInTransaction(): Boolean = false
    }

    private val storeStub: Store = object : Store {

        private val storeReadStub: Store.Read = object : Store.Read {
            override suspend fun get(key: String): String? = null
            override suspend fun count(value: String): Int = 0
        }

        private val storeWriteStub: Store.Write = object : Store.Write, Store.Read by storeReadStub {
            override suspend fun set(key: String, value: String) = Unit
            override suspend fun delete(key: String) = Unit
            override suspend fun applySnapshot(snapshot: Store.Snapshot) = Unit
        }

        private val storeSnapshotStub = object : Store.Snapshot {
            override fun set(key: String, value: String): Store.Snapshot = this
            override fun get(key: String): String? = null
            override fun delete(key: String): Store.Snapshot = this
            override fun count(value: String): Int = 0
        }

        override val withReadAccess: Store.Read = storeReadStub
        override val withWriteAccess: Store.Write = storeWriteStub

        override suspend fun snapshot(): Store.Snapshot = storeSnapshotStub
    }

    override fun store(): Store = storeStub
    override fun transactions(): Transactions = transactionsStub
}
