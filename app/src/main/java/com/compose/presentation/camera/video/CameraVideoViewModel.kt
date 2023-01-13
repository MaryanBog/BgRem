package com.compose.presentation.camera.video

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.data.FileStorages
import com.compose.utils.MimeTypesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CameraVideoViewModel @Inject constructor(
    private val fileStorages: FileStorages,
    private val mimeTypes: MimeTypesUtils,
) : ViewModel() {

    private val _cameraVideoActionState =
        MutableStateFlow<CameraVideoAction>(CameraVideoAction.Start)
    val cameraVideoActionState = _cameraVideoActionState.asStateFlow()

    private val _currentCamera = MutableStateFlow(CameraSelector.DEFAULT_BACK_CAMERA)
    val currentCamera = _currentCamera.asStateFlow()

    private val _previewView = MutableStateFlow<Preview?>(null)
    val previewView = _previewView.asStateFlow()

    private val _currentVideoRecordingDuration = MutableStateFlow(0)
    val currentVideoRecordingDuration = _currentVideoRecordingDuration.asStateFlow()

    val mediaFile = fileStorages.createTempCacheFile(
        mimeTypes.getFileExtensionVideoType(
            mimeTypes.getMimeTypeForNewVideo()
        )
    )

    fun onRecordingVideo(){
        viewModelScope.launch {
            _cameraVideoActionState.value = CameraVideoAction.RecordVideo
        }
    }

    fun onStopRecordingVideo(){
        viewModelScope.launch {
            _cameraVideoActionState.value = CameraVideoAction.StopVideo
        }
    }

    fun switchCamera() {
        viewModelScope.launch {
            if (currentCamera.value == CameraSelector.DEFAULT_FRONT_CAMERA) {
                _currentCamera.emit(CameraSelector.DEFAULT_BACK_CAMERA)
            } else {
                _currentCamera.emit(CameraSelector.DEFAULT_FRONT_CAMERA)
            }
        }
    }

    fun getPreview(previewUseCase: Preview) {
        viewModelScope.launch {
            _previewView.emit(previewUseCase)
        }
    }

    fun onVideoRecordingIndicator(durationInNanos: Long) {
        viewModelScope.launch {
            val seconds = TimeUnit.NANOSECONDS.toSeconds(durationInNanos).toInt()
            _currentVideoRecordingDuration.emit(seconds)
        }
    }
}