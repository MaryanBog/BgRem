package com.bgrem.presentation.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.io.File

object BitmapUtils {
    private const val MIME_TYPE_PNG = "image/png"
    private const val MIME_TYPE_JPG = "image/jpeg"

    private const val DEGREE_90 = 90
    private const val DEGREE_180 = 180
    private const val DEGREE_270 = 270

    fun mimeTypeToBitmapCompressFormat(mimeType: String): Bitmap.CompressFormat = when (mimeType) {
        MIME_TYPE_JPG -> Bitmap.CompressFormat.JPEG
        MIME_TYPE_PNG -> Bitmap.CompressFormat.PNG
        else -> Bitmap.CompressFormat.JPEG
    }

    fun bytesToBitmap(bytes: ByteArray): Bitmap =
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    fun resizeBitmap(bitmap: Bitmap, largeSide: Int, smallerSide: Int): Bitmap {
        val height = bitmap.height
        val width = bitmap.width

        when {
            height >= width -> {
                if (height <= largeSide && width <= smallerSide) return bitmap

                val ratio = smallerSide.toFloat() / width
                val targetWidth = (width * ratio).toInt()
                val targetHeight = (height * ratio).toInt()
                return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
            }
            else -> {
                if (width <= largeSide && height <= smallerSide) return bitmap

                val ratio = smallerSide.toFloat() / height
                val targetWidth = (width * ratio).toInt()
                val targetHeight = (height * ratio).toInt()
                return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
            }
        }
    }

    fun rotateImageIfNeeded(bitmap: Bitmap, file: File): Bitmap {
        val exif = ExifInterface(file.absolutePath)
        return rotateImageIfNeededInternal(bitmap, exif)
    }

    fun rotateImageIfNeeded(bitmap: Bitmap, exifOrientation: Int): Bitmap {
        val degrees = TransformationUtils.getExifOrientationDegrees(exifOrientation)
        return rotateBitmap(bitmap, degrees)
    }

    private fun rotateImageIfNeededInternal(bitmap: Bitmap, exif: ExifInterface): Bitmap {
        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, DEGREE_90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, DEGREE_180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, DEGREE_270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}