package com.eakurnikov.trustore.di

import com.eakurnikov.trustore.domain.errors.OutOfMemoryErrorHandler
import com.eakurnikov.trustore.domain.errors.ThrowableHandler
import com.eakurnikov.trustore.domain.errors.UncaughtExceptionHandlerBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes = [ErrorHandlingModule.Bindings::class]
)
object ErrorHandlingModule {

    @Singleton
    @Provides
    fun provideUncaughtExceptionHandler(
        @Named("OutOfMemoryErrorHandler")
        outOfMemoryErrorHandler: ThrowableHandler
    ): Thread.UncaughtExceptionHandler {
        return UncaughtExceptionHandlerBuilder().apply {
            throwableHandlers += outOfMemoryErrorHandler
        }.build()
    }

    @Module
    abstract class Bindings {

        @Named("OutOfMemoryErrorHandler")
        @Singleton
        @Binds
        abstract fun bindOutOfMemoryErrorHandler(impl: OutOfMemoryErrorHandler): ThrowableHandler
    }
}
