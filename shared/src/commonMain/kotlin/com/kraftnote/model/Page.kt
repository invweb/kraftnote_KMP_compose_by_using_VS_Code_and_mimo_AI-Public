package com.kraftnote.model

import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
