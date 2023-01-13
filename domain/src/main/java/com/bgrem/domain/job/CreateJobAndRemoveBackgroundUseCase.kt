package com.bgrem.domain.job

import com.bgrem.domain.common.Plan
import com.bgrem.domain.common.usecase.FlowUseCase
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.job.model.CreateJobParams
import com.bgrem.domain.localstorage.LocalDataStorage
import com.bgrem.domain.task.TaskDataSource
import com.bgrem.domain.task.model.Task
import com.bgrem.domain.task.model.TaskStatus
import com.bgrem.domain.task.model.TaskType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CreateJobAndRemoveBackgroundUseCase : FlowUseCase<CreateJobParams, Task>

class CreateJobAndRemoveBackgroundUseCaseImpl(
    private val fileStorage: FileStorage,
    private val jobDataSource: JobDataSource,
    private val taskDataSource: TaskDataSource,
    private val localDataStorage: LocalDataStorage
) : CreateJobAndRemoveBackgroundUseCase {
    override fun execute(param: CreateJobParams): Flow<Result<Task>> = flow {
        val job = jobDataSource.createJob(
            file = param.file,
            plan = Plan.MOBILE,
            mimeType = param.mimeType
        )
        var task = taskDataSource.createTask(
            jobId = job.id,
            taskType = TaskType.PREVIEW,
            plan = Plan.MOBILE,
            backgroundId = null
        )

        while (task.status != TaskStatus.DONE) {
            task = taskDataSource.getTask(task.id)
        }

        localDataStorage.setIsPortrait(job.isPortrait)
        localDataStorage.setCurrentJobId(job.id)
        fileStorage.deleteFile(param.file)
        emit(Result.success(task))
    }
}