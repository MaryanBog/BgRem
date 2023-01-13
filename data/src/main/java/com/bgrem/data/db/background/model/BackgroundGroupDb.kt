package com.bgrem.data.db.background.model

import com.bgrem.domain.background.model.BackgroundGroup

enum class BackgroundGroupDb {
    COLOR, IMAGE, VIDEO, TRANSPARENT, USER;

    companion object {
        fun fromDomain(data: BackgroundGroup) = when (data) {
            BackgroundGroup.COLOR -> COLOR
            BackgroundGroup.IMAGE -> IMAGE
            BackgroundGroup.VIDEO -> VIDEO
            BackgroundGroup.TRANSPARENT -> TRANSPARENT
            BackgroundGroup.USER -> USER
        }
    }

    fun toDomain() = when (this) {
        COLOR -> BackgroundGroup.COLOR
        IMAGE -> BackgroundGroup.IMAGE
        VIDEO -> BackgroundGroup.VIDEO
        TRANSPARENT -> BackgroundGroup.TRANSPARENT
        USER -> BackgroundGroup.USER
    }
}