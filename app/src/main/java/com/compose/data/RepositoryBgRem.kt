package com.compose.data

import androidx.lifecycle.MutableLiveData
import com.compose.models.*
import android.content.SharedPreferences
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.domain.task.TaskDataSource
import com.compose.data.models.RemoveBackgroundProgressStateCompose
import com.compose.enumeration.Plans
import com.compose.enumeration.TypeTask
import com.compose.models.BackImage
import kotlinx.coroutines.flow.Flow
import java.io.File

interface RepositoryBgRem {
    suspend fun getBackImage(): Flow<List<BackImage>>
    suspend fun getBackColor(): Flow<List<Background>>
    suspend fun getFirstFiveFavoriteImages(): Flow<FavoritesFirstFiveResult>
    suspend fun getFirstFiveFavoriteVideos(): Flow<FavoritesFirstFiveResult>
    suspend fun getYourBg(): Flow<List<Background>>
    suspend fun getTransparent(): Flow<List<Background>>
    suspend fun getVideoCategories(): Flow<List<Category>>
    suspend fun getImageCategories(): Flow<List<Category>>

    fun saveStateWelcomeScreen(showing: Boolean)
    fun getStateWelcomeScreen(): Boolean

    val videoFromCategory: MutableLiveData<List<Background>>
    suspend fun getVideoFromCategory(id: String)

    val imageFromCategory: MutableLiveData<List<Background>>
    suspend fun getImageFromCategory(id: String)

    val favoriteVideos: MutableLiveData<List<Background>>
    suspend fun getFavoriteVideos()

    val favoriteImages: MutableLiveData<List<Background>>
    suspend fun getFavoriteImages()

    fun saveVideoIds(text: Set<String>)
    fun getVideoIds(): Set<String>?

    fun saveImageIds(text: Set<String>)
    fun getImageIds(): Set<String>?

    fun registerSharedPrefsListener(
        changeListener: SharedPreferences.OnSharedPreferenceChangeListener
    )

    fun unregisterSharedPrefsListener(
        changeListener: SharedPreferences.OnSharedPreferenceChangeListener
    )

    val favoriteVideosAll: MutableLiveData<FavoritesFirstFiveResult>
    suspend fun getFavoriteVideosAll(ids: String): FavoritesFirstFiveResult

    val favoriteImagesAll: MutableLiveData<FavoritesFirstFiveResult>
    suspend fun getFavoriteImagesAll(ids: String) : FavoritesFirstFiveResult

    val job: MutableLiveData<Job>

    suspend fun createJob(file: File?, mediaType: MediaType?, mimeType: String?): Job?

    suspend fun getTask(taskId: String): Task

    suspend fun createTask(
        jobId: String,
        taskType: TypeTask,
        plan: Plans,
        backgroundId: String?
    ): Task

    suspend fun downloadResultFile(taskId: String): ByteArray

    val taskDone: MutableLiveData<Task>

    val createTaskProgress: Flow<Result<RemoveBackgroundProgressStateCompose>>

    suspend fun removeBackground(
        backgroundId: String?,
        mediaType: MediaType,
        file: File?,
        mimeType: String?
    )

    suspend fun getNewBackground(mediaType: MediaType, backgroundId: String)

    val myBg: MutableLiveData<Background>

    suspend fun createBg(file: File?, mediaType: MediaType?, mimeType: String?): Background?

    suspend fun getNewMyBackground(
        mediaType: MediaType,
        file: File?,
        mimeType: String?
    )

    val errorsServer: MutableLiveData<Int>

    fun downloadResultNewFile(
        params: DownloadResultFileComposeParams,
        fileStorage: FileStorage,
        taskDataSource: TaskDataSource,
        mimeTypeManager: MimeTypeManager,
        directory: File
    ): Flow<Result<File>>

}