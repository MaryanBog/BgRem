package com.compose.models

sealed class RemoveBackgroundActionCompose {
    data class RemovingFinished(val task: com.compose.models.Task) : RemoveBackgroundActionCompose()
}