package com.compose.models

data class ReplyBackgroundsState(
    val backgrounds: List<Background> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
