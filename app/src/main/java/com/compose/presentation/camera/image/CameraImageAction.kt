package com.compose.presentation.camera.image

sealed class CameraImageAction{
    object Start: CameraImageAction()
    object Error: CameraImageAction()
    object TakePhoto: CameraImageAction()
}
