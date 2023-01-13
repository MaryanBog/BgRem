package com.bgrem.domain.task

import com.bgrem.domain.common.usecase.FlowUseCase
import com.bgrem.domain.task.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GetTaskUseCase : FlowUseCase<String, Task>

class GetTaskUseCaseImpl(
    private val taskDataSource: TaskDataSource
) : GetTaskUseCase {

    /**
     * @param param is task id
     */
    override fun execute(param: String): Flow<Result<Task>> = flow {
        emit(Result.success(taskDataSource.getTask(param)))
    }
}