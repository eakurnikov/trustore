package com.eakurnikov.trustore.domain.errors

import com.eakurnikov.trustore.domain.backup.BackupManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutOfMemoryErrorHandler @Inject constructor(
    private val backupManager: BackupManager
) : ThrowableHandler {

    override fun handle(throwable: Throwable) {
        if (throwable is OutOfMemoryError) {
            backupManager.onLowMemory()
        }
    }
}
