package com.bgrem.presentation.trimming.thumbnail

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.VideoView
import com.bgrem.app.R
import com.bgrem.presentation.common.utils.VideoUtils
import java.io.FileDescriptor
import java.lang.ref.WeakReference

class VideoTrimmer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var maxDuration: Int = -1
    private var minDuration: Int = -1
    private var duration = 0f
    private var commandVideoListener: OnCommandVideoListener? = null
    private var progressListener: ArrayList<OnProgressVideoListener> = ArrayList()
    private var mTimeVideo = 0f
    private var startPos = 0f
    private var endPos = 0f
    private var resetBar = true
    private val messageHandler = MessageHandler(this)

    private val videoLoader: VideoView
    private val handlerTop: SeekBar
    private val timeLineBar: RangeSeekBarView
    private val iconVideoPlay: ImageView
    private val timeLineView: VideoPreviewView
    private val layoutSurfaceView: RelativeLayout

    init {
        val root = inflate(context, R.layout.view_trimmer, this)
        videoLoader = root.findViewById(R.id.video_loader)
        handlerTop = root.findViewById(R.id.handlerTop)
        timeLineBar = root.findViewById(R.id.timeLineBar)
        iconVideoPlay = root.findViewById(R.id.icon_video_play)
        timeLineView = root.findViewById(R.id.timeLineView)
        layoutSurfaceView = root.findViewById(R.id.layout_surface_view)
        setUpListeners()
        setUpMargins()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListeners() {
        timeLineBar.addOnRangeSeekBarListener(object : OnRangeSeekBarListener {
            override fun onCreate(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
            }

            override fun onSeek(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
                handlerTop.visibility = View.GONE
                onSeekThumbs(index, value)
            }

            override fun onSeekStart(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
            }

            override fun onSeekStop(rangeSeekBarView: RangeSeekBarView, index: Int, value: Float) {
                onStopSeekThumbs()
            }
        })

        progressListener = ArrayList()
        progressListener.add(object : OnProgressVideoListener {
            override fun updateProgress(time: Float, max: Float, scale: Float) {
                updateVideoProgress(time)
            }
        })

        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    togglePause()
                    return true
                }
            })

//        videoLoader.setOnErrorListener { _, _, _ ->
//            commandVideoListener?.onError(resources.getString(R.string.common_error_something_went_wrong))
//            false
//        }

        videoLoader.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        handlerTop.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                onPlayerIndicatorSeekChanged(progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                onPlayerIndicatorSeekStart()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                onPlayerIndicatorSeekStop(seekBar)
            }
        })

        videoLoader.setOnPreparedListener { mp -> onVideoPrepared(mp) }
        videoLoader.setOnCompletionListener { onVideoCompleted() }
    }

    private fun onPlayerIndicatorSeekChanged(progress: Int, fromUser: Boolean) {
        val duration = (duration * progress / DURATION_1000_LONG)
        if (fromUser) {
            if (duration < startPos) setProgressBarPosition(startPos)
            else if (duration > endPos) setProgressBarPosition(endPos)
        }
    }

    private fun onPlayerIndicatorSeekStart() {
        messageHandler.removeMessages(SHOW_PROGRESS)
        videoLoader.pause()
        iconVideoPlay.visibility = View.VISIBLE
        notifyProgressUpdate(false)
    }

    private fun onPlayerIndicatorSeekStop(seekBar: SeekBar) {
        messageHandler.removeMessages(SHOW_PROGRESS)
        videoLoader.pause()
        iconVideoPlay.visibility = View.VISIBLE

        val duration = (duration * seekBar.progress / DURATION_1000_LONG).toInt()
        videoLoader.seekTo(duration)
        notifyProgressUpdate(false)
    }

    private fun setProgressBarPosition(position: Float) {
        if (duration > 0) handlerTop.progress = (DURATION_1000_LONG * position / duration).toInt()
    }

    private fun setUpMargins() {
        val marge = timeLineBar.thumbs[0].widthBitmap
        val lp = timeLineView.layoutParams as LayoutParams
        lp.setMargins(marge, 0, marge, 0)
        timeLineView.layoutParams = lp
    }

    fun save(
        assetFileDescriptor: AssetFileDescriptor?,
        destAssetFileDescriptor: AssetFileDescriptor?
    ) {
        iconVideoPlay.visibility = View.VISIBLE
        videoLoader.pause()

        if (assetFileDescriptor != null && destAssetFileDescriptor != null)
            VideoUtils.startTrim(
                assetFileDescriptor = assetFileDescriptor,
                destAssetFileDescriptor = destAssetFileDescriptor,
                startMs = startPos.toInt(),
                endMs = endPos.toInt(),
                listener = commandVideoListener,
                errorString = resources.getString(R.string.common_error_something_went_wrong)
            )
    }

    private fun togglePause() {
        if (videoLoader.isPlaying) {
            iconVideoPlay.visibility = View.VISIBLE
            messageHandler.removeMessages(SHOW_PROGRESS)
            videoLoader.pause()
        } else {
            iconVideoPlay.visibility = View.GONE
            if (resetBar) {
                resetBar = false
                videoLoader.seekTo(startPos.toInt())
            }
            messageHandler.sendEmptyMessage(SHOW_PROGRESS)
            videoLoader.start()
        }
    }

    private fun onVideoPrepared(mp: MediaPlayer) {
        val videoWidth = mp.videoWidth
        val videoHeight = mp.videoHeight
        val videoProportion = videoWidth.toFloat() / videoHeight.toFloat()
        val screenWidth = layoutSurfaceView.width
        val screenHeight = layoutSurfaceView.height
        val screenProportion = screenWidth.toFloat() / screenHeight.toFloat()
        val lp = videoLoader.layoutParams

        if (videoProportion > screenProportion) {
            lp.width = screenWidth
            lp.height = (screenWidth.toFloat() / videoProportion).toInt()
        } else {
            lp.width = (videoProportion * screenHeight.toFloat()).toInt()
            lp.height = screenHeight
        }
        videoLoader.layoutParams = lp

        iconVideoPlay.visibility = View.VISIBLE

        duration = videoLoader.duration.toFloat()
        setSeekBarPosition()
    }

    private fun setSeekBarPosition() {
        when {
            duration >= maxDuration && maxDuration != -1 -> {
                startPos = duration / 2 - maxDuration / 2
                endPos = duration / 2 + maxDuration / 2
                timeLineBar.setThumbValue(0, (startPos * NUMBER_100 / duration))
                timeLineBar.setThumbValue(1, (endPos * NUMBER_100 / duration))
            }
            duration <= minDuration && minDuration != -1 -> {
                startPos = duration / 2 - minDuration / 2
                endPos = duration / 2 + minDuration / 2
                timeLineBar.setThumbValue(0, (startPos * NUMBER_100 / duration))
                timeLineBar.setThumbValue(1, (endPos * NUMBER_100 / duration))
            }
            else -> {
                startPos = 0f
                endPos = duration
            }
        }
        videoLoader.seekTo(startPos.toInt())
        mTimeVideo = duration
        timeLineBar.initMaxWidth()
    }

    private fun onSeekThumbs(index: Int, value: Float) {
        when (index) {
            Thumb.LEFT -> {
                startPos = (duration * value / 100L)
                videoLoader.seekTo(startPos.toInt())
            }
            Thumb.RIGHT -> {
                endPos = (duration * value / 100L)
            }
        }
        mTimeVideo = endPos - startPos
    }

    private fun onStopSeekThumbs() {
        messageHandler.removeMessages(SHOW_PROGRESS)
        videoLoader.pause()
        iconVideoPlay.visibility = View.VISIBLE
    }

    private fun onVideoCompleted() {
        videoLoader.seekTo(startPos.toInt())
    }

    private fun notifyProgressUpdate(all: Boolean) {
        if (duration == 0f) return
        val position = videoLoader.currentPosition
        if (all) {
            for (item in progressListener) {
                item.updateProgress(position.toFloat(), duration, (position * NUMBER_100 / duration))
            }
        } else {
            progressListener[0].updateProgress(
                position.toFloat(),
                duration,
                (position * NUMBER_100 / duration)
            )
        }
    }

    private fun updateVideoProgress(time: Float) {
        if (time <= startPos && time <= endPos) handlerTop.visibility = View.GONE
        else handlerTop.visibility = View.VISIBLE
        if (time >= endPos - 300) {
            messageHandler.removeMessages(SHOW_PROGRESS)
            videoLoader.pause()
            iconVideoPlay.visibility = View.VISIBLE
            resetBar = true
            return
        }
        setProgressBarPosition(time)
    }

    fun setOnCommandListener(commandVideoListener: OnCommandVideoListener): VideoTrimmer {
        this.commandVideoListener = commandVideoListener
        return this
    }

    fun setMaxDuration(maxDuration: Int): VideoTrimmer {
        this.maxDuration = maxDuration * DURATION_1000
        return this
    }

    fun setMinDuration(minDuration: Int): VideoTrimmer {
        this.minDuration = minDuration * DURATION_1000
        return this
    }

    fun setVideoURI(videoURI: Uri): VideoTrimmer {
        videoLoader.setVideoURI(videoURI)
        videoLoader.requestFocus()
        timeLineView.setVideo(videoURI)
        return this
    }

    private class MessageHandler(view: VideoTrimmer) : Handler() {
        private val viewReference: WeakReference<VideoTrimmer> = WeakReference(view)
        override fun handleMessage(msg: Message) {
            val view = viewReference.get()
            if (view?.videoLoader == null) {
                return
            }
            view.notifyProgressUpdate(true)
            if (view.videoLoader.isPlaying) {
                sendEmptyMessageDelayed(0, DELAY_MILLIS)
            }
        }
    }

    companion object {
        private const val SHOW_PROGRESS = 2
        private const val DURATION_1000_LONG = 1000L
        private const val DURATION_1000 = 1000
        private const val NUMBER_100 = 100
        private const val DELAY_MILLIS = 10L
    }
}
