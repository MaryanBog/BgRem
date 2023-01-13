package com.compose.data

import java.io.File

interface FileStorages {
    fun createTempCacheFile(suffix: String): File
    fun createTempDirFile(directory: File?, suffix: String): File
}