package com.eakurnikov.trustore.domain.backup

sealed interface BackupEvent {

    sealed interface Restore : BackupEvent {
        data object Success : Restore
        data object Failure : Restore
        data class Error(val error: Throwable?) : Restore
    }

    sealed interface Save : BackupEvent {
        data object Success : Save
        data object Failure : Save
        data class Error(val error: Throwable?) : Save
    }
}
