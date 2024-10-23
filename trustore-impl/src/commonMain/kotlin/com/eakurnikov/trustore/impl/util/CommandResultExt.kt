package com.eakurnikov.trustore.impl.util

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.CommandResult.Status

internal inline fun success(value: Any? = Unit) = CommandResult(
    status = Status.SUCCESS,
    value = value,
    error = null
)

internal inline fun failure() = CommandResult(
    status = Status.FAILURE,
    value = null,
    error = null
)

internal inline fun error(error: Throwable) = CommandResult(
    status = Status.ERROR,
    value = null,
    error = error
)
