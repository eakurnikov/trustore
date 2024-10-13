package com.eakurnikov.trustore.impl.di

import com.eakurnikov.trustore.api.Trustore
import com.eakurnikov.trustore.impl.TrustoreImpl

class TrustoreBuilder : Trustore.Builder {
    private var dependencies: Trustore.Dependencies? = null

    override fun dependencies(dependencies: Trustore.Dependencies): TrustoreBuilder {
        return apply {
            this.dependencies = dependencies
        }
    }

    override fun create(): Trustore {
        val dependencies: Trustore.Dependencies = dependencies ?: error("Missing dependencies")
        return TrustoreImpl(
            store = dependencies.store(),
            transactions = dependencies.transactions()
        )
    }
}
