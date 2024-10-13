package com.eakurnikov.trustore.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eakurnikov.trustore.domain.InputHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrustoreViewModel @Inject constructor(
    private val inputHandler: InputHandler
) : ViewModel() {

    val commands: SnapshotStateList<String> = mutableStateListOf()

    fun onCommand(input: String) {
        if (input.isBlank()) {
            return
        }
        viewModelScope.launch {
            try {
                commands += Texts.Responses.logInput(input)
                inputHandler.onInput(input)?.let { result: String ->
                    commands += result
                }
            } catch (e: Throwable) {
                commands += Texts.Responses.operationError(e)
            }
        }
    }
}
