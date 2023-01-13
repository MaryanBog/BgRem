package com.bgrem.data.net.common.failure

import okhttp3.Interceptor
import okhttp3.Response

class FailureInterceptor(
    private val statusCodesHandler: StatusCodesHandler
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null

        return try {
            response = chain.proceed(request)
            statusCodesHandler.handleResponse(response)
            response
        } catch (e: Exception) {
            response?.close()
            statusCodesHandler.handleException(e)
            chain.proceed(request)
        }
    }
}