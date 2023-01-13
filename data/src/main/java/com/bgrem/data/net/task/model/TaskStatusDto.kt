package com.bgrem.data.net.task.model

import com.bgrem.domain.task.model.TaskStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskStatusDto {
    @SerialName("queue")
    QUEUE,

    @SerialName("assigned")
    ASSIGNED,

    @SerialName("download")
    DOWNLOAD,

    @SerialName("in_work")
    IN_WORK,

    @SerialName("upload")
    UPLOAD,

    @SerialName("done")
    DONE;

    fun toDomain() = when (this) {
        QUEUE -> TaskStatus.QUEUE
        ASSIGNED -> TaskStatus.ASSIGNED
        DOWNLOAD -> TaskStatus.DOWNLOAD
        IN_WORK -> TaskStatus.IN_WORK
        UPLOAD -> TaskStatus.UPLOAD
        DONE -> TaskStatus.DONE
    }
}