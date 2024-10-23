package com.eakurnikov.trustore.domain.errors

class UncaughtExceptionHandlerBuilder {
    val throwableHandlers: MutableList<ThrowableHandler> = arrayListOf()

    fun build(): Thread.UncaughtExceptionHandler {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        return Thread.UncaughtExceptionHandler { thread: Thread, exception: Throwable ->
            try {
                throwableHandlers.forEach { handler: ThrowableHandler ->
                    handler.handle(exception)
                }
            } finally {
                defaultHandler?.uncaughtException(thread, exception)
            }
        }
    }
}
