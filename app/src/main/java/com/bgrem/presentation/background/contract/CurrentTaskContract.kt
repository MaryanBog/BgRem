package com.bgrem.presentation.background.contract

import androidx.lifecycle.LiveData
import com.bgrem.domain.task.model.Task

interface CurrentTaskContract {
    val taskLiveData: LiveData<Task>
}