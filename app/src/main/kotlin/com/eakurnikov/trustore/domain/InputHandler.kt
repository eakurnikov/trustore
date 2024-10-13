package com.eakurnikov.trustore.domain

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.Trustore
import com.eakurnikov.trustore.impl.Commands
import com.eakurnikov.trustore.ui.Texts
import javax.inject.Inject

class InputHandler @Inject constructor(
    private val trustore: Trustore
) {
    suspend fun onInput(input: String): String? {
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
            else -> Texts.Responses.unknownCommandError(command)
        }
    }

    private suspend fun onCommandSet(input: List<String>): String? {
        return if (input.size < 3) {
            Texts.Responses.SET_USAGE_ERROR
        } else {
            val key: String = input[1]
            val value: String = input[2]

            val result: CommandResult<Unit> = trustore.command(Commands.Set(key, value))
            when (result.status) {
                CommandResult.Status.SUCCESS -> null
                else -> Texts.Responses.operationError(result.error)
            }
        }
    }

    private suspend fun onCommandGet(input: List<String>): String? {
        return if (input.size < 2) {
            Texts.Responses.GET_USAGE_ERROR
        } else {
            val key: String = input[1]

            val result: CommandResult<String?> = trustore.command(Commands.Get(key))
            when (result.status) {
                CommandResult.Status.SUCCESS -> result.value
                CommandResult.Status.FAILURE -> Texts.Responses.KEY_NOT_SET_FAILURE
                CommandResult.Status.ERROR -> Texts.Responses.operationError(result.error)
            }
        }
    }

    private suspend fun onCommandDelete(input: List<String>): String? {
        return if (input.size < 2) {
            Texts.Responses.DELETE_USAGE_ERROR
        } else {
            val key: String = input[1]

            val result: CommandResult<Unit> = trustore.command(Commands.Delete(key))
            when (result.status) {
                CommandResult.Status.SUCCESS -> null
                else -> Texts.Responses.operationError(result.error)
            }
        }
    }

    private suspend fun onCommandCount(input: List<String>): String? {
        return if (input.size < 2) {
            Texts.Responses.COUNT_USAGE_ERROR
        } else {
            val value: String = input[1]

            val result: CommandResult<String?> = trustore.command(Commands.Count(value))
            when (result.status) {
                CommandResult.Status.SUCCESS -> result.value
                else -> Texts.Responses.operationError(result.error)
            }
        }
    }

    private suspend fun onCommandBegin(): String? {
        val result: CommandResult<Unit> = trustore.command(Commands.Begin)
        return when (result.status) {
            CommandResult.Status.SUCCESS -> null
            else -> Texts.Responses.operationError(result.error)
        }
    }

    private suspend fun onCommandCommit(): String? {
        val result: CommandResult<Unit> = trustore.command(Commands.Commit)
        return when (result.status) {
            CommandResult.Status.SUCCESS -> null
            CommandResult.Status.FAILURE -> Texts.Responses.NO_TRANSACTION_FAILURE
            CommandResult.Status.ERROR -> Texts.Responses.operationError(result.error)
        }
    }

    private suspend fun onCommandRollback(): String? {
        val result: CommandResult<Unit> = trustore.command(Commands.Rollback)
        return when (result.status) {
            CommandResult.Status.SUCCESS -> null
            CommandResult.Status.FAILURE -> Texts.Responses.NO_TRANSACTION_FAILURE
            CommandResult.Status.ERROR -> Texts.Responses.operationError(result.error)
        }
    }
}
