package com.bgrem.presentation.background.model

data class ChangeBackgroundState(
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
