package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.Trustore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

interface BackupManager {
    val events: SharedFlow<BackupEvent>
    fun onInit()
    fun onLowMemory()
}

@Singleton
class BackupManagerImpl @Inject constructor(
    private val trustore: Trustore,
    private val backupStorage: BackupStorage
) : BackupManager {

    private val backupScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val mutableEvents: MutableSharedFlow<BackupEvent> = MutableSharedFlow(replay = 10)
    override val events: SharedFlow<BackupEvent> = mutableEvents

    override fun onInit() {
        backupScope.launch {
            val result: CommandResult = trustore.command(CustomCommands.Restore(backupStorage))
            when (result.status) {
                CommandResult.Status.SUCCESS -> mutableEvents.emit(BackupEvent.Restore.Success)
                CommandResult.Status.FAILURE -> mutableEvents.emit(BackupEvent.Restore.Failure)
                CommandResult.Status.ERROR -> mutableEvents.emit(BackupEvent.Restore.Error(result.error))
            }
        }
    }

    override fun onLowMemory() {
        backupScope.launch {
            val result: CommandResult = trustore.command(CustomCommands.Save(backupStorage))
            when (result.status) {
                CommandResult.Status.SUCCESS -> mutableEvents.emit(BackupEvent.Save.Success)
                CommandResult.Status.FAILURE -> mutableEvents.emit(BackupEvent.Save.Failure)
                CommandResult.Status.ERROR -> mutableEvents.emit(BackupEvent.Save.Error(result.error))
            }
        }
    }
}
