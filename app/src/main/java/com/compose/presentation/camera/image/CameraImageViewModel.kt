package com.compose.presentation.camera.image

import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.data.FileStorages
import com.compose.utils.MimeTypesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraImageViewModel @Inject constructor(
    private val fileStorages: FileStorages,
    private val mimeTypes: MimeTypesUtils,
) : ViewModel() {

    private val _cameraImageActionState =
        MutableStateFlow<CameraImageAction>(CameraImageAction.Start)
    val cameraImageActionState = _cameraImageActionState.asStateFlow()

    private val _currentCamera = MutableStateFlow(CameraSelector.DEFAULT_BACK_CAMERA)
    val currentCamera = _currentCamera.asStateFlow()

    val mediaFile = fileStorages.createTempCacheFile(
        mimeTypes.getFileExtensionImageType(
            mimeTypes.getMimeTypeForNewImage()
        )
    )

    fun switchCamera() {
        viewModelScope.launch {
            if (currentCamera.value == CameraSelector.DEFAULT_FRONT_CAMERA) {
                _currentCamera.emit(CameraSelector.DEFAULT_BACK_CAMERA)
            } else {
                _currentCamera.emit(CameraSelector.DEFAULT_FRONT_CAMERA)
            }
        }
    }

    fun onTakePhoto() {
        viewModelScope.launch {
            _cameraImageActionState.value = CameraImageAction.TakePhoto
        }
    }
}