package com.compose.data.models

sealed interface RemoveBackgroundProgressStateCompose {
    @JvmInline
    value class Progress(val value: Float) : RemoveBackgroundProgressStateCompose
    @JvmInline
    value class Finished(val task: com.compose.models.Task) : RemoveBackgroundProgressStateCompose

}