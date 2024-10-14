package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.Store
import kotlinx.serialization.Serializable

@Serializable
data class Backup(
    val map: Map<String, String>
)

class BackupSnapshot(
    override val content: Map<String, String>
) : Store.Snapshot {

    override fun set(key: String, value: String): Store.Snapshot {
        val newMap: Map<String, String> = HashMap(content).apply { put(key, value) }
        return BackupSnapshot(newMap)
    }

    override fun get(key: String): String? {
        return content[key]
    }

    override fun delete(key: String): Store.Snapshot {
        val newMap: Map<String, String> = HashMap(content).apply { remove(key) }
        return BackupSnapshot(newMap)
    }

    override fun count(value: String): Int {
        return content.values.count { it == value }
    }
}
