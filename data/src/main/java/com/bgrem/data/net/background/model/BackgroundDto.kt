package com.bgrem.data.net.background.model

import com.bgrem.domain.background.model.Background
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackgroundDto(
    val id: String,
    val group: BackgroundGroupDto,
    val color: String? = null,
    @SerialName("file_url")
    val fileUrl: String? = null,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerialName("poster_url")
    val posterUrl: String? = null
) {

    fun toDomain() = Background(
        id = id,
        group = group.toDomain(),
        color = color,
        fileUrl = fileUrl,
        thumbnailUrl = thumbnailUrl,
        posterUrl = posterUrl,
        backgroundType = null
    )
}
