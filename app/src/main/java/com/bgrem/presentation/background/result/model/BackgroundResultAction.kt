package com.bgrem.presentation.background.result.model

import androidx.annotation.StringRes
import java.io.File

sealed class BackgroundResultAction {
    object DownloadFile : BackgroundResultAction()
    object SaveFileSucceeded : BackgroundResultAction()
    data class DownloadSuccessful(val file: File) : BackgroundResultAction()
    data class Error(@StringRes val messageRes: Int) : BackgroundResultAction()
}
