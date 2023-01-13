package com.bgrem.data.db.background.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bgrem.domain.background.model.Background

@Entity(tableName = "backgrounds")
data class BackgroundEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val group: BackgroundGroupDb,
    val color: String?,
    val fileUrl: String?,
    val thumbnailUrl: String?,
    val posterUrl: String?,
    val backgroundType: BackgroundTypeDb?
) {

    companion object {
        fun fromDomain(data: Background) = BackgroundEntity(
            id = data.id,
            group = BackgroundGroupDb.fromDomain(data.group),
            color = data.color,
            fileUrl = data.fileUrl,
            thumbnailUrl = data.thumbnailUrl,
            posterUrl = data.posterUrl,
            backgroundType = data.backgroundType?.let { BackgroundTypeDb.fromDomain(it) }
        )
    }

    fun toDomain() = Background(
        id = id,
        group = group.toDomain(),
        color = color,
        fileUrl = fileUrl,
        thumbnailUrl = thumbnailUrl,
        posterUrl = posterUrl,
        backgroundType = backgroundType?.toDomain()
    )
}
