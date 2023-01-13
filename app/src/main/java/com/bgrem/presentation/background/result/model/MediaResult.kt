package com.bgrem.presentation.background.result.model

import android.os.Parcelable
import com.bgrem.domain.common.media.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaResult(
    val mediaType: MediaType,
    val url: String?
) : Parcelable