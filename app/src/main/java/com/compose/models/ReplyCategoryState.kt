package com.compose.models

data class ReplyCategoryState(
    val category: List<Category> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
