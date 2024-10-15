package com.eakurnikov.trustore.di

import android.content.Context
import com.eakurnikov.trustore.domain.InputHandler
import com.eakurnikov.trustore.domain.InputHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [AppModule.Bindings::class]
)
class AppModule(
    private val appContext: Context
) {
    @Singleton
    @Provides
    fun provideAppContext(): Context = appContext

    @Module
    interface Bindings {

        @Singleton
        @Binds
        fun bindsInputHandler(impl: InputHandlerImpl): InputHandler
    }
}
