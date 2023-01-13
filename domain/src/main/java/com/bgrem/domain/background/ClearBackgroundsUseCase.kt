package com.bgrem.domain.background

import com.bgrem.domain.common.usecase.UseCaseWithoutParam
import com.bgrem.domain.files.FileStorage

interface ClearBackgroundsUseCase : UseCaseWithoutParam<Unit>

class ClearBackgroundsUseCaseImpl(
    private val backgroundDbDataSource: BackgroundDbDataSource,
    private val fileStorage: FileStorage
) : ClearBackgroundsUseCase {

    override suspend fun execute() {
        backgroundDbDataSource.getUserBackgrounds().forEach { background ->
            background.posterUrl?.let { fileStorage.deleteFile(it) }
        }

        fileStorage.clearCache()
        backgroundDbDataSource.clearAll()
    }
}