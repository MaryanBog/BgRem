package com.compose.models

data class ReplyBackImageState(
    val backImage: List<BackImage> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
