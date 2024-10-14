package com.eakurnikov.trustore.domain.backup

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

class BackupStorage @Inject constructor(
    private val context: Context
) {
    private val backupFile: File
        get() = File(context.filesDir, "backup.json")

    suspend fun saveBackup(snapshot: BackupSnapshot) {
        val backup = Backup(snapshot.content)
        val jsonString: String = Json.encodeToString(backup)

        withContext(Dispatchers.IO) {
            backupFile.writeText(jsonString)
        }
    }

    suspend fun restoreBackup(): BackupSnapshot? {
        return withContext(Dispatchers.IO) {
            if (!backupFile.exists()) {
                return@withContext null
            }

            val jsonString: String = backupFile.readText()
            val backup: Backup = Json.decodeFromString<Backup>(jsonString)
            BackupSnapshot(backup.map)
        }
    }
}
