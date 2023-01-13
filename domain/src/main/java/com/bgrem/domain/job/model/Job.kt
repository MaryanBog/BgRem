package com.bgrem.domain.job.model

data class Job(
    val id: String,
    val sourceUrl: String,
    val duration: Int,
    val size: Long,
    val isPortrait: Boolean,
    val thumbnailUrl: String?,
    val charged_seconds: Int,
    val price: Int,
    val videoWidth: Int,
    val videoHeight: Int
)
