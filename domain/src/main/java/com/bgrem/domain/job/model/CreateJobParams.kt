package com.bgrem.domain.job.model

import java.io.File

data class CreateJobParams(
    val file: File,
    val mimeType: String
)
