package com.bgrem.presentation.trimming

import android.net.Uri

interface TrimmingListener {
    fun onError()
    fun onCancel()
    fun onDone(uri: Uri?)
}