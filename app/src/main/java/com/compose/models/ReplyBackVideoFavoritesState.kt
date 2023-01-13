package com.compose.models

data class ReplyBackVideoFavoritesState(
    val backVideo: List<Background> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
