package com.bgrem.presentation.background.select

import android.media.ThumbnailUtils
import android.provider.MediaStore
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bgrem.app.R
import com.bgrem.domain.background.CreateBackgroundUseCase
import com.bgrem.domain.background.ObserveBackgroundsUseCase
import com.bgrem.domain.background.model.Background
import com.bgrem.domain.background.model.BackgroundGroup
import com.bgrem.domain.background.model.BackgroundType
import com.bgrem.domain.background.model.CreateBackgroundParams
import com.bgrem.domain.common.failure.UnsupportedMimeType
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.domain.job.GetIsPortraitMediaUseCase
import com.bgrem.presentation.background.select.model.SelectBackgroundAction
import com.bgrem.presentation.background.select.model.SelectBackgroundState
import com.bgrem.presentation.background.select.model.SelectableBackgroundUi
import com.bgrem.presentation.common.AppConstants
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.VideoDurationResult
import com.bgrem.presentation.common.extensions.asLiveData
import com.bgrem.presentation.common.extensions.getAvailableBackgroundGroups
import com.bgrem.presentation.common.extensions.getSortedIndex
import com.bgrem.presentation.common.extensions.postEvent
import com.bgrem.presentation.common.utils.BitmapUtils
import com.bgrem.presentation.common.utils.VideoUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream

class SelectBackgroundViewModel(
    val mimeTypeManager: MimeTypeManager,
    val contentMediaType: MediaType,
    private val observeBackgroundsUseCase: ObserveBackgroundsUseCase,
    private val fileStorage: FileStorage,
    private val createBackgroundUseCase: CreateBackgroundUseCase,
    private val getIsPortraitMediaUseCase: GetIsPortraitMediaUseCase
) : StateViewModel<SelectBackgroundState, SelectBackgroundAction>(SelectBackgroundState()) {

    var backgroundType = MutableLiveData<String>()

    var backgroundId = MutableLiveData<String>()

    var backgroundGroup = MutableLiveData<BackgroundGroup>()

    private companion object {
        const val SELECT_NEW_BG_DELAY = 500L
    }

    private val _backgrounds =
        MutableLiveData<List<Pair<BackgroundGroup, List<SelectableBackgroundUi>>>>()
    val backgrounds = _backgrounds.asLiveData()

    private val _selectedGroupPosition = MutableLiveData<Int>()
    val selectedGroupPosition = _selectedGroupPosition.asLiveData()

    private val _isPortraitMedia = MutableLiveData<Boolean>()
    val isPortraitMedia = _isPortraitMedia.asLiveData()

    private val allBackgrounds = mutableListOf<Background>()

    private var selectedGroup = BackgroundGroup.TRANSPARENT
    private var selectedBackgroundId: String? = null

    var selectedBackgroundMediaType = MediaType.IMAGE
        private set

    val availableBackgroundGroups = contentMediaType.getAvailableBackgroundGroups()
        .sortedBy { it.getSortedIndex() }


    fun observeBackgrounds() {
        viewModelScope.launch {
            observeBackgroundsUseCase()
                .onStart { _state.value = _state.value?.copy(isLoading = true, error = null) }
                .collect { result ->
                    result
                        .onSuccess { data ->
                            allBackgrounds.clear()
                            allBackgrounds.addAll(data)

                            _backgrounds.value = availableBackgroundGroups.map { group ->
                                val backgrounds = data.filter { it.group == group }
                                    .mapNotNull {
                                        val isVideoBackground =
                                            it.backgroundType == BackgroundType.VIDEO
                                        when (contentMediaType) {
                                            MediaType.IMAGE -> if (isVideoBackground) {
                                                null
                                            } else {
                                                SelectableBackgroundUi.fromDomain(it)
                                            }
                                            MediaType.VIDEO -> SelectableBackgroundUi.fromDomain(it)
                                        }
                                    }
                                    .toMutableList()

                                if (group == BackgroundGroup.USER) {
                                    backgrounds.add(0, SelectableBackgroundUi.AddNewBg)
                                }

                                group to backgrounds
                            }.sortedBy { it.first.getSortedIndex() }

                            if (selectedBackgroundId.isNullOrEmpty()) {
                                data.find { it.group == selectedGroup }?.id?.let {
                                    onBackgroundSelected(it, selectedGroup)
                                }
                            }

                            _state.value = _state.value?.copy(isLoading = false)
                        }
                        .onFailure {
                            _state.value = _state.value?.copy(error = it, isLoading = false)
                        }
                }
        }
    }

    fun onBackgroundSelected(id: String, group: BackgroundGroup) {
        if (selectedBackgroundId == id) return
        _backgrounds.value = _backgrounds.value?.map { pair ->
            pair.copy(second = pair.second.map { it.copyItem(selected = it.id == id) })
        }
        selectedBackgroundId = id
        val background = allBackgrounds.find { it.id == id } ?: return
        _event.postEvent(SelectBackgroundAction.BackgroundSelected(background))
        val isVideoBg = group == BackgroundGroup.VIDEO
                || (group == BackgroundGroup.USER && !background.posterUrl.isNullOrEmpty())
        _state.value = _state.value?.copy(isPreviewPlayerVisible = isVideoBg)
    }

    fun onGroupSelected(position: Int) {
        val group = availableBackgroundGroups.getOrNull(position) ?: return
        if (group == selectedGroup) return
        selectedGroup = group
        _selectedGroupPosition.value = position
        if (group == BackgroundGroup.TRANSPARENT) onTransparentSelected()
    }

    fun onSelectBackgroundMediaType(mediaType: MediaType) {
        selectedBackgroundMediaType = mediaType
    }

    fun onMediaSelected(
        inputStream: InputStream?,
        mimeType: String?,
        fileDescriptor: FileDescriptor?,
        exifOrientation: Int?
    ) {
        if (inputStream == null || mimeType.isNullOrEmpty()) {
            postErrorEvent(R.string.common_error_process_file)
            return
        }

        when (selectedBackgroundMediaType) {
            MediaType.IMAGE -> onImageSelected(inputStream, exifOrientation, mimeType)
            MediaType.VIDEO -> onVideoSelected(inputStream, mimeType, fileDescriptor)
        }
    }

    fun onContinue() {
        val isTransparent =
            selectedBackgroundId == allBackgrounds.find { it.group == BackgroundGroup.TRANSPARENT }?.id
        _event.postEvent(
            if (isTransparent && contentMediaType == MediaType.IMAGE) {
                backgroundType.value = "transparent"
                SelectBackgroundAction.ContinueTransparentImage
            } else {
                backgroundId.value = selectedBackgroundId
                val id = selectedBackgroundId
                val background = allBackgrounds.find { it.id == id }
                val group = background?.group
                backgroundGroup.value = group
                SelectBackgroundAction.Continue(backgroundId = selectedBackgroundId)
            }
        )
    }

    private fun onTransparentSelected() {
        val transparentItem =
            allBackgrounds.find { it.group == BackgroundGroup.TRANSPARENT } ?: return
        onBackgroundSelected(transparentItem.id, transparentItem.group)
    }

    private fun onImageSelected(inputStream: InputStream, exifOrientation: Int?, mimeType: String) {
        if (exifOrientation == null) {
            postErrorEvent(R.string.common_error_process_file)
            return
        }

        viewModelScope.launch {
            val bitmap = BitmapUtils.resizeBitmap(
                bitmap = BitmapUtils.bytesToBitmap(inputStream.readBytes()),
                largeSide = AppConstants.IMAGE_MAX_LARGE_SIDE,
                smallerSide = AppConstants.IMAGE_MAX_SMALLER_SIDE
            ).let {
                BitmapUtils.rotateImageIfNeeded(bitmap = it, exifOrientation = exifOrientation)
            }
            val imageFile = fileStorage.createTempCacheFile(
                suffix = getFileExtensionByMimeTypeSafe(mimeType) ?: return@launch
            ).also { file ->
                file.outputStream().use {
                    bitmap.compress(
                        BitmapUtils.mimeTypeToBitmapCompressFormat(mimeType),
                        AppConstants.BITMAP_COMPRESS_QUALITY,
                        it
                    )
                }
            }

            sendBackground(file = imageFile, mimeType = mimeType, mediaType = MediaType.IMAGE)
        }
    }

    private fun onVideoSelected(
        inputStream: InputStream,
        mimeType: String,
        fileDescriptor: FileDescriptor?
    ) {
        if (fileDescriptor == null) {
            postErrorEvent(R.string.common_error_process_file)
            return
        }
        when (isVideoValidByDuration(fileDescriptor)) {
            VideoDurationResult.TOO_LONG -> null
            VideoDurationResult.ERROR -> R.string.common_error_process_file
            VideoDurationResult.FIT -> null
        }?.let {
            postErrorEvent(it)
            return
        }

        viewModelScope.launch {
            val file = fileStorage.createTempCacheFile(
                suffix = getFileExtensionByMimeTypeSafe(mimeType) ?: return@launch
            )
            val bytes = try {
                inputStream.use { it.readBytes() }  /** Bag IOStreams.kt line 135*/
            } catch (e: Exception){
                return@launch
            }

            file.outputStream().use { it.write(bytes) }
            val thumbnail = ThumbnailUtils.createVideoThumbnail(
                file.absolutePath,
                MediaStore.Images.Thumbnails.MINI_KIND
            )?.let { bitmap ->
                val thumbnailMimeType = mimeTypeManager.getMimeTypeForVideoThumbnail()
                val thumbnailFile = fileStorage.createTempCacheFile(
                    suffix = getFileExtensionByMimeTypeSafe(thumbnailMimeType) ?: return@launch
                )
                thumbnailFile.outputStream().use { out ->
                    bitmap.compress(
                        BitmapUtils.mimeTypeToBitmapCompressFormat(thumbnailMimeType),
                        AppConstants.BITMAP_COMPRESS_QUALITY,
                        out
                    )
                }
                thumbnailFile
            }

            if (thumbnail == null) {
                postErrorEvent(R.string.common_error_process_file)
                return@launch
            }

            sendBackground(
                file = file,
                mimeType = mimeType,
                thumbnailFile = thumbnail,
                MediaType.VIDEO
            )
        }
    }

    private suspend fun sendBackground(
        file: File,
        mimeType: String,
        thumbnailFile: File? = null,
        mediaType: MediaType
    ) {
        val param = CreateBackgroundParams(file, mimeType, thumbnailFile?.absolutePath, mediaType)
        createBackgroundUseCase(param)
            .onStart { _event.postEvent(SelectBackgroundAction.CreatingBackground) }
            .collect { result ->
                result
                    .onSuccess {
                        _event.postEvent(SelectBackgroundAction.BackgroundCreated)
                        delay(SELECT_NEW_BG_DELAY)
                        onBackgroundSelected(it.id, it.group)
                    }
                    .onFailure {
                        postErrorEvent(R.string.common_error_something_went_wrong)
                    }
            }
    }

    private fun isVideoValidByDuration(fileDescriptor: FileDescriptor): VideoDurationResult =
        VideoUtils.isVideoValidByDuration(
            fileDescriptor = fileDescriptor,
            maxDurationInSeconds = AppConstants.VIDEO_MAX_DURATION_IN_SECONDS
        )

    private fun postErrorEvent(@StringRes messageRes: Int) {
        _event.postEvent(SelectBackgroundAction.Error(messageRes))
    }

    fun getIsPortraitMedia() {
        viewModelScope.launch {
            _isPortraitMedia.value = getIsPortraitMediaUseCase()
        }
    }

    private fun getFileExtensionByMimeTypeSafe(mimeType: String): String? = try {
        mimeTypeManager.getFileExtensionByMimeType(mimeType)
    } catch (e: UnsupportedMimeType) {
        postErrorEvent(R.string.common_error_process_file)
        null
    }
}