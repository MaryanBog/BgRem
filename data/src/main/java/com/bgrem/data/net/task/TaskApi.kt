package com.bgrem.data.net.task

import com.bgrem.data.net.task.model.TaskDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {
    @GET("tasks/{taskId}/")
    suspend fun getTask(@Path("taskId") taskId: String): TaskDto

    @POST("tasks/")
    suspend fun createTask(@Body body: RequestBody): TaskDto

    @GET("tasks/{taskId}/download/")
    suspend fun downloadResultFile(@Path("taskId") taskId: String): ResponseBody
}