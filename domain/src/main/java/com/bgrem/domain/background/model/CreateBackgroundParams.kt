package com.bgrem.domain.background.model

import com.bgrem.domain.common.media.MediaType
import java.io.File

data class CreateBackgroundParams(
    val file: File,
    val mimeType: String,
    val thumbnailUri: String?,
    val mediaType: MediaType
)
