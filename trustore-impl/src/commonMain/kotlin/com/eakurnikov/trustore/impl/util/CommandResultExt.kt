package com.eakurnikov.trustore.impl.util

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.CommandResult.Status

internal inline fun <T> success(value: T) = CommandResult(
    status = Status.SUCCESS,
    value = value,
    error = null
)

internal inline fun success() = CommandResult(
    status = Status.SUCCESS,
    value = Unit,
    error = null
)

internal inline fun <T> failure() = CommandResult<T>(
    status = Status.FAILURE,
    value = null,
    error = null
)

internal inline fun <T> error(error: Throwable) = CommandResult<T>(
    status = Status.ERROR,
    value = null,
    error = error
)
