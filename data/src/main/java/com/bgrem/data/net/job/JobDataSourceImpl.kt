package com.bgrem.data.net.job

import com.bgrem.data.net.common.NetworkConstants
import com.bgrem.domain.common.Plan
import com.bgrem.domain.job.JobDataSource
import com.bgrem.domain.job.model.Job
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class JobDataSourceImpl(
    private val jobApi: JobApi
) : JobDataSource {

    override suspend fun createJob(file: File, plan: Plan, mimeType: String): Job {
        val planPart = plan.name.lowercase().toRequestBody(contentType = NetworkConstants.TEXT_PLAIN.toMediaType())
        val filePart = MultipartBody.Part.createFormData(
            name = NetworkConstants.FILE_PART_NAME,
            filename = file.name,
            body = file.asRequestBody(contentType = mimeType.toMediaType())
        )
        return jobApi.createJob(filePart, planPart).toDomain()
    }
}