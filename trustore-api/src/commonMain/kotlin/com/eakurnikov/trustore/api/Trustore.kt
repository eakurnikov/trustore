package com.eakurnikov.trustore.api

interface Trustore {
    suspend fun command(command: ControlCommand): CommandResult
    suspend fun command(command: ReadCommand): CommandResult
    suspend fun command(command: WriteCommand): CommandResult

    interface Dependencies {
        fun store(): Store
        fun transactions(): Transactions
    }

    interface Builder {
        fun dependencies(dependencies: Dependencies): Builder
        fun create(): Trustore
    }
}
