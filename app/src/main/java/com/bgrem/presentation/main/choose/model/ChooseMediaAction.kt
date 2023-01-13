package com.bgrem.presentation.main.choose.model

import android.net.Uri
import androidx.annotation.StringRes
import com.bgrem.domain.common.media.MediaType
import java.io.File

sealed class ChooseMediaAction {
    object SelectVideo : ChooseMediaAction()
    object SelectImage : ChooseMediaAction()
    object ClearSelectionMediaType : ChooseMediaAction()
    data class Error(@StringRes val errorMessageRes: Int) : ChooseMediaAction()
    data class ThumbnailSelected(val uri: Uri): ChooseMediaAction()
    data class MediaSelected(
        val file: File,
        val mediaType: MediaType,
        val mimeType: String,
        val uri: Uri?
    ) : ChooseMediaAction()
}
