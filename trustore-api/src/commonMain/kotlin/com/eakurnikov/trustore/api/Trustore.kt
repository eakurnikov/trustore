package com.eakurnikov.trustore.api

interface Trustore {
    suspend fun command(command: ControlCommand): CommandResult<Unit>
    suspend fun command(command: ReadCommand): CommandResult<Any?>
    suspend fun command(command: WriteCommand): CommandResult<Unit>

    interface Dependencies {
        fun store(): Store
        fun transactions(): Transactions
    }

    interface Builder {
        fun dependencies(dependencies: Dependencies): Builder
        fun create(): Trustore
    }
}
