package com.bgrem.presentation.background.removing.model

import com.bgrem.domain.task.model.Task

sealed class RemoveBackgroundAction {
    data class RemovingFinished(val task: Task) : RemoveBackgroundAction()
}
