package com.bgrem.domain.task

import com.bgrem.domain.common.Plan
import com.bgrem.domain.task.model.Task
import com.bgrem.domain.task.model.TaskType
import java.io.File

interface TaskDataSource {
    suspend fun getTask(taskId: String): Task

    suspend fun createTask(
        jobId: String,
        taskType: TaskType,
        plan: Plan,
        backgroundId: String?
    ): Task

    suspend fun downloadResultFile(taskId: String): ByteArray
}