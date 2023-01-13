package com.bgrem.presentation.media.capture

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bgrem.app.R
import com.bgrem.app.databinding.FramgentCaptureBinding
import com.bgrem.domain.common.media.MediaType
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getIconRes
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.bgrem.presentation.media.capture.model.CameraAction
import com.bgrem.presentation.media.capture.model.CaptureAction
import com.bgrem.presentation.media.capture.model.CaptureFragmentListener
import com.bgrem.presentation.media.capture.model.CaptureState
import com.bumptech.glide.load.ImageHeaderParser
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.concurrent.Executors

class CaptureFragment : Fragment(R.layout.framgent_capture) {

    private var _binding: FramgentCaptureBinding? = null
    private val binding get() = _binding!!

    private val listener by lazy { getParentAsListener<CaptureFragmentListener>() }
    private val viewModel by viewModel<CaptureViewModel> {
        parametersOf(getExtra(KEY_MEDIA_TYPE))
    }

    private val cameraProvider by lazy { ProcessCameraProvider.getInstance(requireContext()) }
    private val imageCapture by lazy { ImageCapture.Builder().build() }
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val mainExecutor by lazy { ContextCompat.getMainExecutor(requireContext()) }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(requireContext()) {
            @SuppressLint("RestrictedApi")
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ImageHeaderParser.UNKNOWN_ORIENTATION) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture.targetRotation = rotation
                videoCapture?.targetRotation = rotation
            }
        }
    }

    private val cameraProviderCallback = Runnable {
        viewModel.onCameraReady(CameraSelector.DEFAULT_FRONT_CAMERA)
    }

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FramgentCaptureBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        orientationEventListener.enable()
    }

    override fun onPause() {
        super.onPause()
        orientationEventListener.disable()
    }

    private fun initView() = with(binding) {
        cameraProvider.addListener(
            cameraProviderCallback,
            mainExecutor
        )

        cameraActionImage.setOnClickListener {
            when (viewModel.state.value?.currentAction) {
                CameraAction.TAKE_PHOTO -> takePhoto()
                CameraAction.TAKE_VIDEO -> recordVideo()
                CameraAction.STOP -> viewModel.onStopRecordingClicked()
                else -> Unit
            }
        }

        cameraSwitchImage.setOnClickListener {
            viewModel.switchCameraLens()
        }
    }

    private fun observeViewModel() = with(viewModel) {
        state.observe(viewLifecycleOwner) { state ->
            updateState(state)
        }
        event.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                handleAction(it)
            }
        }
        currentCameraLens.observe(viewLifecycleOwner) { lens ->
            bindCamera(lens)
        }
        currentVideoRecordingDuration.observe(viewLifecycleOwner) { duration ->
            binding.recordingVideoDurationText.text =
                getString(R.string.capture_media_recording_duration, duration)
        }
    }

    private fun updateState(state: CaptureState) = with(binding) {
        cameraActionImage.setImageResource(state.currentAction.getIconRes())
        recordingVideoDurationText.isVisible = state.currentAction == CameraAction.STOP
        cameraSwitchImage.isVisible = state.currentAction != CameraAction.STOP
    }

    private fun handleAction(action: CaptureAction) = when (action) {
        is CaptureAction.StopVideoRecording -> {
            recording?.stop()
            onVideoSaved()
        }
        is CaptureAction.OnVideoRecordingStopped -> onVideoSaved()
    }

    private fun onVideoSaved(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(DELAY_MILLIS)
            listener.onVideoSaved(viewModel.mediaFile.toUri())
        }
    }

    private fun bindCamera(cameraSelector: CameraSelector) {
        val provider = cameraProvider.get()
        val preview = Preview.Builder().build()
            .also { it.setSurfaceProvider(binding.cameraPreviewView.surfaceProvider) }
        val recorder = Recorder.Builder()
            .setExecutor(cameraExecutor)
            .setQualitySelector(
                QualitySelector.from(
                    Quality.FHD,
                    FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
                )
            ).build()

        val currentUseCase = when (viewModel.mediaType) {
            MediaType.IMAGE -> imageCapture
            MediaType.VIDEO -> VideoCapture.withOutput(recorder).also { videoCapture = it }
        }

        provider.unbindAll()
        try {
            provider.bindToLifecycle(this, cameraSelector, currentUseCase, preview)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun takePhoto() {
        val outputOptions = ImageCapture.OutputFileOptions.Builder(viewModel.mediaFile).build()
        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { listener.onImageSaved(it) }
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            })
    }

    private fun recordVideo() {
        viewModel.onStartVideoRecording()
        val outputOptions = FileOutputOptions.Builder(viewModel.mediaFile).build()
        recording = videoCapture?.output
            ?.prepareRecording(requireContext(), outputOptions)
            ?.apply {
                if (PermissionChecker.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            ?.start(cameraExecutor) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Finalize -> viewModel.onVideoRecordingStopped()
                    is VideoRecordEvent.Status -> viewModel.updateCurrentVideoRecordingDuration(
                        recordEvent.recordingStats.recordedDurationNanos
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider.get().unbindAll()
        _binding = null
    }

    companion object {
        val TAG: String = CaptureFragment::class.java.simpleName

        private const val KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE"
        private const val DELAY_MILLIS = 1000L

        fun newInstance(mediaType: MediaType) = CaptureFragment().apply {
            arguments = bundleOf(KEY_MEDIA_TYPE to mediaType)
        }
    }
}