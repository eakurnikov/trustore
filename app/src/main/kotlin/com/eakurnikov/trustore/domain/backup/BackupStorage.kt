package com.eakurnikov.trustore.domain.backup

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface BackupStorage {
    suspend fun saveBackup(snapshot: SnapshotImpl): Result<Unit>
    suspend fun retrieveBackup(): Result<SnapshotImpl?>
    suspend fun dropBackup(): Result<Boolean>
}

@Singleton
class BackupStorageImpl @Inject constructor(
    private val context: Context
) : BackupStorage {

    private val backupFile: File
        get() = File(context.filesDir, BACKUP_FILE_NAME)

    override suspend fun saveBackup(snapshot: SnapshotImpl): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val backup = Backup(snapshot.content)
                val jsonString: String = Json.encodeToString(backup)
                backupFile.writeText(jsonString)
            }
        }
    }

    override suspend fun retrieveBackup(): Result<SnapshotImpl?> {
        return withContext(Dispatchers.IO) {
            if (!backupFile.exists()) {
                return@withContext Result.success(null)
            }
            runCatching {
                val jsonString: String = backupFile.readText()
                val backup: Backup = Json.decodeFromString<Backup>(jsonString)
                SnapshotImpl(backup.content)
            }
        }
    }

    override suspend fun dropBackup(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            runCatching { backupFile.delete() }
        }
    }

    private companion object {
        const val BACKUP_FILE_NAME = "trustore-backup.json"
    }
}
