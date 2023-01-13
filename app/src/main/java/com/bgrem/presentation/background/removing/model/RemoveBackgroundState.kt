package com.bgrem.presentation.background.removing.model

data class RemoveBackgroundState(
    val progress: Int = 0,
    val error: Throwable? = null
)
