package com.compose.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoritesFirstFiveResult(
    @Json(name = "results")
    val results: List<Background> = emptyList()
)