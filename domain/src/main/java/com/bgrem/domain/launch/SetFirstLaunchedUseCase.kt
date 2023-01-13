package com.bgrem.domain.launch

import com.bgrem.domain.common.usecase.UseCaseWithoutParam
import com.bgrem.domain.localstorage.LocalDataStorage

interface SetFirstLaunchedUseCase : UseCaseWithoutParam<Unit>

class SetFirstLaunchedUseCaseImpl(
    private val localDataStorage: LocalDataStorage
) : SetFirstLaunchedUseCase {

    override suspend fun execute() {
        localDataStorage.setFirstLaunched()
    }
}