package com.compose.utils

interface MimeTypesUtils {
    fun getAvailableImageTypes(): List<String>
    fun getAvailableVideoTypes(): List<String>
    fun getFileExtensionImageType(mimeType: String): String
    fun getFileExtensionVideoType(mimeType: String): String
    fun getMimeTypeForNewImage(): String
    fun getMimeTypeForNewVideo(): String
}