package com.compose.data.storage

import com.compose.data.FileStorages
import java.io.File
import java.time.Instant


class FileStoragesImpl(
    private val cacheDir: File
): FileStorages {

    override fun createTempCacheFile(suffix: String): File {
        return File.createTempFile(Instant.now().epochSecond.toString(), suffix, cacheDir)
    }

    override fun createTempDirFile(directory: File?, suffix: String): File {
        return File.createTempFile(Instant.now().epochSecond.toString(), suffix, directory)
    }
}