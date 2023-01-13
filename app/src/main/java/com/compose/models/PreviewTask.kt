package com.compose.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PreviewTask(
    @Json(name = "id")
    val id: String,
    @Json(name = "result_url")
    val resultUrl: String
)