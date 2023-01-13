package com.bgrem.presentation.background.removing

import com.bgrem.domain.task.model.Task

interface RemoveBackgroundListener {
    fun onRemovingFinished(task: Task)
}