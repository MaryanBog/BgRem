package com.compose.presentation.finalFragment

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgrem.domain.common.media.MediaType
import com.compose.data.FileStorages
import com.compose.data.RepositoryBgRem
import com.compose.models.Task
import com.compose.utils.MimeTypesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class FinalComposeViewModel @Inject constructor(
    private val repositoryBgRem: RepositoryBgRem,
    private val fileStorages: FileStorages,
    private val mimeTypes: MimeTypesUtils,
    private val contentResolver: ContentResolver,
    private val directory: File?
) : ViewModel() {

    private var currentMediaFile: File? = null
    private var currentTask: Task? = null
    var currentMediaType = MediaType.IMAGE

    private val _finalResultAction = MutableStateFlow<FinalResultAction>(FinalResultAction.Start)
    val finalResultAction = _finalResultAction.asStateFlow()

    private val _visibleProgressBar = MutableStateFlow(false)
    val visibleProgressBar = _visibleProgressBar.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun saveMedia(mediaType: MediaType, task: Task) {
        currentTask = task
        currentMediaType = mediaType
        saveMediaResult(task, mediaType)
    }

    fun sharedFile(mediaType: MediaType, task: Task) {
        currentTask = task
        currentMediaType = mediaType
        sharedMediaFile(task, mediaType)
    }

    fun sharedApp(){
        viewModelScope.launch {
            _finalResultAction.value = FinalResultAction.SharedApp
        }
    }

    fun plugClick(){
        _finalResultAction.value = FinalResultAction.Plug
    }

    fun publicationInInstagram(mediaType: MediaType, task: Task){
        currentTask = task
        currentMediaType = mediaType
        sharedInstagram(task, mediaType)
    }

    fun showProgressBar(visible: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            _visibleProgressBar.emit(visible)
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

    fun finalSaveFile(){
        val file = currentMediaFile ?: return
        val values = getContentValues(currentMediaType, file.nameWithoutExtension)
        try {
            val externalUri = getExternalUri(values, currentMediaType)
            if (externalUri != null) {
                val output = contentResolver.openOutputStream(externalUri)
                val input = file.inputStream()
                output.use { outputStream ->
                    outputStream?.write(input.readBytes())
                }
            }
        } catch (_: IOException) {
            _finalResultAction.value = FinalResultAction.Error
        }
    }

    private fun saveMediaResult(task: Task, mediaType: MediaType) {
        viewModelScope.launch(Dispatchers.Main) {
            downloadTempFile(task, mediaType)
                .onStart { _finalResultAction.value = FinalResultAction.Loading }
                .collect{ result ->
                    result
                        .onSuccess {
                            currentMediaFile = it
                            _finalResultAction.value = FinalResultAction.Saved
                        }
                        .onFailure {
                            _finalResultAction.value = FinalResultAction.Error
                        }
                }
        }
    }

    private fun sharedInstagram(task: Task, mediaType: MediaType) {
        viewModelScope.launch(Dispatchers.Main) {
            downloadTempFile(task, mediaType)
                .onStart { _finalResultAction.value = FinalResultAction.Loading }
                .collect{ result ->
                    result
                        .onSuccess {
                            currentMediaFile = it
                            _finalResultAction.value = FinalResultAction.SharedInstagram(it)
                        }
                        .onFailure {
                            _finalResultAction.value = FinalResultAction.Error
                        }
                }
        }
    }

    private fun sharedMediaFile(task: Task, mediaType: MediaType) {
        viewModelScope.launch(Dispatchers.Main) {
            downloadTempFile(task, mediaType)
                .onStart { _finalResultAction.value = FinalResultAction.Loading }
                .collect{ result ->
                    result
                        .onSuccess {
                            currentMediaFile = it
                            _finalResultAction.value = FinalResultAction.SharedFile(it)
                        }
                        .onFailure {
                            _finalResultAction.value = FinalResultAction.Error
                        }
                }
        }
    }

    private fun downloadTempFile(task: Task, mediaType: MediaType): Flow<Result<File>> = flow {
        val resultBytes = repositoryBgRem.downloadResultFile(task.id)
        val fileSuffix = if (mediaType == MediaType.IMAGE) {
            mimeTypes.getFileExtensionImageType(
                mimeTypes.getMimeTypeForNewImage()
            )
        } else {
            mimeTypes.getFileExtensionVideoType(
                mimeTypes.getMimeTypeForNewVideo()
            )
        }
        val tempFile = fileStorages.createTempDirFile(directory, fileSuffix)

        tempFile.outputStream().use {
            it.write(resultBytes)
        }
        emit(Result.success(tempFile))
    }

    private fun getContentValues(mediaType: MediaType, fileName: String): ContentValues{
        val values = if (mediaType == MediaType.VIDEO){
            ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                put(
                    MediaStore.Video.Media.MIME_TYPE,
                    mimeTypes.getMimeTypeForNewVideo()
                )
            }
        } else {
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    mimeTypes.getMimeTypeForNewImage()
                )
            }
        }
        return values
    }

    private fun getExternalUri(values: ContentValues, mediaType: MediaType): Uri?{
        val externalContentUri: Uri = if (mediaType == MediaType.VIDEO){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Video.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                ) else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                ) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        return contentResolver.insert(externalContentUri, values)
    }
}