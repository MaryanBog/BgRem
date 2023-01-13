package com.bgrem.presentation.background.select.model

import com.bgrem.domain.background.model.Background
import com.bgrem.domain.background.model.BackgroundGroup
import com.bgrem.domain.background.model.BackgroundType

sealed class SelectableBackgroundUi {
    companion object {
        fun fromDomain(data: Background) = when (data.group) {
            BackgroundGroup.COLOR -> Color.fromDomain(data)
            BackgroundGroup.IMAGE -> Image.fromDomain(data)
            BackgroundGroup.VIDEO -> Video.fromDomain(data)
            BackgroundGroup.TRANSPARENT -> Transparent.fromDomain(data)
            BackgroundGroup.USER -> User.fromDomain(data)
        }
    }

    abstract val id: String
    abstract val group: BackgroundGroup
    abstract val isSelected: Boolean

    abstract fun copyItem(selected: Boolean): SelectableBackgroundUi

    data class Color(
        override val id: String,
        override val group: BackgroundGroup,
        override val isSelected: Boolean,
        val color: String
    ) : SelectableBackgroundUi() {
        companion object {
            fun fromDomain(data: Background) = Color(
                id = data.id,
                group = data.group,
                color = data.color.orEmpty(),
                isSelected = false
            )
        }

        override fun copyItem(selected: Boolean): SelectableBackgroundUi =
            copy(isSelected = selected)
    }

    data class Image(
        override val id: String,
        override val group: BackgroundGroup,
        override val isSelected: Boolean,
        val previewUrl: String
    ) : SelectableBackgroundUi() {
        companion object {
            fun fromDomain(data: Background) = Image(
                id = data.id,
                group = data.group,
                previewUrl = data.thumbnailUrl.orEmpty(),
                isSelected = false
            )
        }

        override fun copyItem(selected: Boolean): SelectableBackgroundUi =
            copy(isSelected = selected)
    }

    data class Video(
        override val id: String,
        override val group: BackgroundGroup,
        override val isSelected: Boolean,
        val previewUrl: String
    ) : SelectableBackgroundUi() {
        companion object {
            fun fromDomain(data: Background) = Video(
                id = data.id,
                group = data.group,
                previewUrl = data.posterUrl.orEmpty(),
                isSelected = false
            )
        }

        override fun copyItem(selected: Boolean): SelectableBackgroundUi =
            copy(isSelected = selected)
    }

    data class Transparent(
        override val id: String,
        override val group: BackgroundGroup,
        override val isSelected: Boolean
    ) : SelectableBackgroundUi() {
        companion object {
            fun fromDomain(data: Background) = Transparent(
                id = data.id,
                group = data.group,
                isSelected = false
            )
        }

        override fun copyItem(selected: Boolean): SelectableBackgroundUi =
            copy(isSelected = selected)
    }

    data class User(
        override val id: String,
        override val group: BackgroundGroup,
        override val isSelected: Boolean,
        val previewUrl: String,
        val backgroundType: BackgroundType?
    ) : SelectableBackgroundUi() {
        companion object {
            fun fromDomain(data: Background) = User(
                id = data.id,
                group = data.group,
                previewUrl = data.posterUrl ?: data.thumbnailUrl.orEmpty(),
                isSelected = false,
                backgroundType = data.backgroundType
            )
        }

        override fun copyItem(selected: Boolean): SelectableBackgroundUi =
            copy(isSelected = selected)
    }

    object AddNewBg : SelectableBackgroundUi() {
        override val id: String = ""
        override val group: BackgroundGroup = BackgroundGroup.USER
        override val isSelected: Boolean = false

        override fun copyItem(selected: Boolean): SelectableBackgroundUi = this
    }
}