package com.bgrem.presentation.main.model

import android.os.Parcelable
import com.bgrem.domain.common.media.MediaType
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class SelectedMediaInfo(
    val file: File,
    val mediaType: MediaType,
    val mimeType: String
) : Parcelable
