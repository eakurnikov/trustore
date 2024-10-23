package com.eakurnikov.trustore.domain

import com.eakurnikov.trustore.api.CommandResult

sealed interface InputEvent {

    class CommandUnknown(
        val command: String
    ) : InputEvent

    class CommandUsageError(
        val command: String
    ) : InputEvent

    class CommandExecuted(
        val command: String,
        val result: CommandResult
    ) : InputEvent
}
