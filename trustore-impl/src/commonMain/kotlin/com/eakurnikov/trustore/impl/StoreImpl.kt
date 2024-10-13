package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.Store
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf

/**
 * Note: this particular implementation is not thread-safe itself.
 * Even though it uses immutable PersistentMap, different threads can get
 * different values while reading this store. So it's crucial to provide
 * thread-safe access to this store on the call site.
 */
internal class StoreImpl : Store, Store.Read, Store.Write {
    private var current: Store.Snapshot = SnapshotImpl(persistentHashMapOf())

    override val withReadAccess: Store.Read = this
    override val withWriteAccess: Store.Write = this

    override suspend fun get(key: String): String? {
        return current.get(key)
    }

    override suspend fun count(value: String): Int {
        return current.count(value)
    }

    override suspend fun set(key: String, value: String) {
        current = current.set(key, value)
    }

    override suspend fun delete(key: String) {
        current = current.delete(key)
    }

    override suspend fun snapshot(): Store.Snapshot {
        return current
    }

    override suspend fun applySnapshot(snapshot: Store.Snapshot) {
        current = snapshot
    }

    private class SnapshotImpl(
        private val map: PersistentMap<String, String>
    ) : Store.Snapshot {

        override fun set(key: String, value: String): Store.Snapshot {
            return SnapshotImpl(map.put(key, value))
        }

        override fun get(key: String): String? {
            return map[key]
        }

        override fun delete(key: String): Store.Snapshot {
            return SnapshotImpl(map.remove(key))
        }

        override fun count(value: String): Int {
            return map.values.count { it == value }
        }
    }
}
