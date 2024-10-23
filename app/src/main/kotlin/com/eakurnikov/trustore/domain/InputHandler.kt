package com.eakurnikov.trustore.domain

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.Trustore
import com.eakurnikov.trustore.domain.backup.BackupStorage
import com.eakurnikov.trustore.domain.backup.CustomCommands
import com.eakurnikov.trustore.impl.Commands
import com.eakurnikov.trustore.ui.texts.Texts
import javax.inject.Inject
import javax.inject.Singleton

interface InputHandler {
    suspend fun handleInput(input: String): InputEvent
}

@Singleton
class InputHandlerImpl @Inject constructor(
    private val trustore: Trustore,
    private val backupStorage: BackupStorage
) : InputHandler {

    override suspend fun handleInput(input: String): InputEvent {
        val parts: List<String> = input.trim().split("\\s+".toRegex())
        val command: String = parts[0]

        return when (command.uppercase()) {
            Texts.CommandNames.SET -> onCommandSet(parts)
            Texts.CommandNames.GET -> onCommandGet(parts)
            Texts.CommandNames.DELETE -> onCommandDelete(parts)
            Texts.CommandNames.COUNT -> onCommandCount(parts)
            Texts.CommandNames.BEGIN -> onCommandBegin()
            Texts.CommandNames.COMMIT -> onCommandCommit()
            Texts.CommandNames.ROLLBACK -> onCommandRollback()
            Texts.CommandNames.SAVE -> onCommandSave()
            Texts.CommandNames.CLEAR -> onCommandClear()
            Texts.CommandNames.RESTORE -> onCommandRestore()
            Texts.CommandNames.DROP -> onCommandDrop()
            else -> InputEvent.CommandUnknown(command)
        }
    }

    private suspend fun onCommandSet(input: List<String>): InputEvent {
        if (input.size < 3) {
            return InputEvent.CommandUsageError(Texts.CommandNames.SET)
        }
        val key: String = input[1]
        val value: String = input[2]
        val result: CommandResult = trustore.command(Commands.Set(key, value))
        return InputEvent.CommandExecuted(Texts.CommandNames.SET, result)
    }

    private suspend fun onCommandGet(input: List<String>): InputEvent {
        if (input.size < 2) {
            return InputEvent.CommandUsageError(Texts.CommandNames.GET)
        }
        val key: String = input[1]
        val result: CommandResult = trustore.command(Commands.Get(key))
        return InputEvent.CommandExecuted(Texts.CommandNames.GET, result)
    }

    private suspend fun onCommandDelete(input: List<String>): InputEvent {
        if (input.size < 2) {
            return InputEvent.CommandUsageError(Texts.CommandNames.DELETE)
        }
        val key: String = input[1]
        val result: CommandResult = trustore.command(Commands.Delete(key))
        return InputEvent.CommandExecuted(Texts.CommandNames.DELETE, result)
    }

    private suspend fun onCommandCount(input: List<String>): InputEvent {
        if (input.size < 2) {
            return InputEvent.CommandUsageError(Texts.CommandNames.COUNT)
        }
        val value: String = input[1]
        val result: CommandResult = trustore.command(Commands.Count(value))
        return InputEvent.CommandExecuted(Texts.CommandNames.COUNT, result)
    }

    private suspend fun onCommandBegin(): InputEvent {
        val result: CommandResult = trustore.command(Commands.Begin)
        return InputEvent.CommandExecuted(Texts.CommandNames.BEGIN, result)
    }

    private suspend fun onCommandCommit(): InputEvent {
        val result: CommandResult = trustore.command(Commands.Commit)
        return InputEvent.CommandExecuted(Texts.CommandNames.COMMIT, result)
    }

    private suspend fun onCommandRollback(): InputEvent {
        val result: CommandResult = trustore.command(Commands.Rollback)
        return InputEvent.CommandExecuted(Texts.CommandNames.ROLLBACK, result)
    }

    private suspend fun onCommandSave(): InputEvent {
        val result: CommandResult = trustore.command(CustomCommands.Save(backupStorage))
        return InputEvent.CommandExecuted(Texts.CommandNames.SAVE, result)
    }

    private suspend fun onCommandClear(): InputEvent {
        val result: CommandResult = trustore.command(CustomCommands.Clear)
        return InputEvent.CommandExecuted(Texts.CommandNames.CLEAR, result)
    }

    private suspend fun onCommandRestore(): InputEvent {
        val result: CommandResult = trustore.command(CustomCommands.Restore(backupStorage))
        return InputEvent.CommandExecuted(Texts.CommandNames.RESTORE, result)
    }

    private suspend fun onCommandDrop(): InputEvent {
        val result: CommandResult = CustomCommands.Drop.execute(backupStorage)
        return InputEvent.CommandExecuted(Texts.CommandNames.DROP, result)
    }
}
