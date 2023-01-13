package com.bgrem.domain.job

import com.bgrem.domain.common.Plan
import com.bgrem.domain.job.model.Job
import java.io.File

interface JobDataSource {
    suspend fun createJob(file: File, plan: Plan, mimeType: String): Job
}