package com.bgrem.presentation.main.choose

import android.net.Uri
import com.bgrem.domain.common.media.MediaType
import java.io.File

interface ChooseMediaListener {
    fun onMediaSelected(file: File, mediaType: MediaType, mimeType: String, uri: Uri?)
    fun onAboutAppClicked()
}