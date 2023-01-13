package com.bgrem.presentation.background.removing

import androidx.lifecycle.viewModelScope
import com.bgrem.domain.task.RemoveBackgroundInteractor
import com.bgrem.domain.task.model.RemoveBackgroundProgressState
import com.bgrem.presentation.background.removing.model.RemoveBackgroundAction
import com.bgrem.presentation.background.removing.model.RemoveBackgroundState
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.extensions.postEvent
import com.bgrem.domain.common.media.MediaType
import kotlinx.coroutines.launch

class RemoveBackgroundViewModel(
    private val backgroundId: String?,
    private val mediaType: MediaType,
    private val removeBackgroundInteractor: RemoveBackgroundInteractor
) : StateViewModel<RemoveBackgroundState, RemoveBackgroundAction>(RemoveBackgroundState()) {

    init {
        observeRemovingBackgroundProgress()
        removeBackground()
    }

    fun removeBackground() {
        viewModelScope.launch {
            removeBackgroundInteractor.removeBackground(backgroundId, mediaType)
        }
    }

    private fun observeRemovingBackgroundProgress() {
        viewModelScope.launch {
            removeBackgroundInteractor.createTaskProgress.collect { result ->
                result
                    .onSuccess { removeState ->
                        when (removeState) {
                            is RemoveBackgroundProgressState.Progress -> _state.value =
                                _state.value?.copy(progress = removeState.value, error = null)

                            is RemoveBackgroundProgressState.Finished -> _event.postEvent(
                                RemoveBackgroundAction.RemovingFinished(removeState.task)
                            )
                        }
                    }
                    .onFailure {
                        _state.value = _state.value?.copy(error = it)
                    }
            }
        }
    }
}