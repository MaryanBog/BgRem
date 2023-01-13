package com.bgrem.domain.task.model

import com.bgrem.domain.common.media.MediaType

data class DownloadResultFileParams(
    val taskId: String,
    val mediaType: MediaType,
    val isTransparent: Boolean
)
