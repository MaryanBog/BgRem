package com.compose.models

import com.squareup.moshi.Json

enum class BackgroundGroup {
    @Json(name = "color")
    COLOR,
    @Json(name = "image")
    IMAGE,
    @Json(name = "video")
    VIDEO,
    @Json(name = "user")
    USER,
    @Json(name = "transparent")
    TRANSPARENT
}