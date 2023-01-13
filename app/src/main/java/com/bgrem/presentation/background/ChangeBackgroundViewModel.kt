package com.bgrem.presentation.background

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.task.GetTaskUseCase
import com.bgrem.domain.task.model.Task
import com.bgrem.presentation.background.model.ChangeBackgroundAction
import com.bgrem.presentation.background.model.ChangeBackgroundState
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.extensions.asLiveData
import com.bgrem.presentation.common.extensions.postEvent
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChangeBackgroundViewModel(
    val contentMediaType: MediaType,
    private val taskId: String,
    private val getTaskUseCase: GetTaskUseCase
) : StateViewModel<ChangeBackgroundState, ChangeBackgroundAction>(ChangeBackgroundState()) {

    private val _taskLiveData = MutableLiveData<Task>()
    val taskLiveData = _taskLiveData.asLiveData()

    init {
        fetchTask()
    }

    fun fetchTask() {
        viewModelScope.launch {
            getTaskUseCase(taskId)
                .onStart { _state.value = _state.value?.copy(isLoading = true, error = null) }
                .onCompletion { _state.value = _state.value?.copy(isLoading = false) }
                .collect { result ->
                    result
                        .onSuccess {
                            _taskLiveData.value = it
                            _event.postEvent(ChangeBackgroundAction.ShowSelectBackground)
                        }
                        .onFailure {
                            _state.value = _state.value?.copy(error = it)
                        }
                }
        }
    }
}