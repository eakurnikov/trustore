package com.eakurnikov.trustore.api

/**
 * We could declare "interface Command" as a root sealed interface for all commands or even as a single interface with
 * generic Receiver type and pass it to Trustore into a single method, but ObjC doesn't support these features.
 * Having plain "interface Command" is too generic and client shouldn't be able to inherit it,
 * so we provide 3 more specific interfaces that can be extended instead.
 */

interface ControlCommand {
    suspend fun execute(transactions: Transactions): CommandResult
}

interface ReadCommand {
    suspend fun execute(store: Store.Read): CommandResult
}

interface WriteCommand {
    suspend fun execute(store: Store.Write): CommandResult
}
