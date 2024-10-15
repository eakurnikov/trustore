package com.eakurnikov.trustore.ext.stub

import com.eakurnikov.trustore.api.CommandResult
import com.eakurnikov.trustore.api.ControlCommand
import com.eakurnikov.trustore.api.ReadCommand
import com.eakurnikov.trustore.api.Trustore
import com.eakurnikov.trustore.api.WriteCommand

class TrustoreStubBuilder : Trustore.Builder {

    private val resultUnitStub = CommandResult(
        status = CommandResult.Status.FAILURE,
        value = null,
        error = null
    )

    private val resultStringStub = CommandResult(
        status = CommandResult.Status.FAILURE,
        value = null,
        error = null
    )

    private val trustoreStub = object : Trustore {
        override suspend fun command(command: ControlCommand): CommandResult = resultUnitStub
        override suspend fun command(command: ReadCommand): CommandResult = resultStringStub
        override suspend fun command(command: WriteCommand): CommandResult = resultUnitStub
    }

    override fun dependencies(dependencies: Trustore.Dependencies): Trustore.Builder = this

    override fun create(): Trustore = trustoreStub
}
