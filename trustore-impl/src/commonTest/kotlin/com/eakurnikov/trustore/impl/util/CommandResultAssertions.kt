package com.eakurnikov.trustore.impl.util

import com.eakurnikov.trustore.api.CommandResult
import kotlin.test.assertEquals

fun assertSuccess(result: CommandResult) {
    assertEquals(CommandResult.Status.SUCCESS, result.status)
}

fun assertFailure(result: CommandResult) {
    assertEquals(CommandResult.Status.FAILURE, result.status)
}

fun assertError(result: CommandResult) {
    assertEquals(CommandResult.Status.ERROR, result.status)
}
