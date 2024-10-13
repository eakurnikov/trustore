package com.eakurnikov.trustore

import android.app.Application
import com.eakurnikov.trustore.di.AppComponent
import com.eakurnikov.trustore.di.DaggerAppComponent
import com.eakurnikov.trustore.ext.di.TrustoreModule
import com.eakurnikov.trustore.impl.di.TrustoreBuilder
import com.eakurnikov.trustore.impl.di.TrustoreDependencies

class TrustoreApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .trustoreModule(TrustoreModule(::TrustoreDependencies, ::TrustoreBuilder))
            .build()
    }
}
