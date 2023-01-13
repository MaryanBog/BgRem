package com.compose.presentation.background.gallery

import android.net.Uri

sealed class YourBgAction{
    object Start: YourBgAction()
    data class AddImage(val uri: Uri?): YourBgAction()
    data class AddVideo(val uri: Uri): YourBgAction()
}
