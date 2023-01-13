package com.bgrem.presentation.trimming.thumbnail

import androidx.annotation.StringRes

sealed class ThumbnailAction {
    data class Error(@StringRes val errorMessageRes: Int) : ThumbnailAction()
}