package com.bgrem.presentation.common.extensions

import androidx.annotation.DrawableRes
import com.bgrem.app.R
import com.bgrem.presentation.media.capture.model.CameraAction

@DrawableRes
fun CameraAction.getIconRes(): Int = when (this) {
    CameraAction.TAKE_PHOTO -> R.drawable.ic_photo_camera
    CameraAction.TAKE_VIDEO -> R.drawable.ic_video_camera
    CameraAction.STOP -> R.drawable.ic_stop
}