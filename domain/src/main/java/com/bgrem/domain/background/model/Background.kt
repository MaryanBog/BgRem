package com.bgrem.domain.background.model

data class Background(
    val id: String,
    val group: BackgroundGroup,
    val color: String?,
    val fileUrl: String?,
    val thumbnailUrl: String?,
    val posterUrl: String?,
    val backgroundType: BackgroundType?
)
