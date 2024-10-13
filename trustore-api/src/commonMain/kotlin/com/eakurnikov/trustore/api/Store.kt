package com.eakurnikov.trustore.api

interface Store {
    val withReadAccess: Read
    val withWriteAccess: Write

    suspend fun snapshot(): Snapshot

    interface Read {
        suspend fun get(key: String): String?
        suspend fun count(value: String): Int
    }

    interface Write : Read {
        suspend fun set(key: String, value: String)
        suspend fun delete(key: String)
        suspend fun applySnapshot(snapshot: Snapshot)
    }

    interface Snapshot {
        fun set(key: String, value: String): Snapshot
        fun get(key: String): String?
        fun delete(key: String): Snapshot
        fun count(value: String): Int
    }
}
