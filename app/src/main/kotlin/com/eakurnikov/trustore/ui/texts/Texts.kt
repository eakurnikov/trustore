package com.eakurnikov.trustore.ui.texts

object Texts {
    const val LOGO = "Trustore"

    object CommandNames {
        const val SET = "SET"
        const val GET = "GET"
        const val DELETE = "DELETE"
        const val COUNT = "COUNT"
        const val BEGIN = "BEGIN"
        const val COMMIT = "COMMIT"
        const val ROLLBACK = "ROLLBACK"

        const val CLEAR = "CLEAR"
        const val SAVE = "SAVE"
        const val RESTORE = "RESTORE"
        const val DROP = "DROP"
    }

    object Responses {
        private const val ERROR = "Error! "

        const val KEY_NOT_SET_FAILURE = "Key not set"
        const val NO_TRANSACTION_FAILURE = "No transaction"

        const val SET_USAGE_ERROR = "${ERROR}Usage: SET <key> <value>"
        const val GET_USAGE_ERROR = "${ERROR}Usage: Usage: GET <key>"
        const val DELETE_USAGE_ERROR = "${ERROR}Usage: DELETE <key>"
        const val COUNT_USAGE_ERROR = "${ERROR}Usage: Usage: COUNT <value>"

        fun operationError(error: Throwable?, message: String? = null): String {
            val defaultMessage = "$ERROR${error?.message ?: "Unknown error"}"
            return "${message?.let { "$it\n" } ?: ""}$defaultMessage"
        }

        fun unknownCommandError(command: String): String {
            return "${ERROR}Unknown command: $command"
        }

        fun logInput(input: String): String {
            return "> $input"
        }
    }

    object BackupEvents {
        const val CLEAR_SUCCESS = "Storage cleared successfully"
        const val CLEAR_ERROR = "Failed to clear storage"
        const val SAVE_SUCCESS = "Backup saved successfully"
        const val SAVE_ERROR = "Failed to save backup"
        const val RESTORE_SUCCESS = "Backup restored successfully"
        const val RESTORE_FAILURE = "No backup found to be restored"
        const val RESTORE_ERROR = "Failed to restore backup"
        const val DROP_SUCCESS = "Backup dropped successfully"
        const val DROP_FAILURE = "No backup found to be dropped"
        const val DROP_ERROR = "Failed to drop backup"
    }
}
