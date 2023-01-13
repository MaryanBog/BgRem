package com.compose.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BackImage(
    @Json(name = "id")
    val id: String,
    @Json(name = "group")
    val group: BackgroundGroup,
    @Json(name = "thumbnail_url")
    val thumbnailUrl: String?
)
