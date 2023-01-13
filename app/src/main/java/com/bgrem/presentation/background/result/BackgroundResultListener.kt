package com.bgrem.presentation.background.result

import com.bgrem.domain.common.media.MediaType

interface BackgroundResultListener {
    fun onResultBackCLicked()
    fun onAddNewFileClicked()
    fun onFullScreenClicked(uri: String, mediaType: MediaType)
}