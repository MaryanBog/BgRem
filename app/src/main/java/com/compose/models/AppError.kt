package com.compose.models

import java.io.IOException
import java.sql.SQLException

sealed class AppError(val code: Int, val info: String): RuntimeException(info) {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetWorkException
            else -> UnknownException
        }
    }
}

class ApiException(code: Int, message: String) : AppError(code, message)

object NetWorkException: AppError(-1, ("_no_internet").toString())
object UnknownException: AppError(-1, ("something_went_wrong").toString())
object DbError : AppError(-1, "error_db")