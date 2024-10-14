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
        get() = File(context.filesDir, BACKUP_FILE_NAME)

    suspend fun saveBackup(snapshot: BackupSnapshot): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val backup = Backup(snapshot.content)
                val jsonString: String = Json.encodeToString(backup)
                backupFile.writeText(jsonString)
            }
        }
    }

    suspend fun restoreBackup(): Result<BackupSnapshot?> {
        return withContext(Dispatchers.IO) {
            if (!backupFile.exists()) {
                return@withContext Result.success(null)
            }
            runCatching {
                val jsonString: String = backupFile.readText()
                val backup: Backup = Json.decodeFromString<Backup>(jsonString)
                BackupSnapshot(backup.map)
            }
        }
    }

    private companion object {
        const val BACKUP_FILE_NAME = "trustore-backup.json"
    }
}
