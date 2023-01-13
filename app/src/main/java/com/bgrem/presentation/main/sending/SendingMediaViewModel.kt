package com.bgrem.presentation.main.sending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.job.CreateJobAndRemoveBackgroundUseCase
import com.bgrem.domain.job.model.CreateJobParams
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.extensions.asLiveData
import com.bgrem.presentation.common.extensions.postEvent
import com.bgrem.presentation.main.model.SelectedMediaInfo
import com.bgrem.presentation.main.sending.model.SendingMediaAction
import com.bgrem.presentation.main.sending.model.SendingMediaState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SendingMediaViewModel(
    private val selectedMediaInfo: SelectedMediaInfo,
    private val createJobAndRemoveBackgroundUseCase: CreateJobAndRemoveBackgroundUseCase
) : StateViewModel<SendingMediaState, SendingMediaAction>(SendingMediaState()) {

    private val _selectedMediaType = MutableLiveData<MediaType>()
    val selectedMediaType = _selectedMediaType.asLiveData()

    init {
        _selectedMediaType.value = selectedMediaInfo.mediaType
        sendMedia()
    }

    fun sendMedia() {
            viewModelScope.launch {
            val param = CreateJobParams(selectedMediaInfo.file, selectedMediaInfo.mimeType)
            createJobAndRemoveBackgroundUseCase(param)
                .onStart { _state.value = _state.value?.copy(isLoading = true, error = null) }
                .collect { result ->
                    result
                        .onSuccess {
                            _event.postEvent(
                                SendingMediaAction.MediaSent(
                                    task = it,
                                    mediaType = selectedMediaInfo.mediaType
                                )
                            )
                        }
                        .onFailure {
                            _state.value = _state.value?.copy(isLoading = false, error = it)
                        }
                }
        }
    }
}