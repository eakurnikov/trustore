package com.eakurnikov.trustore.ui.texts

import com.eakurnikov.trustore.domain.InputEvent
import com.eakurnikov.trustore.domain.backup.BackupEvent

object TextResponseUiModel {

    fun mapToText(event: BackupEvent): String = when (event) {
        is BackupEvent.Save -> event.mapToText()
        is BackupEvent.Restore -> event.mapToText()
    }

    fun mapToText(event: InputEvent): String? = when (event) {
        is InputEvent.CommandUnknown -> Texts.Responses.unknownCommandError(event.command)
        is InputEvent.CommandUsageError -> event.mapToText()
        is InputEvent.CommandExecuted -> CommandResponseUiModel.mapToText(event)
    }

    private fun InputEvent.CommandUsageError.mapToText(): String? = when (command) {
        Texts.CommandNames.SET -> Texts.Responses.SET_USAGE_ERROR
        Texts.CommandNames.GET -> Texts.Responses.GET_USAGE_ERROR
        Texts.CommandNames.DELETE -> Texts.Responses.DELETE_USAGE_ERROR
        Texts.CommandNames.COUNT -> Texts.Responses.COUNT_USAGE_ERROR
        else -> null
    }

    private fun BackupEvent.Save.mapToText(): String = when (this) {
        BackupEvent.Save.Success -> Texts.BackupEvents.SAVE_SUCCESS
        BackupEvent.Save.Failure -> Texts.BackupEvents.SAVE_ERROR
        is BackupEvent.Save.Error -> Texts.Responses.operationError(error, Texts.BackupEvents.SAVE_ERROR)
    }

    private fun BackupEvent.Restore.mapToText(): String = when (this) {
        BackupEvent.Restore.Success -> Texts.BackupEvents.RESTORE_SUCCESS
        BackupEvent.Restore.Failure -> Texts.BackupEvents.RESTORE_FAILURE
        is BackupEvent.Restore.Error -> Texts.Responses.operationError(error, Texts.BackupEvents.RESTORE_ERROR)
    }
}
