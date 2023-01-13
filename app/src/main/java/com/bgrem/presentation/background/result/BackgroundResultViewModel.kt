package com.bgrem.presentation.background.result

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.background.model.BackgroundGroup
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.domain.job.GetIsPortraitMediaUseCase
import com.bgrem.domain.task.DownloadResultFileUseCase
import com.bgrem.domain.task.model.DownloadResultFileParams
import com.bgrem.domain.task.model.Task
import com.bgrem.presentation.background.result.model.BackgroundResultAction
import com.bgrem.presentation.background.result.model.HandleResultType
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.extensions.asLiveData
import com.bgrem.presentation.common.extensions.getDownloadResultErrorMessageRes
import com.bgrem.presentation.common.extensions.getSaveResultErrorMessageRes
import com.bgrem.presentation.common.extensions.postEvent
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okio.IOException
import timber.log.Timber
import java.io.File

class BackgroundResultViewModel(
    val mediaType: MediaType,
    private val task: Task,
    private val downloadResultFileUseCase: DownloadResultFileUseCase,
    private val mimeTypeManager: MimeTypeManager,
    private val contentResolver: ContentResolver,
    private val getIsPortraitMediaUseCase: GetIsPortraitMediaUseCase
) : StateViewModel<Unit, BackgroundResultAction>() {

    var saveType = MutableLiveData<Int>()

    private var currentMediaFile: File? = null

    private val _isPortraitMedia = MutableLiveData<Boolean>()
    val isPortraitMedia = _isPortraitMedia.asLiveData()

    val contentUrl: String get() = task.resultUrl.orEmpty()
    var handleResultType = HandleResultType.SAVE
        private set

    init {
        getIsPortraitMedia()
//        downloadResultFile()
    }

    fun onShareResultClick() {
        handleResultType = HandleResultType.SHARE
        downloadResultFile()
    }

    fun onSaveResultClick() {
        handleResultType = HandleResultType.SAVE
        downloadResultFile()
    }

    fun getMimeTypeForResultFile() = when (mediaType) {
        MediaType.IMAGE -> mimeTypeManager.getResultTransparentImageMimeType()
        MediaType.VIDEO -> mimeTypeManager.getResultVideoMimeType()
    }

    fun saveMedia() {
        val file = currentMediaFile ?: return
        val values = getMediaContentValues(file.nameWithoutExtension)
        var uri: Uri? = null

        try {
            uri = getExternalFileUri(values)
            if (uri == null) {
                _event.postEvent(BackgroundResultAction.Error(mediaType.getSaveResultErrorMessageRes()))
                return
            }

            val input = file.inputStream()
            val output = contentResolver.openOutputStream(uri)
            if (output == null) {
                _event.postEvent(BackgroundResultAction.Error(mediaType.getSaveResultErrorMessageRes()))
                return
            }

            output.use {
                it.write(input.readBytes())
            }

            _event.postEvent(BackgroundResultAction.SaveFileSucceeded)
        } catch (e: IOException) {
            Timber.e(e)
            uri?.let { contentResolver.delete(it, null, null) }
            _event.postEvent(BackgroundResultAction.Error(mediaType.getSaveResultErrorMessageRes()))
        }
    }

    private fun getIsPortraitMedia() {
        viewModelScope.launch {
            _isPortraitMedia.value = getIsPortraitMediaUseCase()
        }
    }

    private fun downloadResultFile() {
        viewModelScope.launch {
            val params = DownloadResultFileParams(
                taskId = task.id,
                mediaType = mediaType,
                isTransparent = task.bgGroup == BackgroundGroup.TRANSPARENT
            )
            downloadResultFileUseCase(params)
                .onStart {
                    _event.postEvent(BackgroundResultAction.DownloadFile)
                }
                .collect { result ->
                    result
                        .onSuccess {
                            currentMediaFile = it
                            _event.postEvent(BackgroundResultAction.DownloadSuccessful(it))
                        }
                        .onFailure {
                            _event.postEvent(BackgroundResultAction.Error(mediaType.getDownloadResultErrorMessageRes()))
                        }
                }
        }
    }

    private fun getExternalFileUri(values: ContentValues): Uri? {
        val externalContentUri = when (mediaType) {
            MediaType.IMAGE -> {
                saveType.value = 1
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            MediaType.VIDEO -> {
                saveType.value = 0
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
        }
        return contentResolver.insert(externalContentUri, values)
    }

    private fun getMediaContentValues(filename: String): ContentValues = when (mediaType) {
        MediaType.IMAGE -> getImageContentValues(filename)
        MediaType.VIDEO -> getVideoContentValues(filename)
    }

    private fun getImageContentValues(filename: String) = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, mimeTypeManager.getResultTransparentImageMimeType())
    }

    private fun getVideoContentValues(filename: String) = ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, filename)
        put(MediaStore.Video.Media.MIME_TYPE, mimeTypeManager.getResultVideoMimeType())
    }
}