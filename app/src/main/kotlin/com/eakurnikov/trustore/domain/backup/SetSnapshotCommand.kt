package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.WriteCommand

class SetSnapshotCommand(
    private val snapshot: BackupSnapshot
) : WriteCommand {

    override suspend fun execute(store: Store.Write): CommandResult<Unit> {
        store.applySnapshot(BackupSnapshot(snapshot.content))
        return CommandResult(
            status = CommandResult.Status.SUCCESS,
            value = Unit,
            error = null
        )
    }
}
