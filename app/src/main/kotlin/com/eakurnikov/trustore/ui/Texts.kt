package com.eakurnikov.trustore.ui

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
    }

    object Responses {
        private const val ERROR = "Error! "

        const val KEY_NOT_SET_FAILURE = "Key not set"
        const val NO_TRANSACTION_FAILURE = "No transaction"

        const val SET_USAGE_ERROR = "${ERROR}Usage: SET <key> <value>"
        const val GET_USAGE_ERROR = "${ERROR}Usage: Usage: GET <key>"
        const val DELETE_USAGE_ERROR = "${ERROR}Usage: DELETE <key>"
        const val COUNT_USAGE_ERROR = "${ERROR}Usage: Usage: COUNT <value>"

        fun operationError(error: Throwable?): String {
            return "${ERROR}${error?.message ?: "Unknown error"}"
        }

        fun unknownCommandError(command: String): String {
            return "${ERROR}Unknown command: $command"
        }

        fun logInput(input: String): String {
            return "> $input"
        }
    }
}
