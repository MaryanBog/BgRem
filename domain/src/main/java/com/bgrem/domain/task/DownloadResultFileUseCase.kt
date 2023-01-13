package com.bgrem.domain.task

import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.common.usecase.FlowUseCase
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.domain.task.model.DownloadResultFileParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

interface DownloadResultFileUseCase : FlowUseCase<DownloadResultFileParams, File>

class DownloadResultFileUseCaseImpl(
    private val fileStorage: FileStorage,
    private val taskDataSource: TaskDataSource,
    private val mimeTypeManager: MimeTypeManager,
    private val directory: File
) : DownloadResultFileUseCase {

    override fun execute(param: DownloadResultFileParams): Flow<Result<File>> = flow {
        val fileSuffix = when (param.mediaType) {
            MediaType.IMAGE -> if (param.isTransparent) {
                mimeTypeManager.getFileExtensionByMimeType(mimeTypeManager.getResultTransparentImageMimeType())
            } else {
                mimeTypeManager.getFileExtensionByMimeType(mimeTypeManager.getResultImageMimeType())
            }
            MediaType.VIDEO -> mimeTypeManager.getFileExtensionByMimeType(mimeTypeManager.getResultVideoMimeType())
        }
        val tempFile = fileStorage.createTempFile(directory, fileSuffix)
        val resultBytes = taskDataSource.downloadResultFile(param.taskId)

        tempFile.outputStream().use {
            it.write(resultBytes)
        }

        emit(Result.success(tempFile))
    }
}