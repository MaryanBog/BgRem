package com.bgrem.presentation.media.capture

import androidx.camera.core.CameraSelector
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.presentation.common.AppConstants
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.extensions.asLiveData
import com.bgrem.presentation.common.extensions.postEvent
import com.bgrem.presentation.media.capture.model.CameraAction
import com.bgrem.presentation.media.capture.model.CaptureAction
import com.bgrem.presentation.media.capture.model.CaptureState
import java.util.concurrent.TimeUnit

class CaptureViewModel(
    fileStorage: FileStorage,
    mimeTypeManager: MimeTypeManager,
    val mediaType: MediaType
) : StateViewModel<CaptureState, CaptureAction>() {

    private val _currentCameraLens = MutableLiveData<CameraSelector>()
    val currentCameraLens = _currentCameraLens.asLiveData()

    private val _currentVideoRecordingDuration = MutableLiveData<Int>()
    val currentVideoRecordingDuration = _currentVideoRecordingDuration.asLiveData()

    val mediaFile = fileStorage.createTempCacheFile(
        mimeTypeManager.getFileExtensionByMimeType(
            when (mediaType) {
                MediaType.IMAGE -> mimeTypeManager.getMimeTypeForNewImage()
                MediaType.VIDEO -> mimeTypeManager.getMimeTypeForNewVideo()
            }
        )
    )

    init {
        initState()
    }

    fun onCameraReady(cameraSelector: CameraSelector) {
        if (currentCameraLens.value == cameraSelector) return
        _currentCameraLens.postValue(cameraSelector)
    }

    fun onStartVideoRecording() {
        _state.value = _state.value?.copy(currentAction = CameraAction.STOP)
        _currentVideoRecordingDuration.value = 0
    }

    fun updateCurrentVideoRecordingDuration(durationInNanos: Long) {
        val seconds = TimeUnit.NANOSECONDS.toSeconds(durationInNanos).toInt()
        _currentVideoRecordingDuration.postValue(seconds)
    }

    fun onStopRecordingClicked() {
        _event.postEvent(CaptureAction.StopVideoRecording)
        _state.postValue(_state.value?.copy(currentAction = mediaType.toCameraAction()))
    }

    fun onVideoRecordingStopped() {
        _event.postEvent(CaptureAction.OnVideoRecordingStopped)
    }

    fun switchCameraLens() {
        _currentCameraLens.value =
            if (currentCameraLens.value == CameraSelector.DEFAULT_FRONT_CAMERA) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
    }

    private fun initState() {
        _state.value = CaptureState(currentAction = mediaType.toCameraAction())
    }

    private fun MediaType.toCameraAction() = when (this) {
        MediaType.IMAGE -> CameraAction.TAKE_PHOTO
        MediaType.VIDEO -> CameraAction.TAKE_VIDEO
    }
}