package com.eakurnikov.trustore.ui.texts

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.domain.InputEvent

object CommandResponseUiModel {

    fun mapToText(event: InputEvent.CommandExecuted): String? = when (event.command) {
        Texts.CommandNames.SET -> event.result.mapSetToText()
        Texts.CommandNames.GET -> event.result.mapGetToText()
        Texts.CommandNames.DELETE -> event.result.mapDeleteToText()
        Texts.CommandNames.COUNT -> event.result.mapCountToText()
        Texts.CommandNames.BEGIN -> event.result.mapBeginToText()
        Texts.CommandNames.COMMIT -> event.result.mapCommitToText()
        Texts.CommandNames.ROLLBACK -> event.result.mapRollbackToText()
        Texts.CommandNames.CLEAR -> event.result.mapClearToText()
        Texts.CommandNames.SAVE -> event.result.mapSaveToText()
        Texts.CommandNames.RESTORE -> event.result.mapRestoreToText()
        Texts.CommandNames.DROP -> event.result.mapDropToText()
        else -> null
    }

    private fun CommandResult.mapSetToText(): String? = when (status) {
        CommandResult.Status.SUCCESS -> null
        else -> Texts.Responses.operationError(error)
    }

    private fun CommandResult.mapGetToText(): String = when (status) {
        CommandResult.Status.SUCCESS -> value.toString()
        CommandResult.Status.FAILURE -> Texts.Responses.KEY_NOT_SET_FAILURE
        CommandResult.Status.ERROR -> Texts.Responses.operationError(error)
    }

    private fun CommandResult.mapDeleteToText(): String? = when (status) {
        CommandResult.Status.SUCCESS -> null
        else -> Texts.Responses.operationError(error)
    }

    private fun CommandResult.mapCountToText(): String = when (status) {
        CommandResult.Status.SUCCESS -> value.toString()
        else -> Texts.Responses.operationError(error)
    }

    private fun CommandResult.mapBeginToText(): String? = when (status) {
        CommandResult.Status.SUCCESS -> null
        else -> Texts.Responses.operationError(error)
    }

    private fun CommandResult.mapCommitToText(): String? = when (status) {
        CommandResult.Status.SUCCESS -> null
        CommandResult.Status.FAILURE -> Texts.Responses.NO_TRANSACTION_FAILURE
        CommandResult.Status.ERROR -> Texts.Responses.operationError(error)
    }

    private fun CommandResult.mapRollbackToText(): String? = when (status) {
        CommandResult.Status.SUCCESS -> null
        CommandResult.Status.FAILURE -> Texts.Responses.NO_TRANSACTION_FAILURE
        CommandResult.Status.ERROR -> Texts.Responses.operationError(error)
    }

    private fun CommandResult.mapClearToText(): String = when (status) {
        CommandResult.Status.SUCCESS -> Texts.BackupEvents.CLEAR_SUCCESS
        else -> Texts.Responses.operationError(error, Texts.BackupEvents.CLEAR_ERROR)
    }

    private fun CommandResult.mapSaveToText(): String = when (status) {
        CommandResult.Status.SUCCESS -> Texts.BackupEvents.SAVE_SUCCESS
        else -> Texts.Responses.operationError(error, Texts.BackupEvents.SAVE_ERROR)
    }

    private fun CommandResult.mapRestoreToText(): String = when (status) {
        CommandResult.Status.SUCCESS -> Texts.BackupEvents.RESTORE_SUCCESS
        CommandResult.Status.FAILURE -> Texts.BackupEvents.RESTORE_FAILURE
        CommandResult.Status.ERROR -> Texts.Responses.operationError(error, Texts.BackupEvents.RESTORE_ERROR)
    }

    private fun CommandResult.mapDropToText(): String = when (status) {
        CommandResult.Status.SUCCESS -> Texts.BackupEvents.DROP_SUCCESS
        CommandResult.Status.FAILURE -> Texts.BackupEvents.DROP_FAILURE
        CommandResult.Status.ERROR -> Texts.Responses.operationError(error, Texts.BackupEvents.DROP_ERROR)
    }
}
