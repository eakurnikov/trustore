package com.eakurnikov.trustore.domain.errors

interface ThrowableHandler {
    fun handle(throwable: Throwable)
}
