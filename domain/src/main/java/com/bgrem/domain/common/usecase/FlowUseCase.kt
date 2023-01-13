package com.bgrem.domain.common.usecase

import com.bgrem.domain.common.extensions.handleOn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface FlowUseCase<in P, out R> {
    suspend operator fun invoke(
        param: P,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Result<R>> = execute(param).handleOn(dispatcher)

    fun execute(param: P): Flow<Result<R>>
}