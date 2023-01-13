package com.bgrem.managers

import android.content.Context
import com.bgrem.app.R
import com.bgrem.domain.common.failure.UnsupportedMimeType
import com.bgrem.domain.files.MimeTypeManager

class MimeTypeManagerImpl(
    private val context: Context
) : MimeTypeManager {

    private val availableImageMimeTypesWithExtension: Map<String, String>
        get() = with(context) {
            mapOf(
                getString(R.string.mime_type_image_jpeg) to getString(R.string.file_extensions_jpg),
                getString(R.string.mime_type_image_png) to getString(R.string.file_extensions_png)
            )
        }

    private val availableVideoMimeTypesWithExtension: Map<String, String>
        get() = with(context) {
            mapOf(
                getString(R.string.mime_type_video_mp4) to getString(R.string.file_extensions_mp4),
                getString(R.string.mime_type_video_3GP) to getString(R.string.file_extensions_3gp)
//                getString(R.string.mime_type_video_matroska) to getString(R.string.file_extensions_mkv),
//                getString(R.string.mime_type_video_av) to getString(R.string.file_extensions_avi),
//                getString(R.string.mime_type_video_iphone_segment) to getString(R.string.file_extensions_ts),
//                getString(R.string.mime_type_video_mpeg) to getString(R.string.file_extensions_mpg),
//                getString(R.string.mime_type_video_quicktime) to getString(R.string.file_extensions_mov),
//                getString(R.string.mime_type_video_webm) to getString(R.string.file_extensions_webm),

            )
        }

    private val allMimeTypesWithExtension: Map<String, String>
        get() = availableImageMimeTypesWithExtension + availableVideoMimeTypesWithExtension

    override fun getAvailableImageMimeTypes(): List<String> =
        availableImageMimeTypesWithExtension.keys.toList()

    override fun getAvailableVideoMimeTypes(): List<String> =
        availableVideoMimeTypesWithExtension.keys.toList()

    @Throws(UnsupportedMimeType::class)
    override fun getFileExtensionByMimeType(mimeType: String): String =
        allMimeTypesWithExtension[mimeType] ?: throw UnsupportedMimeType()

    override fun getMimeTypeForNewImage(): String = context.getString(R.string.mime_type_image_jpeg)

    override fun getMimeTypeForNewVideo(): String = context.getString(R.string.mime_type_video_mp4)

    override fun getMimeTypeForVideoThumbnail(): String =
        context.getString(R.string.mime_type_image_jpeg)

    override fun getResultTransparentImageMimeType(): String =
        context.getString(R.string.mime_type_image_png)

    override fun getResultImageMimeType(): String = context.getString(R.string.mime_type_image_jpeg)

    override fun getResultVideoMimeType(): String = context.getString(R.string.mime_type_video_mp4)
}