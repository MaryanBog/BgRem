package com.bgrem.data.files

import com.bgrem.domain.files.FileStorage
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.time.Instant

class FileStorageImpl(
    private val cacheDir: File
) : FileStorage {

    override fun createTempCacheFile(suffix: String): File {
        return createTempFile(cacheDir, suffix)
    }

    override fun createTempFile(directory: File, suffix: String): File {
        return File.createTempFile(Instant.now().epochSecond.toString(), suffix, directory)
    }

    override fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        val bytes = inputStream.use { it.readBytes() }
        Timber.d("Read from file ${bytes.size} bytes")
        copyBytesToFile(bytes, file)
    }

    override fun copyBytesToFile(bytes: ByteArray, file: File) {
        Timber.d("Bytes for write ${bytes.size}")
        file.outputStream().use { it.write(bytes) }
    }

    override fun deleteFile(file: File) {
        if (file.exists()) {
            val isDeleteSuccessful = file.delete()
            Timber.d("File ${file.name} was deleted: $isDeleteSuccessful")
        }
    }

    override fun deleteFile(filePath: String) {
        val file = File(filePath)
        deleteFile(file)
    }

    override fun clearCache() {
        clearDirectory(cacheDir)
    }

    override fun clearDirectory(directory: File) {
        directory.listFiles()?.forEach { file -> if (file.isFile) deleteFile(file) }
    }
}