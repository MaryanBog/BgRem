package com.bgrem.data.net.common

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RetrofitDataSource {
    fun buildRequestBody(vararg pairs: Pair<String, String?>): RequestBody =
        MultipartBody.Builder().apply {
            setType(MultipartBody.FORM)
            pairs.forEach { pair ->
                pair.second?.let { addFormDataPart(pair.first, it) }
            }
        }.build()
}