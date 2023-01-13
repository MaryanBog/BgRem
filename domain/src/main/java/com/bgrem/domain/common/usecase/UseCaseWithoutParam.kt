package com.bgrem.domain.common.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UseCaseWithoutParam<out R> {
    suspend operator fun invoke(dispatcher: CoroutineDispatcher = Dispatchers.IO): R =
        withContext(dispatcher) { execute() }

    suspend fun execute(): R
}