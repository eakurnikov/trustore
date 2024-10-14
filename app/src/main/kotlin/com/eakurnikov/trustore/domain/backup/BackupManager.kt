package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.Trustore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupManager @Inject constructor(
    private val trustore: Trustore,
    private val storage: BackupStorage
) {
    private val backupScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun onInit() {
        backupScope.launch {
            storage.restoreBackup().onSuccess { restoredBackup: BackupSnapshot? ->
                restoredBackup?.let { trustore.command(SetSnapshotCommand(it)) }
            }
        }
    }

    fun onLowMemory() {
        backupScope.launch {
            val result: CommandResult<Any?> = trustore.command(GetSnapshotCommand())
            val content: Any? = result.value

            if (result.status == CommandResult.Status.SUCCESS &&
                content is BackupSnapshot
            ) {
                storage.saveBackup(content)
            }
        }
    }

    fun onTerminate() {
        backupScope.cancel()
    }
}
