package com.compose.presentation.camera.video

sealed class CameraVideoAction{
    object Start: CameraVideoAction()
    object Error: CameraVideoAction()
    object RecordVideo: CameraVideoAction()
    object StopVideo: CameraVideoAction()
}
