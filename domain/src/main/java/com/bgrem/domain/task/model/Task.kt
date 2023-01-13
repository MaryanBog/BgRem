package com.bgrem.domain.task.model

import com.bgrem.domain.background.model.BackgroundGroup
import java.io.Serializable

data class Task(
    val id: String,
    val outputType: String,
    val status: TaskStatus,
    val bgGroup: BackgroundGroup?,
    val bgUrl: String?,
    val error: String?,
    val checkoutUrl: String?,
    val price: Int,
    val paymentStatus: String?,
    val duration: Int,
    val resultUrl: String?,
    val preview: Preview?,
    val sourceUrl: String,
    val plan: String,
    val progress: Int,
    val unpaidSeconds: Int,
    val bgRotationAngle: Int,
    val maskScaleFactor: Double,
    val maskPosX: Int,
    val maskPosY: Int,
    val maskFlip: String?
) : Serializable
