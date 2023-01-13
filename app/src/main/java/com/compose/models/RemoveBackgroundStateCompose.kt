package com.compose.models

data class RemoveBackgroundStateCompose(
    val progress: Int = 0,
    val error: Throwable? = null
)