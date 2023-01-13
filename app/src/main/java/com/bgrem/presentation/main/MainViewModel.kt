package com.bgrem.presentation.main

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.job.ClearJobUseCase
import com.bgrem.domain.launch.GetIsFirstLaunchUseCase
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.extensions.postEvent
import com.bgrem.presentation.main.model.MainAction
import com.bgrem.presentation.main.model.SelectedMediaInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val selectOtherMedia: Boolean,
    private val getIsFirstLaunchUseCase: GetIsFirstLaunchUseCase,
    private val clearJobUseCase: ClearJobUseCase,
    private val contentResolver: ContentResolver
) : StateViewModel<Unit, MainAction>() {

    init {
        clearJob()
        handleInitScreen()
    }

    fun onSplashAnimationEnded() {
        _event.postEvent(MainAction.ShowChooseMedia)
    }

    fun onGetStartedClicked() {
        _event.postEvent(MainAction.ShowSplash)
    }

    fun onMediaSelected(file: File, mediaType: MediaType, mimeType: String, uri: Uri?) {
        _event.postEvent(
            MainAction.SendMedia(
                info = SelectedMediaInfo(
                    file = file,
                    mediaType = mediaType,
                    mimeType = mimeType
                )
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            if (uri != null) {
                delay(DELAY_DELETE)
                contentResolver.delete(uri, null, null)
            }
        }
    }

    private fun handleInitScreen() {
        viewModelScope.launch {
            val isFirstLaunch = getIsFirstLaunchUseCase()
            _event.postEvent(
                when {
                    isFirstLaunch -> MainAction.ShowWelcome
                    selectOtherMedia -> MainAction.ShowChooseMedia
                    else -> MainAction.ShowSplash
                }
            )
        }
    }

    private fun clearJob() {
        viewModelScope.launch {
            clearJobUseCase()
        }
    }

    companion object{
        private const val DELAY_DELETE = 3000L
    }
}