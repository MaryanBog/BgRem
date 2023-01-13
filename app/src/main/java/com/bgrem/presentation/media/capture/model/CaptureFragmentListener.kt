package com.bgrem.presentation.media.capture.model

import android.net.Uri

interface CaptureFragmentListener {
    fun onImageSaved(uri: Uri)
    fun onVideoSaved(uri: Uri)
}