package com.compose.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Background(
    @Json(name = "id")
    val id: String,
    @Json(name = "group")
    val group: String,
    @Json(name = "color")
    var color: String? = null,
    @Json(name = "file_url")
    val file_url: String? = null,
    @Json(name = "thumbnail_url")
    val thumbnail_url: String? = null,
    @Json(name = "poster_url")
    val poster_url: String? = null,
    @Json(name = "small_thumbnail_url")
    val small_thumbnail_url: String? = null,
    @Json(name = "small_poster_url")
    val small_poster_url: String? = null,
    @Json(name = "is_favorite")
    var is_favorite: Boolean = false
)