package com.bgrem.domain.files

import java.io.File
import java.io.InputStream

interface FileStorage {
    fun createTempFile(directory: File, suffix: String): File
    fun createTempCacheFile(suffix: String): File
    fun copyInputStreamToFile(inputStream: InputStream, file: File)
    fun copyBytesToFile(bytes: ByteArray, file: File)
    fun deleteFile(file: File)
    fun deleteFile(filePath: String)
    fun clearCache()
    fun clearDirectory(directory: File)
}