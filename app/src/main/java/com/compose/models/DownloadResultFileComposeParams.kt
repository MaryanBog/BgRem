package com.compose.models

import com.bgrem.domain.common.media.MediaType

data class DownloadResultFileComposeParams(
    val taskId: String,
    val mediaType: MediaType,
    val isTransparent: Boolean
)