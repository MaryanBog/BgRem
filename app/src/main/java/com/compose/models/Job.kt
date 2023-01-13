package com.compose.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Job(
    @Json(name = "id")
    val id: String,
    @Json(name = "source_url")
    val sourceUrl: String,
    @Json(name = "duration")
    val duration: Int,
    @Json(name = "size")
    val size: Long,
    @Json(name = "is_portrait")
    val isPortrait: Boolean,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @Json(name = "charged_seconds")
    val charged_seconds: Int,
    @Json(name = "price")
    val price: Int,
    @Json(name = "video_width")
    val videoWidth: Int,
    @Json(name = "video_height")
    val videoHeight: Int,
    @Json(name = "media_type")
    val media_type: String,
    @Json(name = "status")
    val status: String,
)