package com.bgrem.domain.job

import com.bgrem.domain.common.usecase.UseCaseWithoutParam
import com.bgrem.domain.localstorage.LocalDataStorage

interface GetIsPortraitMediaUseCase : UseCaseWithoutParam<Boolean>

class GetIsPortraitMediaUseCaseImpl(
    private val localDataStorage: LocalDataStorage
) : GetIsPortraitMediaUseCase {
    override suspend fun execute(): Boolean {
        return localDataStorage.getIsPortrait()
    }
}