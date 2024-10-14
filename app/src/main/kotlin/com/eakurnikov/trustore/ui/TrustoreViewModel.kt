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

    val lines: SnapshotStateList<String> = mutableStateListOf()

    fun onInputSubmitted(input: String) {
        if (input.isBlank()) {
            return
        }
        viewModelScope.launch {
            try {
                lines += Texts.Responses.logInput(input)
                inputHandler.handleInput(input)?.let { output: String ->
                    lines += output
                }
            } catch (e: Throwable) {
                lines += Texts.Responses.operationError(e)
            }
        }
    }
}
