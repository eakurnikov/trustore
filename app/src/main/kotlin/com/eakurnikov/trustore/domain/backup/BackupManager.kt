package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.Trustore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupManager @Inject constructor(
    private val trustore: Trustore,
    private val storage: BackupStorage
) {
    private val scope: CoroutineScope = GlobalScope

    fun onInit() {
        scope.launch {
            android.util.Log.i("TRATATA", "BackupManager onInit")
            val restoredBackup: BackupSnapshot? = storage.restoreBackup()
            restoredBackup?.let {
                trustore.command(SetSnapshotCommand(it))
                android.util.Log.i("TRATATA", "BackupManager onInit restored")
            }
        }
    }

    fun onLowMemory() {
        scope.launch {
            android.util.Log.i("TRATATA", "BackupManager onLowMemory")
            val result: CommandResult<Any?> = trustore.command(GetSnapshotCommand())
            val content: Any? = result.value

            if (result.status == CommandResult.Status.SUCCESS &&
                content is BackupSnapshot
            ) {
                storage.saveBackup(content)
                android.util.Log.i("TRATATA", "BackupManager onLowMemory saved")
            }
        }
    }
}
