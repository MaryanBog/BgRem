package com.bgrem.app.di

import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.task.model.Task
import com.bgrem.presentation.background.ChangeBackgroundViewModel
import com.bgrem.presentation.background.removing.RemoveBackgroundViewModel
import com.bgrem.presentation.background.result.BackgroundResultViewModel
import com.bgrem.presentation.background.select.SelectBackgroundViewModel
import com.bgrem.presentation.main.MainViewModel
import com.bgrem.presentation.main.choose.ChooseMediaViewModel
import com.bgrem.presentation.main.model.SelectedMediaInfo
import com.bgrem.presentation.main.sending.SendingMediaViewModel
import com.bgrem.presentation.media.capture.CaptureViewModel
import com.bgrem.presentation.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val captureMediaModule = module {
    viewModel { (mediaType: MediaType) ->
        CaptureViewModel(
            mediaType = mediaType,
            fileStorage = get(),
            mimeTypeManager = get()
        )
    }
}

private val mainModule = module {
    viewModel {
        ChooseMediaViewModel(
            fileStorage = get(),
            mimeTypeManager = get()
        )
    }
    viewModel { (info: SelectedMediaInfo) ->
        SendingMediaViewModel(
            selectedMediaInfo = info,
            createJobAndRemoveBackgroundUseCase = get()
        )
    }
    viewModel { (selectOtherMedia: Boolean) ->
        MainViewModel(
            selectOtherMedia = selectOtherMedia,
            getIsFirstLaunchUseCase = get(),
            clearJobUseCase = get(),
            contentResolver = get()
        )
    }
    viewModel {
        WelcomeViewModel(
            setFirstLaunchedUseCase = get()
        )
    }
}

private val backgroundModule = module {
    viewModel { (taskId: String, contentMediaType: MediaType) ->
        ChangeBackgroundViewModel(
            taskId = taskId,
            getTaskUseCase = get(),
            contentMediaType = contentMediaType
        )
    }
    viewModel { (mediaType: MediaType) ->
        SelectBackgroundViewModel(
            contentMediaType = mediaType,
            observeBackgroundsUseCase = get(),
            mimeTypeManager = get(),
            fileStorage = get(),
            createBackgroundUseCase = get(),
            getIsPortraitMediaUseCase = get()
        )
    }
    viewModel { (backgroundId: String?, mediaType: MediaType) ->
        RemoveBackgroundViewModel(
            backgroundId = backgroundId,
            mediaType = mediaType,
            removeBackgroundInteractor = get()
        )
    }
    viewModel { (task: Task, mediaType: MediaType) ->
        BackgroundResultViewModel(
            task = task,
            mediaType = mediaType,
            downloadResultFileUseCase = get(),
            mimeTypeManager = get(),
            contentResolver = get(),
            getIsPortraitMediaUseCase = get()
        )
    }
}

val presentationModule = module {
    loadKoinModules(listOf(mainModule, backgroundModule, captureMediaModule))
}