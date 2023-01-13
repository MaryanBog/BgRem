package com.compose.models

import com.bgrem.domain.background.model.BackgroundGroup
import com.bgrem.domain.task.model.Preview
import com.bgrem.domain.task.model.TaskStatus
import com.compose.data.models.TaskStatusCopmose
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Task(
    @Json(name = "id")
    val id: String,
    @Json(name = "output_type")
    val outputType: String,
    @Json(name = "bg_group")
    var bg_group: String?,
    @Json(name = "status")
    val status: String,
    @Json(name = "bg_url")
    val bgUrl: String?,
    @Json(name = "error")
    val error: String?,
    @Json(name = "checkout_url")
    val checkoutUrl: String?,
    @Json(name = "price")
    val price: Int,
    @Json(name = "payment_status")
    val paymentStatus: String?,
    @Json(name = "duration")
    val duration: Int,
    @Json(name = "result_url")
    val resultUrl: String?,
    @Json(name = "preview")
    val preview: PreviewTask?,
    @Json(name = "source_url")
    val sourceUrl: String,
    @Json(name = "plan")
    val plan: String,
    @Json(name = "progress")
    val progress: Int,
    @Json(name = "unpaid_seconds")
    val unpaidSeconds: Int,
    @Json(name = "bg_rotation_angle")
    val bgRotationAngle: Int,
    @Json(name = "mask_scale_factor")
    val maskScaleFactor: Double,
    @Json(name = "mask_pos_x")
    val maskPosX: Int,
    @Json(name = "mask_pos_y")
    val maskPosY: Int,
    @Json(name = "mask_flip")
    val maskFlip: String?,
    @Json(name = "thumbnail_url")
    val thumbnail_url: String?,
    @Json(name = "download_url")
    val download_url: String?
)