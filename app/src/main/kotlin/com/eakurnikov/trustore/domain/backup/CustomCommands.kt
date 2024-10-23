package com.eakurnikov.trustore.domain.backup

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ControlCommand
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.Store
import com.eakurnikov.trustore.api.Transactions

object CustomCommands {

    class GetSnapshot : ReadCommand {
        override suspend fun execute(store: Store.Read): CommandResult {
            return try {
                CommandResult(
                    status = CommandResult.Status.SUCCESS,
                    value = SnapshotImpl(store.snapshot().content),
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

    class SetSnapshot(
        private val snapshot: SnapshotImpl
    ) : ControlCommand {

        override suspend fun execute(transactions: Transactions): CommandResult {
            return try {
                val result: Boolean = transactions.applySnapshot(SnapshotImpl(snapshot.content))
                if (result) {
                    CommandResult(
                        status = CommandResult.Status.SUCCESS,
                        value = Unit,
                        error = null
                    )
                } else {
                    CommandResult(
                        status = CommandResult.Status.FAILURE,
                        value = null,
                        error = null
                    )
                }
            } catch (e: Throwable) {
                CommandResult(
                    status = CommandResult.Status.ERROR,
                    value = null,
                    error = e
                )
            }
        }
    }

    object Clear : ControlCommand by SetSnapshot(SnapshotImpl(emptyMap()))

    class Save(
        private val backupStorage: BackupStorage
    ) : ReadCommand {

        override suspend fun execute(store: Store.Read): CommandResult {
            val getResult: CommandResult = GetSnapshot().execute(store)

            if (getResult.status != CommandResult.Status.SUCCESS) {
                return getResult
            }

            val snapshot: SnapshotImpl = try {
                getResult.value as SnapshotImpl
            } catch (e: Throwable) {
                return CommandResult(
                    status = CommandResult.Status.ERROR,
                    value = null,
                    error = e
                )
            }

            val saveResult: Result<Unit> = backupStorage.saveBackup(snapshot)

            return when {
                saveResult.isSuccess -> CommandResult(
                    status = CommandResult.Status.SUCCESS,
                    value = Unit,
                    error = null
                )

                else -> CommandResult(
                    status = CommandResult.Status.ERROR,
                    value = null,
                    error = saveResult.exceptionOrNull()
                )
            }
        }
    }

    class Restore(
        private val backupStorage: BackupStorage
    ) : ControlCommand {

        override suspend fun execute(transactions: Transactions): CommandResult {
            val retrieveResult: Result<SnapshotImpl?> = backupStorage.retrieveBackup()

            if (retrieveResult.isFailure) {
                return CommandResult(
                    status = CommandResult.Status.ERROR,
                    value = null,
                    error = retrieveResult.exceptionOrNull()
                )
            }

            val restoredBackup: SnapshotImpl = retrieveResult.getOrNull()
                ?: return CommandResult(
                    status = CommandResult.Status.FAILURE,
                    value = BackupEvent.Restore.Failure,
                    error = null
                )

            val setResult: CommandResult = SetSnapshot(restoredBackup).execute(transactions)

            return when (setResult.status) {
                CommandResult.Status.SUCCESS -> {
                    CommandResult(
                        status = CommandResult.Status.SUCCESS,
                        value = Unit,
                        error = null
                    )
                }

                CommandResult.Status.FAILURE -> {
                    CommandResult(
                        status = CommandResult.Status.FAILURE,
                        value = BackupEvent.Restore.Restricted,
                        error = null
                    )
                }

                else -> {
                    CommandResult(
                        status = CommandResult.Status.ERROR,
                        value = null,
                        error = setResult.error
                    )
                }
            }
        }
    }

    object Drop {

        suspend fun execute(backupStorage: BackupStorage): CommandResult {
            val result: Result<Boolean> = backupStorage.dropBackup()
            return when {
                result.isSuccess -> {
                    if (result.getOrNull() == true) {
                        CommandResult(
                            status = CommandResult.Status.SUCCESS,
                            value = Unit,
                            error = null
                        )
                    } else {
                        CommandResult(
                            status = CommandResult.Status.FAILURE,
                            value = null,
                            error = null
                        )
                    }
                }

                else -> {
                    CommandResult(
                        status = CommandResult.Status.ERROR,
                        value = null,
                        error = result.exceptionOrNull()
                    )
                }
            }
        }
    }
}
