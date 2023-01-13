package com.compose.models

data class ReplyBackVideoState(
    val backImage: List<BackVideo> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
