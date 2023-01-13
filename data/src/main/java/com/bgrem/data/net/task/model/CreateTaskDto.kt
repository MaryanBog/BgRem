package com.bgrem.data.net.task.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskDto(
    val job: String,
    @SerialName("task_type")
    val taskType: String
)
