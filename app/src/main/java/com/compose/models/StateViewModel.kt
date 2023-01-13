package com.compose.models

class StateViewModel(
    val loading: Boolean = false,
    val error: Boolean = false,
    val exception: Boolean = false,
    val refreshing: Boolean = false,
    val serverError: Boolean = false
)

