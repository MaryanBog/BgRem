package com.bgrem.domain.background

import com.bgrem.domain.background.model.Background
import com.bgrem.domain.background.model.BackgroundType
import com.bgrem.domain.background.model.CreateBackgroundParams
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.common.usecase.FlowUseCase
import com.bgrem.domain.files.FileStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CreateBackgroundUseCase : FlowUseCase<CreateBackgroundParams, Background>

class CreateBackgroundUseCaseImpl(
    private val backgroundNetworkDataSource: BackgroundNetworkDataSource,
    private val backgroundDbDataSource: BackgroundDbDataSource,
    private val fileStorage: FileStorage
) : CreateBackgroundUseCase {

    override fun execute(param: CreateBackgroundParams): Flow<Result<Background>> = flow {
        val background = backgroundNetworkDataSource.createBackground(
            file = param.file,
            mimeType = param.mimeType
        )
        backgroundDbDataSource.addBackground(
            background.copy(
                posterUrl = if (background.thumbnailUrl.isNullOrEmpty()) param.thumbnailUri else null,
                backgroundType = param.mediaType.toBackgroundType()
            )
        )
        fileStorage.deleteFile(param.file)
        emit(Result.success(background))
    }

    private fun MediaType.toBackgroundType() = when (this) {
        MediaType.VIDEO -> BackgroundType.VIDEO
        MediaType.IMAGE -> BackgroundType.IMAGE
    }
}