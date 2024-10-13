package com.eakurnikov.trustore.di

import com.eakurnikov.trustore.ext.di.TrustoreModule
import com.eakurnikov.trustore.ui.TrustoreActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        TrustoreModule::class
    ]
)
interface AppComponent {
    fun inject(activity: TrustoreActivity)
}
