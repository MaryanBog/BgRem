package com.bgrem.domain.task.model

sealed interface RemoveBackgroundProgressState {
    @JvmInline
    value class Progress(val value: Int) : RemoveBackgroundProgressState
    @JvmInline
    value class Finished(val task: Task) : RemoveBackgroundProgressState
}
