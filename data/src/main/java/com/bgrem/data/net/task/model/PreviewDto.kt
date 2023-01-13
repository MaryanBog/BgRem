package com.bgrem.data.net.task.model

import com.bgrem.domain.task.model.Preview
import com.bgrem.domain.task.model.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewDto(
    val id: String,
    @SerialName("result_url")
    val resultUrl: String
) {

    fun toDomain() = Preview(
        id = id,
        resultUrl = resultUrl
    )
}
