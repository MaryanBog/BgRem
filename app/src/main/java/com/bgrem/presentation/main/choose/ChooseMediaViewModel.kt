package com.bgrem.presentation.main.choose

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.net.toFile
import androidx.lifecycle.viewModelScope
import com.bgrem.app.R
import com.bgrem.domain.common.failure.UnsupportedMimeType
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.presentation.common.AppConstants
import com.bgrem.presentation.common.StateViewModel
import com.bgrem.presentation.common.extensions.postEvent
import com.bgrem.presentation.common.utils.BitmapUtils
import com.bgrem.presentation.main.choose.model.ChooseMediaAction
import com.bgrem.presentation.main.choose.model.ChooseMediaState
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class ChooseMediaViewModel(
    private val fileStorage: FileStorage,
    private val mimeTypeManager: MimeTypeManager
) : StateViewModel<ChooseMediaState, ChooseMediaAction>(ChooseMediaState()) {

    val selectedMediaType: MediaType? get() = _state.value?.selectedMediaType
    val imageMimeTypes: List<String> get() = mimeTypeManager.getAvailableImageMimeTypes()
    val videoMimeTypes: List<String> get() = mimeTypeManager.getAvailableVideoMimeTypes()

    fun onSelectMediaType(mediaType: MediaType?) {
        _state.value = _state.value?.copy(selectedMediaType = mediaType)
        _event.postEvent(
            when (mediaType) {
                MediaType.IMAGE -> ChooseMediaAction.SelectImage
                MediaType.VIDEO -> ChooseMediaAction.SelectVideo
                null -> ChooseMediaAction.ClearSelectionMediaType
            }
        )
    }

    fun onTakePhotoSucceeded(uri: Uri) {
        val photoFile = uri.toFile()
        viewModelScope.launch {
            val bitmap = resizeBitmap(bytes = photoFile.readBytes()).let {
                if (it == null) return@launch /**Bag BitmapUtils.kt line 25 */
                BitmapUtils.rotateImageIfNeeded(it, photoFile)
            }
            sendBitmap(bitmap, mimeTypeManager.getMimeTypeForNewImage())
        }
    }

    fun onTakeVideoSucceeded(uri: Uri?) {
        if (uri != null) {
            postSelectedThumbnail(uri)
        } else return
    }

    fun onMediaSelected(
        inputStream: InputStream?,
        mimeType: String?,
        uri: Uri?
    ) {
        if (inputStream == null || mimeType.isNullOrBlank()) {
            return
        }

        when (selectedMediaType) {
            MediaType.IMAGE -> onImageSelected(inputStream, mimeType)
            MediaType.VIDEO -> onVideoSelected(inputStream, mimeType, uri)
            else -> Unit
        }
    }

    private fun onImageSelected(inputStream: InputStream, mimeType: String) {
        viewModelScope.launch {
            val bitmap = resizeBitmap(bytes = inputStream.use { it.readBytes() })
            sendBitmap(bitmap, mimeType)
        }
    }

    private fun onVideoSelected(
        inputStream: InputStream,
        mimeType: String,
        uri: Uri?
    ) {
        viewModelScope.launch {
            val file = fileStorage.createTempCacheFile(
                suffix = getFileExtensionByMimeTypeSafe(mimeType) ?: return@launch
            )
            file.outputStream().use {
                inputStream.copyTo(it)
            }
            postSelectedMedia(file, MediaType.VIDEO, mimeType, uri)
        }
    }

    /**Bag BitmapUtils.kt line 25 */
    private fun resizeBitmap(bytes: ByteArray): Bitmap? {
        return try {
            BitmapUtils.resizeBitmap(
                bitmap = BitmapUtils.bytesToBitmap(bytes),
                largeSide = AppConstants.IMAGE_MAX_LARGE_SIDE,
                smallerSide = AppConstants.IMAGE_MAX_SMALLER_SIDE
            )
        } catch (e: Exception) {
            return null
        }
    }


    private fun sendBitmap(bitmap: Bitmap?, mimeType: String) { /**Bag BitmapUtils.kt line 25 */
    val imageFile = fileStorage.createTempCacheFile(
            suffix = getFileExtensionByMimeTypeSafe(mimeType) ?: return
        ).also { file ->
            file.outputStream().use {
                bitmap?.compress( /**Bag BitmapUtils.kt line 25 */
                    BitmapUtils.mimeTypeToBitmapCompressFormat(mimeType),
                    AppConstants.BITMAP_COMPRESS_QUALITY,
                    it
                )
            }
        }
        postSelectedMedia(imageFile, MediaType.IMAGE, mimeType, null)
    }

    private fun postErrorEvent(@StringRes messageRes: Int) {
        _event.postEvent(ChooseMediaAction.Error(messageRes))
    }

    private fun postSelectedMedia(file: File, mediaType: MediaType, mimeType: String, uri: Uri?) {
        _event.postEvent(ChooseMediaAction.MediaSelected(file, mediaType, mimeType, uri))
    }

    private fun postSelectedThumbnail(uri: Uri) {
        _event.postEvent(ChooseMediaAction.ThumbnailSelected(uri))
    }

    private fun getFileExtensionByMimeTypeSafe(mimeType: String): String? = try {
        mimeTypeManager.getFileExtensionByMimeType(mimeType)
    } catch (e: UnsupportedMimeType) {
        postErrorEvent(R.string.common_error_process_file)
        null
    }
}