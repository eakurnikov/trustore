package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.Store

class GetSnapshotCommand : ReadCommand {
    override suspend fun execute(store: Store.Read): CommandResult<Any?> {
        return try {
            CommandResult(
                status = CommandResult.Status.SUCCESS,
                value = BackupSnapshot(store.snapshot().content),
                error = null
            )
        } catch (e: Throwable) {
            CommandResult(
                status = CommandResult.Status.ERROR,
                value = null,
                error = e
            )
        }
    }
}
