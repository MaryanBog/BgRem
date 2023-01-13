package com.bgrem.domain.common.usecase

import com.bgrem.domain.common.extensions.handleOn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface FlowUseCaseWithoutParam<out R> {
    suspend operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Result<R>> = execute().handleOn(dispatcher)

    fun execute(): Flow<Result<R>>
}