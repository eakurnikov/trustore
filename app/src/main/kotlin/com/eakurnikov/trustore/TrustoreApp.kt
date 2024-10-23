package com.eakurnikov.trustore

import android.app.Application
import android.content.ComponentCallbacks2
import android.os.Handler
import android.os.Looper
import com.eakurnikov.trustore.di.AppComponent
import com.eakurnikov.trustore.di.AppModule
import com.eakurnikov.trustore.di.DaggerAppComponent
import com.eakurnikov.trustore.domain.backup.BackupManager
import com.eakurnikov.trustore.ext.di.TrustoreModule
import com.eakurnikov.trustore.impl.di.TrustoreBuilder
import com.eakurnikov.trustore.impl.di.TrustoreDependencies
import javax.inject.Inject

class TrustoreApp : Application() {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var backupManager: BackupManager

    @Inject
    lateinit var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .trustoreModule(TrustoreModule(::TrustoreDependencies, ::TrustoreBuilder))
            .build()
            .apply { inject(this@TrustoreApp) }

        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler)
        backupManager.onInit()

//        forceOnTrimMemory()
//        forceOutOfMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            backupManager.onLowMemory()
        }
    }

    private fun forceOnTrimMemory() {
        Handler(Looper.getMainLooper()).postDelayed(
            { onTrimMemory(ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) },
            15_000
        )
    }

    private fun forceOutOfMemory() {
        Handler(Looper.getMainLooper()).postDelayed(
            { throw OutOfMemoryError() },
            20_000
        )
    }
}
