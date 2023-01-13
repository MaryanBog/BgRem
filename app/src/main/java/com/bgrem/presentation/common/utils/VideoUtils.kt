package com.bgrem.presentation.common.utils

import android.content.res.AssetFileDescriptor
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.media.MediaMuxer
import android.util.Log
import com.bgrem.presentation.common.VideoDurationResult
import com.bgrem.presentation.trimming.thumbnail.OnCommandVideoListener
import java.io.FileDescriptor
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

object VideoUtils {

    fun isVideoValidByDuration(
        fileDescriptor: FileDescriptor,
        maxDurationInSeconds: Int
    ): VideoDurationResult {
        val retriever = try {
            MediaMetadataRetriever().apply {
                setDataSource(fileDescriptor)
            }
        } catch (e: Exception) {
            null
        }
        val durationInSec = retriever
            ?.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            ?.toLong()
            ?.let { TimeUnit.MILLISECONDS.toSeconds(it).toInt() }

        retriever?.release()

        return when {
            durationInSec == null -> VideoDurationResult.ERROR
            durationInSec <= maxDurationInSeconds -> VideoDurationResult.FIT
            else -> VideoDurationResult.TOO_LONG
        }
    }

    fun startTrim(
        assetFileDescriptor: AssetFileDescriptor,
        destAssetFileDescriptor: AssetFileDescriptor,
        startMs: Int,
        endMs: Int,
        listener: OnCommandVideoListener?,
        errorString: String
    ) {
        try {
            /** Set up MediaExtractor to read from the source. */
            val extractor = MediaExtractor()
            extractor.setDataSource(assetFileDescriptor)
            val trackCount = extractor.trackCount

            val fileDescriptor = destAssetFileDescriptor.fileDescriptor

            /** Set up MediaMuxer for the destination. */
            val muxer = MediaMuxer(fileDescriptor, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

            /** Set up the tracks and retrieve the max buffer size for selected tracks. */
            val indexMap = HashMap<Int, Int>(trackCount)
            var bufferSize = -1
            for (i in 0 until trackCount) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                var selectCurrentTrack = false
                if (mime!!.startsWith("audio/")) {
                    selectCurrentTrack = true
                } else if (mime.startsWith("video/")) {
                    selectCurrentTrack = true
                }
                if (selectCurrentTrack) {
                    extractor.selectTrack(i)
                    val dstIndex = muxer.addTrack(format)
                    indexMap[i] = dstIndex
                    if (format.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
                        val newSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
                        bufferSize = if (newSize > bufferSize) newSize else bufferSize
                    }
                }
            }
            if (bufferSize < 0) {
                bufferSize = DEFAULT_BUFFER_SIZE
            }
            /** Set up the orientation and starting time for extractor. */
            val retrieverSrc = MediaMetadataRetriever()
            retrieverSrc.setDataSource(assetFileDescriptor.fileDescriptor)
            val degreesString = retrieverSrc.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION
            )
            if (degreesString != null) {
                val degrees = degreesString.toInt()
                if (degrees >= 0) {
                    muxer.setOrientationHint(degrees)
                }
            }
            if (startMs > 0) {
                extractor.seekTo((startMs * 1000).toLong(), MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            }
            /** Copy the samples from MediaExtractor to MediaMuxer. We will loop
            for copying each sample and stop when we get to the end of the source
            file or exceed the end time of the trimming. */
            val offset = 0
            var trackIndex: Int
            val dstBuf = ByteBuffer.allocate(bufferSize)
            val bufferInfo = MediaCodec.BufferInfo()
            try {
                muxer.start()
                while (true) {
                    bufferInfo.offset = offset
                    bufferInfo.size = extractor.readSampleData(dstBuf, offset)
                    if (bufferInfo.size < 0) {
                        bufferInfo.size = 0
                        break
                    } else {
                        bufferInfo.presentationTimeUs = extractor.sampleTime
                        if (endMs > 0 && bufferInfo.presentationTimeUs > endMs * 1000) {
                            break
                        } else {
                            bufferInfo.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME
                            trackIndex = extractor.sampleTrackIndex
                            muxer.writeSampleData(
                                indexMap[trackIndex]!!, dstBuf,
                                bufferInfo
                            )
                            extractor.advance()
                        }
                    }
                }
                muxer.stop()
                listener?.onProgress()
                muxer.release()
            } catch (e: IllegalStateException) {
                listener?.onError()
            }
            return
        } catch (e: Throwable) {
            listener?.onError()
            Log.e("ThumbnailFragment", "error = ${e.stackTraceToString()}")
        }
    }
}

