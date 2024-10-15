package com.eakurnikov.trustore.domain.backup

import kotlinx.serialization.Serializable

@Serializable
data class Backup(
    val content: Map<String, String>
)
