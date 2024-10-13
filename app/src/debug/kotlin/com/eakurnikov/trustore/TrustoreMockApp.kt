package com.eakurnikov.trustore

import android.app.Application
import com.eakurnikov.trustore.di.AppComponent
import com.eakurnikov.trustore.di.DaggerAppComponent
import com.eakurnikov.trustore.ext.di.TrustoreModule
import com.eakurnikov.trustore.ext.stub.TrustoreStubBuilder
import com.eakurnikov.trustore.ext.stub.TrustoreStubDependencies

class TrustoreMockApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .trustoreModule(TrustoreModule(::TrustoreStubDependencies, ::TrustoreStubBuilder))
            .build()
    }
}
