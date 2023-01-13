package com.compose.data

import com.compose.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface BgRemApi {

    @Multipart
    @POST("bg/")
    suspend fun createBackground(@Part file: MultipartBody.Part): Background

    @Multipart
    @POST("jobs/")
    suspend fun createJob(
        @Part file: MultipartBody.Part
    ): Job

    @GET("tasks/{taskId}/")
    suspend fun getTask(@Path("taskId") taskId: String): Task

    @POST("tasks/")
    suspend fun createTask(@Body body: RequestBody): Task

    @GET("tasks/{taskId}/download/")
    suspend fun downloadResultFile(@Path("taskId") taskId: String): ResponseBody

    @GET("bg/")
    suspend fun getBackImage(): List<BackImage>

    @GET("backgrounds/?group=video&ids=")
    suspend fun getFavoritesVideos(): List<Background>

    @GET("backgrounds/?group=image&ids=")
    suspend fun getFavoritesImages(): List<Background>

    @GET("backgrounds/?group=color")
    suspend fun getBackColor(): List<Background>

    @GET("v1/backgrounds/?group=image&favorite=1")
    suspend fun getFiveFavoritesImages(): FavoritesFirstFiveResult

    @GET("v1/backgrounds/?group=video&favorite=1/")
    suspend fun getFiveFavoritesVideos(): FavoritesFirstFiveResult

    @GET("backgrounds/?group=user")
    suspend fun getYourBg(): List<Background>

    @GET("backgrounds/?group=transparent")
    suspend fun getTransparent(): List<Background>

    @GET("backgrounds/categories/?group=video")
    suspend fun getVideoCategories(): List<Category>

    @GET("backgrounds/categories/?group=image")
    suspend fun getImageCategories(): List<Category>

    @GET("backgrounds/?group=video")
    suspend fun getVideoFromCategory(@Query("category_id") id: String): List<Background>

    @GET("backgrounds/?group=image")
    suspend fun getImageFromCategory(@Query("category_id") id: String): List<Background>

    @GET("v1/backgrounds/?group=video")
    suspend fun getFavoritesVideos(@Query("ids") id: String): FavoritesFirstFiveResult

    @GET("v1/backgrounds/?group=image")
    suspend fun getFavoritesImages(@Query("ids") id: String): FavoritesFirstFiveResult



}