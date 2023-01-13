package com.compose.models

data class ReplyBackImagesFavoritesState(
    val backImage: List<Background> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
