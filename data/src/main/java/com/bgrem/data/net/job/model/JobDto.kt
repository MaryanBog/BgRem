package com.bgrem.data.net.job.model

import com.bgrem.domain.job.model.Job
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobDto(
    val id: String,
    @SerialName("source_url")
    val sourceUrl: String,
    val duration: Int,
    val size: Long,
    @SerialName("is_portrait")
    val isPortrait: Boolean,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerialName("charged_seconds")
    val charged_seconds: Int,
    val price: Int,
    @SerialName("video_width")
    val videoWidth: Int,
    @SerialName("video_height")
    val videoHeight: Int
) {

    fun toDomain() = Job(
        id = id,
        sourceUrl = sourceUrl,
        duration = duration,
        size = size,
        isPortrait = isPortrait,
        thumbnailUrl = thumbnailUrl,
        charged_seconds = charged_seconds,
        price = price,
        videoWidth = videoWidth,
        videoHeight = videoHeight
    )
}
