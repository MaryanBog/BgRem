package com.compose.presentation.gallery.image

import com.bgrem.domain.common.media.MediaType
import java.io.File

sealed class ImageAction{
    object StartImage: ImageAction()
    object Error: ImageAction()
    data class ImageSelected(
        val file: File,
        val mediaType: MediaType,
        val mimeType: String,
    ): ImageAction()
}