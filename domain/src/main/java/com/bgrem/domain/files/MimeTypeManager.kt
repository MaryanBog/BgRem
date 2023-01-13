package com.bgrem.domain.files

interface MimeTypeManager {
    fun getAvailableImageMimeTypes(): List<String>
    fun getAvailableVideoMimeTypes(): List<String>
    fun getFileExtensionByMimeType(mimeType: String): String
    fun getMimeTypeForNewImage(): String
    fun getMimeTypeForNewVideo(): String
    fun getMimeTypeForVideoThumbnail(): String
    fun getResultTransparentImageMimeType(): String
    fun getResultImageMimeType(): String
    fun getResultVideoMimeType(): String
}