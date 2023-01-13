package com.bgrem.presentation.common.extensions

import androidx.annotation.StringRes
import com.bgrem.app.R
import com.bgrem.domain.background.model.BackgroundGroup
import com.bgrem.domain.common.media.MediaType

@StringRes
fun MediaType.getSendingTextRes(): Int = when (this) {
    MediaType.VIDEO -> R.string.sending_media_video
    MediaType.IMAGE -> R.string.sending_media_image
}

fun MediaType.getAvailableBackgroundGroups(): List<BackgroundGroup> = when (this) {
    MediaType.IMAGE -> BackgroundGroup.values().toList().minus(BackgroundGroup.VIDEO)
    MediaType.VIDEO -> BackgroundGroup.values().toList()
}

@StringRes
fun MediaType.getResultTypeRes(): Int = when (this) {
    MediaType.VIDEO -> R.string.result_video
    MediaType.IMAGE -> R.string.result_image
}

@StringRes
fun MediaType.getDownloadResultErrorMessageRes(): Int = when (this) {
    MediaType.IMAGE -> R.string.common_error_download_image_error
    MediaType.VIDEO -> R.string.common_error_download_video_error
}

@StringRes
fun MediaType.getSaveResultErrorMessageRes(): Int = when (this) {
    MediaType.IMAGE -> R.string.common_error_save_image_error
    MediaType.VIDEO -> R.string.common_error_save_video_error
}

@StringRes
fun MediaType.getSaveResultSuccessMessageRes(): Int = when (this) {
    MediaType.IMAGE -> R.string.result_save_image_success
    MediaType.VIDEO -> R.string.result_save_video_success
}