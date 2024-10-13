package com.eakurnikov.trustore.api

/**
 * We can't simply use kotlin.Result as covariance/contravariance are not supported by ObjC.
 * Sealed interfaces are also not supported. So we're defining a class with a simple generic type.
 */
class CommandResult<T>(
    val status: Status,
    val value: T?,
    val error: Throwable?
) {
    enum class Status {
        SUCCESS,
        FAILURE,
        ERROR
    }
}
