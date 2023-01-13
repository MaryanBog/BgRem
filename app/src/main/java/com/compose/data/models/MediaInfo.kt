package com.compose.data.models

import com.bgrem.domain.common.media.MediaType
import java.io.File

data class MediaInfo(
    val file: File? = null,
    val mediaType: MediaType? = null,
    val mimeType: String? = null
)
