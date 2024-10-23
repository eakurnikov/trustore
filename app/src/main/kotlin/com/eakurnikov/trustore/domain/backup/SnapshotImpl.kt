package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.Store

class SnapshotImpl(
    override val content: Map<String, String>
) : Store.Snapshot {

    override fun set(key: String, value: String): Store.Snapshot {
        val newMap: Map<String, String> = HashMap(content).apply { put(key, value) }
        return SnapshotImpl(newMap)
    }

    override fun get(key: String): String? {
        return content[key]
    }

    override fun delete(key: String): Store.Snapshot {
        val newMap: Map<String, String> = HashMap(content).apply { remove(key) }
        return SnapshotImpl(newMap)
    }

    override fun count(value: String): Int {
        return content.values.count { it == value }
    }
}
