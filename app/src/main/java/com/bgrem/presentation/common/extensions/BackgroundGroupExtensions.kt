package com.bgrem.presentation.common.extensions

import androidx.annotation.StringRes
import com.bgrem.app.R
import com.bgrem.domain.background.model.BackgroundGroup

@StringRes
fun BackgroundGroup.getTitleRes(): Int = when (this) {
    BackgroundGroup.COLOR -> R.string.select_background_color
    BackgroundGroup.USER -> R.string.select_background_your_bg
    BackgroundGroup.IMAGE -> R.string.select_background_image
    BackgroundGroup.VIDEO -> R.string.select_background_video
    BackgroundGroup.TRANSPARENT -> R.string.select_background_transparent
}

fun BackgroundGroup.getSortedIndex(): Int = when (this) {
    BackgroundGroup.TRANSPARENT -> 0
    BackgroundGroup.USER -> 1
    BackgroundGroup.COLOR -> 2
    BackgroundGroup.IMAGE -> 3
    BackgroundGroup.VIDEO -> 4
}