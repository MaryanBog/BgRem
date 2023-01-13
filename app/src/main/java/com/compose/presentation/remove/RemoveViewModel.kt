package com.compose.presentation.remove

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.common.extensions.asLiveData
import com.compose.data.FileStorages
import com.compose.data.RepositoryBgRem
import com.compose.data.models.MediaInfo
import com.compose.data.models.RemoveBackgroundProgressStateCompose
import com.compose.models.*
import com.compose.presentation.gallery.image.ImageAction
import com.compose.presentation.gallery.video.VideoAction
import com.compose.utils.BitmapsUtils
import com.compose.utils.Constant
import com.compose.utils.MimeTypesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class RemoveViewModel @Inject constructor(
    private val fileStorages: FileStorages,
    private val mimeTypes: MimeTypesUtils,
    private val contentResolver: ContentResolver,
    private val repositoryBgRem: RepositoryBgRem
) : ViewModel() {

    val imageMimeTypes: List<String> get() = mimeTypes.getAvailableImageTypes()
    val videoMimeTypes: List<String> get() = mimeTypes.getAvailableVideoTypes()

    private val _selectMediaInfo = MutableStateFlow(MediaInfo())
    val selectMediaInfo: StateFlow<MediaInfo> = _selectMediaInfo.asStateFlow()

    private val _previewImage = MutableStateFlow<Uri?>(null)
    val previewImage = _previewImage.asStateFlow()

    private val _imageActionState = MutableStateFlow<ImageAction>(ImageAction.StartImage)
    val imageActionState = _imageActionState.asStateFlow()

    private val _visibilityPhotoVideoButton = MutableStateFlow(true)
    val visibilityPhotoVideoButton = _visibilityPhotoVideoButton.asStateFlow()

    private val _enableNavButton = MutableStateFlow(false)
    val enableNavButton = _enableNavButton.asStateFlow()

    private val _videoActionState = MutableStateFlow<VideoAction>(VideoAction.StartVideo)
    val videoActionState = _videoActionState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _dataState = MutableLiveData<StateViewModel>()
    val dataState: LiveData<StateViewModel>
        get() = _dataState

    private val emptyTask = Task(
        id = "",
        outputType = "",
        price = 0,
        duration = 0,
        sourceUrl = "",
        maskPosX = 0,
        maskFlip = "",
        maskPosY = 0,
        maskScaleFactor = 0.0,
        plan = "",
        progress = 0,
        unpaidSeconds = 0,
        bgRotationAngle = 0,
        bgUrl = "",
        bg_group = "",
        checkoutUrl = "",
        error = "",
        paymentStatus = "",
        preview = null,
        resultUrl = "",
        status = "",
        thumbnail_url = "",
        download_url = ""
    )

    private val emptyMedia = MediaInfo(null, null, null)

    private val emptyJob = Job(
        id = "",
        sourceUrl = "",
        duration = 0,
        size = 0,
        isPortrait = false,
        thumbnailUrl = "",
        charged_seconds = 0,
        price = 0,
        videoWidth = 0,
        videoHeight = 0,
        media_type = "",
        status = ""
    )

    fun onVisiblePhotoVideoButton(visible: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _visibilityPhotoVideoButton.emit(visible)
        }
    }

    fun onEnableNavButton(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _enableNavButton.emit(enable)
        }
    }

    fun onImagePush(uri: Uri?) {
        try {
            val photoFile = uri?.toFile() ?: return
            viewModelScope.launch {
                val bytes: ByteArray = photoFile.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size).let {
                    if (it == null) return@launch
                    BitmapsUtils.rotateImageIfNeeded(it, photoFile)
                }
                val mimeType = mimeTypes.getMimeTypeForNewImage()
                val imageFile = fileStorages.createTempCacheFile(
                    suffix = mimeTypes.getFileExtensionImageType(mimeType)
                ).also { file ->
                    file.outputStream().use {
                        bitmap.compress(
                            BitmapsUtils.mimeTypeCompressFormat(mimeType),
                            Constant.BITMAP_COMPRESS_QUALITY,
                            it
                        )
                    }
                }
                _imageActionState.value = ImageAction.ImageSelected(
                    imageFile, MediaType.IMAGE, mimeType
                )
            }
        } catch (_: Exception) {
            _imageActionState.value = ImageAction.Error
        }
    }

    fun onPreviewPhoto(uri: Uri?) {
        viewModelScope.launch {
            _previewImage.emit(uri)
            _imageActionState.value = ImageAction.StartImage
        }
    }

    fun onTakeVideo(uri: Uri?) {
        if (uri != null) {
            viewModelScope.launch {
                _videoActionState.value = VideoAction.TruncateVideo(uri)
            }
        } else return
    }

    fun onImageSelected(
        inputStream: InputStream?,
        mimeType: String?
    ) {
        if (inputStream == null || mimeType.isNullOrBlank()) {
            return
        }

        try {
            viewModelScope.launch {
                val bytes: ByteArray = inputStream.use { it.readBytes() }
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val imageFile = fileStorages.createTempCacheFile(
                    suffix = mimeTypes.getFileExtensionImageType(mimeType)
                ).also { file ->
                    file.outputStream().use {
                        bitmap.compress(
                            BitmapsUtils.mimeTypeCompressFormat(mimeType),
                            Constant.BITMAP_COMPRESS_QUALITY,
                            it
                        )
                    }
                }
                _imageActionState.value = ImageAction.ImageSelected(
                    imageFile, MediaType.IMAGE, mimeType
                )
            }
        } catch (_: Exception) {
            _imageActionState.value = ImageAction.Error
        }
    }

    fun onVideoSelected(
        inputStream: InputStream?,
        mimeType: String?,
        uri: Uri?
    ) {
        if (inputStream == null || mimeType.isNullOrBlank()) {
            return
        }

        try {
            viewModelScope.launch {
                val file = fileStorages.createTempCacheFile(
                    suffix = mimeTypes.getFileExtensionVideoType(mimeType)
                )
                file.outputStream().use {
                    inputStream.copyTo(it)
                }
                _videoActionState.value = VideoAction.VideoSelected(
                    file, MediaType.VIDEO, mimeType, uri
                )
            }
        } catch (_: Exception) {
            _videoActionState.value = VideoAction.Error
        }
    }

    fun onMediaSelected(file: File, mediaType: MediaType, mimeType: String, uri: Uri?) {
        viewModelScope.launch {
            _selectMediaInfo.update { currentState ->
                currentState.copy(
                    file = file, mediaType = mediaType, mimeType = mimeType
                )
            }
            _imageActionState.value = ImageAction.StartImage
            _videoActionState.value = VideoAction.StartVideo
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (uri != null) {
                delay(3000L)
                contentResolver.delete(uri, null, null)
            }
        }
    }

    fun onErrorShow(stringRes: String?) {
        viewModelScope.launch {
            if (stringRes == null) {
                _errorMessage.emit(null)
            } else {
                _errorMessage.emit(stringRes)
            }
        }
    }

    val job = repositoryBgRem.job

    val task = repositoryBgRem.taskDone

    fun removeBg() {
        viewModelScope.launch {
            _selectMediaInfo.value.mediaType?.let {
                repositoryBgRem.removeBackground(
                    backgroundId = null,
                    it,
                    _selectMediaInfo.value.file,
                    _selectMediaInfo.value.mimeType
                )
            }
        }
    }

    private val _state = MutableLiveData<Float>()
    val state = _state.asLiveData()

    private val _event = MutableLiveData<EventCompose<RemoveBackgroundActionCompose>>()
    val event = _event.asLiveData()

    val serverError = repositoryBgRem.errorsServer

    fun observeRemovingBackgroundProgress() {
        viewModelScope.launch {
            repositoryBgRem.createTaskProgress.collect { result ->
                result
                    .onSuccess { removeState ->
                        when (removeState) {
                            is RemoveBackgroundProgressStateCompose.Progress -> _state.value =
                                removeState.value

                            is RemoveBackgroundProgressStateCompose.Finished -> _event.postEvent(
                                RemoveBackgroundActionCompose.RemovingFinished(removeState.task)
                            )

                        }
                    }
                    .onFailure {
                        _dataState.value = StateViewModel(exception = true)
                    }
            }
        }
    }

    fun getNewBackground(backgroundId: String) {
        try {
            viewModelScope.launch {
                _selectMediaInfo.value.mediaType?.let {
                    repositoryBgRem.getNewBackground(
                        it,
                        backgroundId
                    )
                }
            }
        } catch (_: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }

    }

    var imageMediaInfo = MutableLiveData<MediaInfo>()

    fun getMyBgImageInfo(mimeType: String?, inputStream: InputStream?) {
        if (inputStream == null || mimeType.isNullOrBlank()) {
            return
        }
        try {
            viewModelScope.launch {
                val bytes: ByteArray = inputStream.use { it.readBytes() }
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val imageFile = fileStorages.createTempCacheFile(
                    suffix = mimeTypes.getFileExtensionImageType(mimeType)
                ).also { file ->
                    file.outputStream().use {
                        bitmap.compress(
                            BitmapsUtils.mimeTypeCompressFormat(mimeType),
                            Constant.BITMAP_COMPRESS_QUALITY,
                            it
                        )
                    }
                }
                val myBgInfo =
                    MediaInfo(file = imageFile, mimeType = mimeType, mediaType = MediaType.IMAGE)
                imageMediaInfo.value = myBgInfo
            }
        } catch (_: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }
    }

    fun getNewYourBackgroundImage() {
        try {
            viewModelScope.launch {
                repositoryBgRem.getNewMyBackground(
                    mediaType = MediaType.IMAGE,
                    mimeType = imageMediaInfo.value?.mimeType,
                    file = imageMediaInfo.value?.file
                )
            }
        } catch (_: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }

    }

    fun getMyBgVideoInfo(mimeType: String?, inputStream: InputStream?) {
        if (inputStream == null || mimeType.isNullOrBlank()) {
            return
        }
        try {
            viewModelScope.launch {
                val videoFile = fileStorages.createTempCacheFile(
                    suffix = mimeTypes.getFileExtensionVideoType(mimeType)
                )
                videoFile.outputStream().use {
                    inputStream.copyTo(it)
                }
                val myBgInfo =
                    MediaInfo(file = videoFile, mimeType = mimeType, mediaType = MediaType.VIDEO)
                imageMediaInfo.value = myBgInfo
            }
        } catch (_: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }
    }

    fun getNewYourBackgroundVideo() {
        try {
            viewModelScope.launch {
                repositoryBgRem.getNewMyBackground(
                    mediaType = MediaType.VIDEO,
                    mimeType = imageMediaInfo.value?.mimeType,
                    file = imageMediaInfo.value?.file
                )
            }
        } catch (_: Exception) {
            _dataState.value = StateViewModel(exception = true)
        }
    }

    fun cleanMediaInfo() {
        imageMediaInfo.value = emptyMedia
    }

    private fun <T> MutableLiveData<EventCompose<T>>.postEvent(value: T?) {
        value?.let { postValue(EventCompose(value)) }
    }

    fun clearTaskMedia() {
        repositoryBgRem.taskDone.value = emptyTask
        repositoryBgRem.job.value = emptyJob
        task.value = emptyTask
        job.value = emptyJob
        _selectMediaInfo.value = emptyMedia
    }
}
