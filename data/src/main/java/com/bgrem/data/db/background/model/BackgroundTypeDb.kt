package com.bgrem.data.db.background.model

import com.bgrem.domain.background.model.BackgroundType

enum class BackgroundTypeDb {
    IMAGE, VIDEO;

    companion object {
        fun fromDomain(data: BackgroundType) = when (data) {
            BackgroundType.IMAGE -> IMAGE
            BackgroundType.VIDEO -> VIDEO
        }
    }

    fun toDomain() = when (this) {
        IMAGE -> BackgroundType.IMAGE
        VIDEO -> BackgroundType.VIDEO
    }
}