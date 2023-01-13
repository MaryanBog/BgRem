package com.bgrem.domain.background

import com.bgrem.domain.background.model.Background
import com.bgrem.domain.common.usecase.FlowUseCaseWithoutParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ObserveBackgroundsUseCase : FlowUseCaseWithoutParam<List<Background>>

class ObserveBackgroundsUseCaseImpl(
    private val backgroundNetworkDataSource: BackgroundNetworkDataSource,
    private val backgroundDbDataSource: BackgroundDbDataSource
) : ObserveBackgroundsUseCase {

    override fun execute(): Flow<Result<List<Background>>> =
        backgroundDbDataSource.observeBackgrounds().map {
            if (it.isEmpty()) getNewBackgrounds()
            Result.success(it)
        }

    private suspend fun getNewBackgrounds() {
        backgroundDbDataSource.addBackgrounds(backgroundNetworkDataSource.getAllBackgrounds())
    }
}