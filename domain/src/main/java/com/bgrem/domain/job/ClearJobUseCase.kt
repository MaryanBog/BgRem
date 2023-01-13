package com.bgrem.domain.job

import com.bgrem.domain.common.usecase.UseCaseWithoutParam
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.localstorage.LocalDataStorage
import timber.log.Timber
import java.io.File

interface ClearJobUseCase : UseCaseWithoutParam<Unit>

class ClearJobUseCaseImpl(
    private val fileStorage: FileStorage,
    private val localDataStorage: LocalDataStorage,
    private val contentDirectory: File
) : ClearJobUseCase {

    override suspend fun execute() {
        try {
            localDataStorage.setIsPortrait(false)
            localDataStorage.setCurrentJobId(null)
            fileStorage.clearCache()
            fileStorage.clearDirectory(contentDirectory)
        } catch (e: Exception) {
            Timber.e(e) // This error is not important to us
        }
    }
}