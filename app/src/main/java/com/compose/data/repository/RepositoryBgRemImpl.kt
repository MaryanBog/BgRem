package com.compose.data.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.bgrem.domain.common.media.MediaType
import com.bgrem.domain.files.FileStorage
import com.bgrem.domain.files.MimeTypeManager
import com.bgrem.domain.task.TaskDataSource
import com.compose.data.network.Network
import com.compose.data.RepositoryBgRem
import com.compose.data.SharedPrefStorage
import com.compose.data.models.RemoveBackgroundProgressStateCompose
import com.compose.data.network.RetrofitDataSource
import com.compose.enumeration.Plans
import com.compose.enumeration.TypeTask
import com.compose.models.*
import com.compose.utils.Constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class RepositoryBgRemImpl @Inject constructor(
    private val sharedPrefStorage: SharedPrefStorage
) : RepositoryBgRem, RetrofitDataSource {

    override suspend fun getBackImage(): Flow<List<BackImage>> = flow {
        emit(Network.bgRemApi.getBackImage())
    }

    override suspend fun getBackColor(): Flow<List<Background>> = flow {
        emit(Network.bgRemApi.getBackColor())
    }

    override fun saveStateWelcomeScreen(showing: Boolean) {
        sharedPrefStorage.saveStateWelcomeScreen(showing)
    }

    override fun getStateWelcomeScreen(): Boolean {
        return sharedPrefStorage.getStateWelcomeScreen()
    }

    override suspend fun getFirstFiveFavoriteImages(): Flow<FavoritesFirstFiveResult> = flow {
        emit(Network.bgRemApi.getFiveFavoritesImages())
    }

    override suspend fun getFirstFiveFavoriteVideos(): Flow<FavoritesFirstFiveResult> = flow {
        emit(Network.bgRemApi.getFiveFavoritesVideos())
    }

    override suspend fun getYourBg(): Flow<List<Background>> = flow {
        emit(Network.bgRemApi.getYourBg())
    }

    override suspend fun getTransparent(): Flow<List<Background>> = flow {
        emit(Network.bgRemApi.getTransparent())
    }

    override suspend fun getVideoCategories(): Flow<List<Category>> = flow {
        emit(Network.bgRemApi.getVideoCategories())
    }

    override suspend fun getImageCategories(): Flow<List<Category>> = flow {
        emit(Network.bgRemApi.getImageCategories())
    }

    override val videoFromCategory = MutableLiveData<List<Background>>()

    override suspend fun getVideoFromCategory(id: String) {
        val videos = Network.bgRemApi.getVideoFromCategory(id)
        videoFromCategory.value = videos
    }

    override val imageFromCategory = MutableLiveData<List<Background>>()

    override suspend fun getImageFromCategory(id: String) {
        val images = Network.bgRemApi.getImageFromCategory(id)
        imageFromCategory.value = images
    }

    override val favoriteVideos = MutableLiveData<List<Background>>()

    override suspend fun getFavoriteVideos() {
        val videos = Network.bgRemApi.getFavoritesVideos()
        favoriteVideos.value = videos
    }

    override val favoriteImages = MutableLiveData<List<Background>>()

    override suspend fun getFavoriteImages() {
        val videos = Network.bgRemApi.getFavoritesImages()
        favoriteImages.value = videos
    }

    override fun saveImageIds(text: Set<String>) {
        sharedPrefStorage.saveImageIds(text)
    }

    override fun getImageIds(): Set<String>? {
        return sharedPrefStorage.getImageIds()
    }

    override fun saveVideoIds(text: Set<String>) {
        sharedPrefStorage.saveVideoIds(text)
    }

    override fun getVideoIds(): Set<String>? {
        return sharedPrefStorage.getVideoIds()
    }

    override fun registerSharedPrefsListener(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPrefStorage.registerSharedPrefsListener(changeListener)
    }

    override fun unregisterSharedPrefsListener(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPrefStorage.unregisterSharedPrefsListener(changeListener)
    }

    override val favoriteImagesAll = MutableLiveData<FavoritesFirstFiveResult>()

    override suspend fun getFavoriteImagesAll(ids: String) : FavoritesFirstFiveResult {
        val images = Network.bgRemApi.getFavoritesImages(ids)
        favoriteImagesAll.value = images
        return images
    }

    override val favoriteVideosAll = MutableLiveData<FavoritesFirstFiveResult>()

    override suspend fun getFavoriteVideosAll(ids: String) : FavoritesFirstFiveResult {
        val videos = Network.bgRemApi.getFavoritesVideos(ids)
        favoriteVideosAll.value = videos
        return videos
    }

    override val job = MutableLiveData<Job>()

    override suspend fun createJob(file: File?, mediaType: MediaType?, mimeType: String?): Job? {
        val filesPart = file?.asRequestBody(contentType = mimeType?.toMediaType())?.let {
            MultipartBody.Part.createFormData(
                name = Constant.FILE_NAME_PART,
                filename = file.name,
                body = it
            )
        }
        val gotJob = filesPart?.let { Network.bgRemApi.createJob(it) }
        job.value = gotJob
        return filesPart?.let { Network.bgRemApi.createJob(it) }
    }

    override suspend fun getTask(taskId: String): Task {
        val result = Network.bgRemApi.getTask(taskId)
        taskDone.value = result
        return Network.bgRemApi.getTask(taskId)
    }

    override val taskDone = MutableLiveData<Task>()

    override suspend fun createTask(
        jobId: String,
        taskType: TypeTask,
        plan: Plans,
        backgroundId: String?
    ): Task {
        val body = buildRequestComposeBody(
            Constant.KEY_JOB to jobId,
            Constant.KEY_TASK_TYPE to taskType.name.lowercase(),
            Constant.KEY_BACKGROUND_ID to backgroundId,
            Constant.KEY_PLAN to plan.name.lowercase()
        )

        return Network.bgRemApi.createTask(body)
    }

    override suspend fun downloadResultFile(taskId: String): ByteArray {
        return Network.bgRemApi.downloadResultFile(taskId).bytes()
    }

    private val _createTaskProgress: MutableStateFlow<Result<RemoveBackgroundProgressStateCompose>> =
        MutableStateFlow(Result.success(RemoveBackgroundProgressStateCompose.Progress(0.0f)))
    override val createTaskProgress: Flow<Result<RemoveBackgroundProgressStateCompose>> =
        _createTaskProgress

    override val errorsServer = MutableLiveData<Int>()

    @SuppressLint("SuspiciousIndentation")
    override suspend fun removeBackground(
        backgroundId: String?,
        mediaType: MediaType,
        file: File?,
        mimeType: String?
    ) = safeCall {
        errorsServer.value = 0
        emitProgress(0.0f)
        val job = createJob(
            file = file,
            mimeType = mimeType,
            mediaType = mediaType
        )

        var task = job?.let {
            createTask(
                jobId = it.id,
                taskType = TypeTask.PREVIEW,
                plan = Plans.MOBILE,
                backgroundId = backgroundId
            )
        }
        while (task!!.status != "done") {
            delay(1000)
            task = getTask(task.id)

            emitProgress(
                toProgressComposeValue(task.status)
            )

            if (task.status == "error") {
                errorsServer.value = 1
                break
            }

        }
        emitFinished(task = task)

    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun getNewBackground(
        mediaType: MediaType,
        backgroundId: String
    ) = safeCall {
        emitProgress(0.0f)

        when (mediaType) {
            MediaType.VIDEO -> {
                var task = createTask(
                    jobId = job.value!!.id,
                    taskType = TypeTask.VIDEO,
                    plan = Plans.MOBILE,
                    backgroundId = backgroundId
                )
                while (task.status != "done") {
                    delay(1000)
                    task = getTask(task.id)

                    emitProgress(
                        toProgressComposeValue(task.status)
                    )

                    if (task.status == "error") {
                        errorsServer.value = 1
                        break
                    }

                }
                emitFinished(task = task)
            }
            MediaType.IMAGE -> {
                var task = createTask(
                    jobId = job.value!!.id,
                    taskType = TypeTask.VIDEO,
                    plan = Plans.MOBILE,
                    backgroundId = backgroundId
                )
                while (task.status != "done") {
                    delay(1000)
                    task = getTask(task.id)

                    emitProgress(
                        toProgressComposeValue(task.status)
                    )

                    if (task.status == "error") {
                        errorsServer.value = 1
                        break
                    }

                }
            }

        }

    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun getNewMyBackground(
        mediaType: MediaType,
        file: File?,
        mimeType: String?
    ) = safeCall {
        emitProgress(0.0f)

        when (mediaType) {
            MediaType.VIDEO -> {
                val myNewBg = createBg(file, mediaType, mimeType)
                var task = createTask(
                    jobId = job.value!!.id,
                    taskType = TypeTask.VIDEO,
                    plan = Plans.MOBILE,
                    backgroundId = myNewBg?.id
                )
                while (task.status != "done") {
                    delay(1000)
                    task = getTask(task.id)

                    emitProgress(
                        toProgressComposeValue(task.status)
                    )

                    if (task.status == "error") {
                        errorsServer.value = 1
                        break
                    }

                }
                emitFinished(task = task)
            }
            MediaType.IMAGE -> {
                val myNewBg = createBg(file, mediaType, mimeType)
                var task = createTask(
                    jobId = job.value!!.id,
                    taskType = TypeTask.VIDEO,
                    plan = Plans.MOBILE,
                    backgroundId = myNewBg?.id
                )
                while (task.status != "done") {
                    delay(1000)
                    task = getTask(task.id)

                    emitProgress(
                        toProgressComposeValue(task.status)
                    )

                    if (task.status == "error") {
                        errorsServer.value = 1
                        break
                    }

                }
            }

        }

    }


    private suspend fun emitProgress(progress: Float) {
        _createTaskProgress.emit(
            Result.success(
                RemoveBackgroundProgressStateCompose.Progress(
                    progress
                )
            )
        )
    }

    private suspend fun emitFinished(task: Task) {
        _createTaskProgress.emit(Result.success(RemoveBackgroundProgressStateCompose.Finished(task)))
    }

    private suspend fun safeCall(action: suspend () -> Unit) = try {
        action.invoke()
    } catch (e: Exception) {
        _createTaskProgress.emit(Result.failure(e))
    }

    private fun toProgressComposeValue(value: String): Float {
        return when (value) {
            "queue" -> return 0.1f
            "assigned" -> return 0.3f
            "download" -> return 0.5f
            "in_work" -> return 0.7f
            "upload" -> return 0.9f
            "done" -> return 1.0f
            else -> {
                0.0f
            }
        }

    }

    override val myBg = MutableLiveData<Background>()

    override suspend fun createBg(
        file: File?,
        mediaType: MediaType?,
        mimeType: String?
    ): Background? {
        val filesPart = file?.asRequestBody(contentType = mimeType?.toMediaType())?.let {
            MultipartBody.Part.createFormData(
                name = Constant.FILE_NAME_PART,
                filename = file.name,
                body = it
            )
        }
        val gotBg = filesPart?.let { Network.bgRemApi.createBackground(it) }
        myBg.value = gotBg
        return filesPart?.let { Network.bgRemApi.createBackground(it) }
    }

    override fun downloadResultNewFile(
        params: DownloadResultFileComposeParams,
        fileStorage: FileStorage,
        taskDataSource: TaskDataSource,
        mimeTypeManager: MimeTypeManager,
        directory: File
    ): Flow<Result<File>> = flow {
        val fileSuffix = when (params.mediaType) {
            MediaType.IMAGE -> if (params.isTransparent) {
                mimeTypeManager.getFileExtensionByMimeType(mimeTypeManager.getResultTransparentImageMimeType())
            } else {
                mimeTypeManager.getFileExtensionByMimeType(mimeTypeManager.getResultImageMimeType())
            }
            MediaType.VIDEO -> mimeTypeManager.getFileExtensionByMimeType(mimeTypeManager.getResultVideoMimeType())
        }
        val tempFile = fileStorage.createTempFile(directory, fileSuffix)
        val resultBytes = taskDataSource.downloadResultFile(params.taskId)

        tempFile.outputStream().use {
            it.write(resultBytes)
        }

        emit(Result.success(tempFile))
    }

}