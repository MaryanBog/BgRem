package com.bgrem.data.net.task.model

import com.bgrem.data.net.background.model.BackgroundGroupDto
import com.bgrem.domain.task.model.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    @SerialName("output_type")
    val outputType: String,
    val status: TaskStatusDto,
    @SerialName("bg_group")
    val bgGroup: BackgroundGroupDto? = null,
    @SerialName("bg_url")
    val bgUrl: String? = null,
    val error: String? = null,
    @SerialName("checkout_url")
    val checkoutUrl: String? = null,
    val price: Int,
    @SerialName("payment_status")
    val paymentStatus: String? = null,
    val duration: Int,
    @SerialName("result_url")
    val resultUrl: String? = null,
    val preview: PreviewDto? = null,
    @SerialName("source_url")
    val sourceUrl: String,
    val plan: String,
    val progress: Int,
    @SerialName("unpaid_seconds")
    val unpaidSeconds: Int,
    @SerialName("bg_rotation_angle")
    val bgRotationAngle: Int,
    @SerialName("mask_scale_factor")
    val maskScaleFactor: Double,
    @SerialName("mask_pos_x")
    val maskPosX: Int,
    @SerialName("mask_pos_y")
    val maskPosY: Int,
    @SerialName("mask_flip")
    val maskFlip: String? = null
) {

    fun toDomain() = Task(
        id = id,
        outputType = outputType,
        status = status.toDomain(),
        bgGroup = bgGroup?.toDomain(),
        error = error,
        checkoutUrl = checkoutUrl,
        price = price,
        paymentStatus = paymentStatus,
        duration = duration,
        resultUrl = resultUrl,
        preview = preview?.toDomain(),
        sourceUrl = sourceUrl,
        plan = plan,
        progress = progress,
        unpaidSeconds = unpaidSeconds,
        bgRotationAngle = bgRotationAngle,
        maskScaleFactor = maskScaleFactor,
        maskPosX = maskPosX,
        maskPosY = maskPosY,
        maskFlip = maskFlip,
        bgUrl = bgUrl
    )
}
