package com.bgrem.data.net.task

import com.bgrem.data.net.common.RetrofitDataSource
import com.bgrem.domain.common.Plan
import com.bgrem.domain.task.TaskDataSource
import com.bgrem.domain.task.model.Task
import com.bgrem.domain.task.model.TaskType

class TaskDataSourceImpl(private val taskApi: TaskApi) : TaskDataSource, RetrofitDataSource {

    private companion object {
        const val KEY_JOB = "job"
        const val KEY_TASK_TYPE = "task_type"
        const val KEY_BACKGROUND_ID = "bg_id"
        const val KEY_PLAN = "plan"
    }

    override suspend fun getTask(taskId: String): Task {
        return taskApi.getTask(taskId).toDomain()
    }

    override suspend fun createTask(
        jobId: String,
        taskType: TaskType,
        plan: Plan,
        backgroundId: String?
    ): Task {
        val body = buildRequestBody(
            KEY_JOB to jobId,
            KEY_TASK_TYPE to taskType.name.lowercase(),
            KEY_BACKGROUND_ID to backgroundId,
            KEY_PLAN to plan.name.lowercase()
        )
        return taskApi.createTask(body).toDomain()
    }

    override suspend fun downloadResultFile(taskId: String): ByteArray {
        return taskApi.downloadResultFile(taskId).bytes()
    }
}