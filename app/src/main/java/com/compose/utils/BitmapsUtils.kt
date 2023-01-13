package com.compose.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import com.bgrem.presentation.common.utils.BitmapUtils
import java.io.File

object BitmapsUtils {
    private const val MIME_TYPE_PNG = "image/png"
    private const val MIME_TYPE_JPG = "image/jpeg"

    private const val DEGREE_90 = 90
    private const val DEGREE_180 = 180
    private const val DEGREE_270 = 270

    fun mimeTypeCompressFormat(mimeType: String): Bitmap.CompressFormat = when (mimeType) {
        MIME_TYPE_JPG -> Bitmap.CompressFormat.JPEG
        MIME_TYPE_PNG -> Bitmap.CompressFormat.PNG
        else -> Bitmap.CompressFormat.JPEG
    }

    fun rotateImageIfNeeded(bitmap: Bitmap, file: File): Bitmap {
        val exif = ExifInterface(file.absolutePath)
        return rotateImageIfNeededInternal(bitmap, exif)
    }

    private fun rotateImageIfNeededInternal(bitmap: Bitmap, exif: ExifInterface): Bitmap {
        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(
                bitmap,
                DEGREE_90
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(
                bitmap,
                DEGREE_180
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(
                bitmap,
                DEGREE_270
            )
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}