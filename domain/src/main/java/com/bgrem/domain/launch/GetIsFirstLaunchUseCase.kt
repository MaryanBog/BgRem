package com.bgrem.domain.launch

import com.bgrem.domain.common.usecase.UseCaseWithoutParam
import com.bgrem.domain.localstorage.LocalDataStorage

interface GetIsFirstLaunchUseCase : UseCaseWithoutParam<Boolean>

class GetIsFirstLaunchUseCaseImpl(
    private val localDataStorage: LocalDataStorage
) : GetIsFirstLaunchUseCase {

    override suspend fun execute(): Boolean {
        return localDataStorage.getIsFirstLaunch()
    }
}