package com.bgrem.data.net.common.failure

import com.bgrem.domain.common.failure.CommonBackendFailure
import com.bgrem.domain.common.failure.NoInternetFailure
import okhttp3.Response
import java.net.UnknownHostException

interface StatusCodesHandler {
    fun handleResponse(response: Response?)
    fun handleException(exception: Exception)
}

class StatusCodesHandlerImpl : StatusCodesHandler {
    override fun handleResponse(response: Response?) = when (response?.code) {
        in 200..300 -> Unit
        else -> throw CommonBackendFailure()
    }

    override fun handleException(exception: Exception) =
        throw if (exception is UnknownHostException) NoInternetFailure() else exception
}