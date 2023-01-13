package com.compose.data.api

import com.compose.models.Background
import com.compose.models.Category
import com.compose.models.FavoritesFirstFiveResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.*

private const val BASE_URL = "https://dev.bgrem.deelvin.com/api/"

private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .retryOnConnectionFailure(true)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface ApiService {

    @GET("v1/backgrounds/?group=image&favorite=1\"")
    suspend fun getFiveFavoritesImages(): Response<FavoritesFirstFiveResult>

    @GET("v1/backgrounds/?group=video&favorite=1\"")
    suspend fun getFiveFavoritesVideos(): Response<FavoritesFirstFiveResult>

    @GET("backgrounds/?group=color")
    suspend fun getColors(): Response<List<Background>>

    @GET("backgrounds/?group=user")
    suspend fun getYourBg(): Response<List<Background>>

    @GET("backgrounds/?group=transparent")
    suspend fun getTransparent(): Response<List<Background>>

    @GET("backgrounds/categories/?group=video")
    suspend fun getVideoCategories(): Response<List<Category>>

    @GET("backgrounds/categories/?group=image")
    suspend fun getImageCategories(): Response<List<Category>>

    //    получение видео по категориям:
    @GET("backgrounds/?group=video&category_id=1")
    suspend fun getVideoCatOne(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=2")
    suspend fun getVideoCatTwo(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=3")
    suspend fun getVideoCatThree(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=4")
    suspend fun getVideoCatFour(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=5")
    suspend fun getVideoCatFive(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=6")
    suspend fun getVideoCatSix(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=7")
    suspend fun getVideoCatSeven(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=8")
    suspend fun getVideoCatEigth(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=9")
    suspend fun getVideoCatNine(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=10")
    suspend fun getVideoCatTen(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=11")
    suspend fun getVideoCatEleven(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=12")
    suspend fun getVideoCatTwelve(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=13")
    suspend fun getVideoCatThirteen(): Response<List<Background>>

    @GET("backgrounds/?group=video&category_id=14")
    suspend fun getVideoCatFourthteen(): Response<List<Background>>

    //    получение картинок по категориям:
    @GET("backgrounds/?group=image&category_id=1")
    suspend fun getImageCatOne(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=2")
    suspend fun getImageCatTwo(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=3")
    suspend fun getImageCatThree(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=4")
    suspend fun getImageCatFour(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=5")
    suspend fun getImageCatFive(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=6")
    suspend fun getImageCatSix(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=7")
    suspend fun getImageCatSeven(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=8")
    suspend fun getImageCatEight(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=9")
    suspend fun getImageCatNine(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=10")
    suspend fun getImageCatTen(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=11")
    suspend fun getImageCatEleven(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=12")
    suspend fun getImageCatTwelve(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=13")
    suspend fun getImageCatThirteen(): Response<List<Background>>

    @GET("backgrounds/?group=image&category_id=14")
    suspend fun getImageCatFourthteen(): Response<List<Background>>


    object Api {
        val retrofitService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }

    }

}