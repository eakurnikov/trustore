package com.eakurnikov.trustore.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eakurnikov.trustore.domain.InputHandler
import com.eakurnikov.trustore.domain.InputEvent
import com.eakurnikov.trustore.domain.backup.BackupEvent
import com.eakurnikov.trustore.domain.backup.BackupManager
import com.eakurnikov.trustore.ui.texts.TextResponseUiModel
import com.eakurnikov.trustore.ui.texts.Texts
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrustoreViewModel @Inject constructor(
    private val backupManager: BackupManager,
    private val inputHandler: InputHandler
) : ViewModel() {

    val lines: SnapshotStateList<String> = mutableStateListOf()

    init {
        viewModelScope.launch {
            backupManager.events.collect { event: BackupEvent ->
                lines += TextResponseUiModel.mapToText(event)
            }
        }
    }

    fun onInputSubmitted(input: String) {
        if (input.isBlank()) {
            return
        }
        viewModelScope.launch {
            try {
                lines += Texts.Responses.logInput(input)
                val event: InputEvent = inputHandler.handleInput(input)
                TextResponseUiModel.mapToText(event)?.let { lines += it }
            } catch (e: Throwable) {
                lines += Texts.Responses.operationError(e)
            }
        }
    }
}
