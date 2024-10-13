package com.eakurnikov.trustore.api

interface Transactions {
    suspend fun begin(): Boolean
    suspend fun commit(): Boolean
    suspend fun rollback(): Boolean
    suspend fun isInTransaction(): Boolean
}
