package com.eakurnikov.trustore.impl

import com.eakurnikov.trustore.api.Store

internal data class Transaction(
    val backup: Store.Snapshot
)
