package com.compose.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BackColor(
    @Json(name = "id")
    val id: String,
    @Json(name = "group")
    val group: BackgroundGroup,
    @Json(name = "color")
    val color: String?
)
