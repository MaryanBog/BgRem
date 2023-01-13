package com.bgrem.domain.common.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

fun <T> Flow<Result<T>>.handleOn(dispatcher: CoroutineDispatcher): Flow<Result<T>> = this
    .catch { e ->
        Timber.e(e)
        emit(Result.failure(e))
    }.flowOn(dispatcher)