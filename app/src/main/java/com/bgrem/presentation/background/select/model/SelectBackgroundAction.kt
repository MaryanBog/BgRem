package com.bgrem.presentation.background.select.model

import androidx.annotation.StringRes
import com.bgrem.domain.background.model.Background

sealed class SelectBackgroundAction {
    data class Error(@StringRes val messageRes: Int) : SelectBackgroundAction()
    object BackgroundCreated : SelectBackgroundAction()
    object CreatingBackground : SelectBackgroundAction()
    data class BackgroundSelected(val background: Background) : SelectBackgroundAction()
    data class Continue(val backgroundId: String?) : SelectBackgroundAction()
    object ContinueTransparentImage : SelectBackgroundAction()
}
