package com.bgrem.data.net.job

import com.bgrem.data.net.job.model.JobDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface JobApi {
    @Multipart
    @POST("jobs/")
    suspend fun createJob(
        @Part file: MultipartBody.Part,
        @Part("plan") plan: RequestBody
    ): JobDto
}