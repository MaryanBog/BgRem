package com.bgrem.presentation.media.capture.model

sealed class CaptureAction {
    object StopVideoRecording : CaptureAction()
    object OnVideoRecordingStopped : CaptureAction()
}
