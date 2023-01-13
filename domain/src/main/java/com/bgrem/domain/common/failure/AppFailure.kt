package com.bgrem.domain.common.failure

import java.io.IOException

sealed class AppFailure(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message ?: cause?.message, cause)

// region Backend Failure

sealed class BackendFailure(
    message: String? = null,
    cause: Throwable? = null
) : IOException(message ?: cause?.message, cause)

class CommonBackendFailure(
    message: String? = null,
    cause: Throwable? = null
) : BackendFailure(message, cause)

class NoInternetFailure(
    message: String? = null,
    cause: Throwable? = null
) : BackendFailure(message, cause)

// endregion

// region Files Failure

sealed class FileFailure(
    message: String? = null,
    cause: Throwable? = null
) : AppFailure(message, cause)

class UnsupportedMimeType : FileFailure()

// endregion