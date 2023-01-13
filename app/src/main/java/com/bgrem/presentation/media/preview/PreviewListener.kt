package com.bgrem.presentation.media.preview

import android.net.Uri

interface PreviewListener {
    fun onRetry()
    fun onCancel()
    fun onDone(uri: Uri)
}