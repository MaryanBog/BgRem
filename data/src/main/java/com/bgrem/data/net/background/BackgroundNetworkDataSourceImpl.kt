package com.bgrem.data.net.background

import com.bgrem.data.net.common.NetworkConstants
import com.bgrem.domain.background.BackgroundNetworkDataSource
import com.bgrem.domain.background.model.Background
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class BackgroundNetworkDataSourceImpl(
    private val backgroundApi: BackgroundApi
) : BackgroundNetworkDataSource {

    override suspend fun getAllBackgrounds(): List<Background> {
        return backgroundApi.getAllBackgrounds().map { it.toDomain() }
    }

    override suspend fun createBackground(file: File, mimeType: String): Background {
        val body = MultipartBody.Part.createFormData(
            name = NetworkConstants.FILE_PART_NAME,
            filename = file.name,
            body = file.asRequestBody(contentType = mimeType.toMediaType())
        )

        return backgroundApi.createBackground(body).toDomain()
    }
}