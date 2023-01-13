package com.bgrem.presentation.main.model

sealed class MainAction {
    object ShowSplash : MainAction()
    object ShowChooseMedia : MainAction()
    object ShowWelcome: MainAction()
    data class SendMedia(val info: SelectedMediaInfo) : MainAction()
}
