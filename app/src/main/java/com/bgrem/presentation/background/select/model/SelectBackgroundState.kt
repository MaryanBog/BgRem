package com.bgrem.presentation.background.select.model

data class SelectBackgroundState(
    val isLoading: Boolean = false,
    val isPreviewPlayerVisible: Boolean = false,
    val error: Throwable? = null
)