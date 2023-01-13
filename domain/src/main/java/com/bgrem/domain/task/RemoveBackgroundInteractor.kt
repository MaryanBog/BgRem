package com.bgrem.domain.task

import com.bgrem.domain.common.Plan
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.localstorage.LocalDataStorage
import com.bgrem.domain.task.model.RemoveBackgroundProgressState
import com.bgrem.domain.task.model.Task
import com.bgrem.domain.task.model.TaskStatus
import com.bgrem.domain.task.model.TaskType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface RemoveBackgroundInteractor {
    val createTaskProgress: Flow<Result<RemoveBackgroundProgressState>>

    suspend fun removeBackground(backgroundId: String?, mediaType: MediaType)
}

class RemoveBackgroundInteractorImpl(
    private val localDataStorage: LocalDataStorage,
    private val taskDataSource: TaskDataSource
) : RemoveBackgroundInteractor {

    private val _createTaskProgress: MutableStateFlow<Result<RemoveBackgroundProgressState>> =
        MutableStateFlow(Result.success(RemoveBackgroundProgressState.Progress(0)))
    override val createTaskProgress: Flow<Result<RemoveBackgroundProgressState>> =
        _createTaskProgress

    override suspend fun removeBackground(backgroundId: String?, mediaType: MediaType) = safeCall  {
        emitProgress(0)
        val currentJobId = localDataStorage.getCurrentJobId().orEmpty()
        var task = taskDataSource.createTask(
            jobId = currentJobId,
            taskType = TaskType.VIDEO,  // For images and video use type VIDEO
            plan = Plan.MOBILE,
            backgroundId = backgroundId
        )

        while (task.status != TaskStatus.DONE) {
            delay(1000)
            task = taskDataSource.getTask(task.id)
            emitProgress(
                when (mediaType) {
                    MediaType.IMAGE -> task.status.toProgressValue()
                    MediaType.VIDEO -> task.progress
                }
            )
        }

        emitFinished(task = task)
    }

    private suspend fun emitProgress(progress: Int) {
        _createTaskProgress.emit(Result.success(RemoveBackgroundProgressState.Progress(progress)))
    }

    private suspend fun emitFinished(task: Task) {
        _createTaskProgress.emit(Result.success(RemoveBackgroundProgressState.Finished(task)))
    }

    private suspend fun safeCall(action: suspend () -> Unit) = try {
        action.invoke()
    } catch (e: Exception) {
        _createTaskProgress.emit(Result.failure(e))
    }

    private fun TaskStatus.toProgressValue() = when (this) {
        TaskStatus.QUEUE -> 10
        TaskStatus.ASSIGNED -> 30
        TaskStatus.DOWNLOAD -> 50
        TaskStatus.IN_WORK -> 70
        TaskStatus.UPLOAD -> 90
        TaskStatus.DONE -> 100
    }
}