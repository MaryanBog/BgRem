package com.bgrem.data.net.background.model

import com.bgrem.domain.background.model.BackgroundGroup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BackgroundGroupDto {
    @SerialName("color")
    COLOR,

    @SerialName("image")
    IMAGE,

    @SerialName("video")
    VIDEO,

    @SerialName("transparent")
    TRANSPARENT,

    @SerialName("user")
    USER;

    fun toDomain() = when (this) {
        COLOR -> BackgroundGroup.COLOR
        IMAGE -> BackgroundGroup.IMAGE
        VIDEO -> BackgroundGroup.VIDEO
        TRANSPARENT -> BackgroundGroup.TRANSPARENT
        USER -> BackgroundGroup.USER
    }
}