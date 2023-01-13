package com.compose.models

data class ReplyBackColorState(
    val backColor: List<Background> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
