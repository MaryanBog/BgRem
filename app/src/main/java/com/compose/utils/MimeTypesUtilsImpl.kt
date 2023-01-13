package com.compose.utils

import android.content.Context
import com.bgrem.app.R
import okio.IOException

class MimeTypesUtilsImpl(
    private val context: Context
) : MimeTypesUtils {

    private val availableImageTypesExtension: Map<String, String>
        get() = with(context) {
            mapOf(
                getString(R.string.mime_type_image_jpeg) to getString(R.string.file_extensions_jpg),
                getString(R.string.mime_type_image_png) to getString(R.string.file_extensions_png)
            )
        }

    private val availableVideoTypesExtension: Map<String, String>
        get() = with(context) {
            mapOf(
                getString(R.string.mime_type_video_mp4) to getString(R.string.file_extensions_mp4),
                getString(R.string.mime_type_video_3GP) to getString(R.string.file_extensions_3gp)
            )
        }

    override fun getAvailableImageTypes(): List<String> =
        availableImageTypesExtension.keys.toList()

    override fun getAvailableVideoTypes(): List<String> =
        availableVideoTypesExtension.keys.toList()

    override fun getFileExtensionImageType(mimeType: String): String =
        availableImageTypesExtension[mimeType] ?: throw IOException()

    override fun getFileExtensionVideoType(mimeType: String): String =
        availableVideoTypesExtension[mimeType] ?: throw IOException()

    override fun getMimeTypeForNewImage(): String = context.getString(R.string.mime_type_image_jpeg)

    override fun getMimeTypeForNewVideo(): String = context.getString(R.string.mime_type_video_mp4)
}