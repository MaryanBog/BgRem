package com.bgrem.presentation.trimming.thumbnail

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrimInfo(
    val uri: Uri?
): Parcelable
