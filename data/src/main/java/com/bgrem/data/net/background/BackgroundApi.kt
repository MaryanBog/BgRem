package com.bgrem.data.net.background

import com.bgrem.data.net.background.model.BackgroundDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BackgroundApi {
    @GET("bg/")
    suspend fun getAllBackgrounds(): List<BackgroundDto>

    @Multipart
    @POST("bg/")
    suspend fun createBackground(@Part file: MultipartBody.Part): BackgroundDto
}