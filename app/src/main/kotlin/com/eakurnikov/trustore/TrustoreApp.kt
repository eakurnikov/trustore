package com.eakurnikov.trustore

import android.app.Application
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

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .trustoreModule(TrustoreModule(::TrustoreDependencies, ::TrustoreBuilder))
            .build()
        appComponent.inject(this)

        backupManager.onInit()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                backupManager.onLowMemory()
            },
            15000
        )
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }
}
