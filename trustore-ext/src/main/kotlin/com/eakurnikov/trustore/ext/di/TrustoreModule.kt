package com.eakurnikov.trustore.ext.di

import com.eakurnikov.trustore.api.Trustore
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class TrustoreModule(
    private val dependenciesProvider: Provider<Trustore.Dependencies>,
    private val builderProvider: Provider<Trustore.Builder>
) {
    @Singleton
    @Provides
    fun provideTrustore(): Trustore {
        return builderProvider.get().dependencies(dependenciesProvider.get()).create()
    }
}
