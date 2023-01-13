package com.compose.presentation.gallery.video

import android.net.Uri
import com.bgrem.domain.common.media.MediaType
import java.io.File

sealed class VideoAction{
    object StartVideo: VideoAction()
    object Error: VideoAction()
    data class TruncateVideo(val uri: Uri): VideoAction()
    data class VideoSelected(
        val file: File,
        val mediaType: MediaType,
        val mimeType: String,
        val uri: Uri?
    ): VideoAction()
}
