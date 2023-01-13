package com.compose.presentation.camera.video

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
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
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.bgrem.presentation.trimming.TrimmingContract
import com.bumptech.glide.load.ImageHeaderParser
import com.compose.presentation.gallery.video.VideoAction
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.CameraVideoScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraVideoFragment : Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()
    private val cameraVideoViewModel: CameraVideoViewModel by viewModels()

    private val cameraProvider by lazy { ProcessCameraProvider.getInstance(requireContext()) }
    private val mainExecutor by lazy { ContextCompat.getMainExecutor(requireContext()) }
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val recorder = Recorder.Builder()
        .setExecutor(cameraExecutor)
        .setQualitySelector(
            QualitySelector.from(
                Quality.FHD,
                FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
            )
        ).build()
    private var videoCapture: VideoCapture<Recorder> = VideoCapture.withOutput(recorder)
    private var recording: Recording? = null
    private val cameraProviderCallback = Runnable {
        cameraVideoViewModel.currentCamera.value
    }

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

                videoCapture.targetRotation = rotation
            }
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (isGranted[Manifest.permission.CAMERA] == true) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                startCamera()
            } else {
                showError(
                    resources.getString(
                        R.string.common_error_no_granted_write_storage_permission
                    )
                )
            }
        }

    private val requestWritePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                checkPermissionWriteExternalStorage()
            }
        }

    private val trimming = registerForActivityResult(TrimmingContract()) { uri ->
        uri ?: findNavController().popBackStack()
        handleSelectedVideo(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            lifecycleScope.launchWhenStarted {
                removeViewModel.visibilityPhotoVideoButton.collect { visible ->
                    removeViewModel.enableNavButton.collect { enable ->
                        val navController = findNavController()
                        setContent {
                            BgRemTheme {
                                CameraVideoScreen(
                                    navController = navController,
                                    cameraVideoViewModel = cameraVideoViewModel,
                                    removeViewModel = removeViewModel,
                                    visible = visible,
                                    enable = enable
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraLauncher.launch(
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
        )

        initCamera()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            cameraVideoViewModel.previewView.collect { preview ->
                cameraVideoViewModel.currentCamera.collect { cameraSelector ->
                    bindCamera(preview, cameraSelector)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        orientationEventListener.enable()
    }

    override fun onPause() {
        super.onPause()
        orientationEventListener.disable()
    }

    private fun initCamera(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            removeViewModel.videoActionState.collect { action ->
                when (action) {
                    VideoAction.StartVideo -> {}
                    VideoAction.Error -> {
                        showError(
                            resources.getString(
                                R.string.common_error_something_went_wrong
                            )
                        )
                    }
                    is VideoAction.VideoSelected -> {
                        removeViewModel.onMediaSelected(
                            file = action.file,
                            mediaType = action.mediaType,
                            mimeType = action.mimeType,
                            uri = action.uri
                        )

                        removeViewModel.observeRemovingBackgroundProgress()
                        removeViewModel.removeBg()
                        findNavController().navigate(
                            R.id.action_cameraVideoFragment_to_loadingComposeFragment
                        )
                    }
                    is VideoAction.TruncateVideo -> {
                        onVideoSaved(action.uri)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            cameraVideoViewModel.cameraVideoActionState.collect { action ->
                when (action) {
                    CameraVideoAction.Start -> {}
                    CameraVideoAction.Error -> {
                        showError(
                            resources.getString(
                                R.string.common_error_something_went_wrong
                            )
                        )
                    }
                    CameraVideoAction.RecordVideo -> {
                        recordVideo()
                    }
                    CameraVideoAction.StopVideo -> {
                        recording?.stop()
                        removeViewModel.onTakeVideo(cameraVideoViewModel.mediaFile.toUri())
                    }
                }
            }
        }
    }

    private fun onVideoSaved(uri: Uri) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(TIME_MILLIS)
            trimming.launch(uri)
        }
    }

    private fun handleSelectedVideo(uri: Uri?) = try {
        with(requireContext()) {
            val fileStream = uri?.let { contentResolver.openInputStream(it) }
            val mimeType = uri?.let { contentResolver.getType(it) }
            removeViewModel.onVideoSelected(fileStream, mimeType, uri)
        }
    } catch (_: Exception) {
        showError(resources.getString(R.string.common_error_something_went_wrong))
    }

    private fun bindCamera(preview: Preview?, cameraSelector: CameraSelector) {
        val provider = cameraProvider.get()
        val recorder = Recorder.Builder()
            .setExecutor(cameraExecutor)
            .setQualitySelector(
                QualitySelector.from(
                    Quality.FHD,
                    FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
                )
            ).build()

        val currentUseCase = VideoCapture.withOutput(recorder).also { videoCapture = it }

        provider.unbindAll()
        try {
            provider.bindToLifecycle(
                this,
                cameraSelector,
                currentUseCase,
                preview
            )
        } catch (_: Exception) {
            showError(
                requireContext().resources.getString(
                    R.string.common_error_something_went_wrong
                )
            )
        }
    }

    private fun recordVideo() {
        val outputOptions = FileOutputOptions.Builder(cameraVideoViewModel.mediaFile).build()
        recording = videoCapture.output
            .prepareRecording(requireContext(), outputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(cameraExecutor) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        cameraVideoViewModel.onVideoRecordingIndicator(
                            recordEvent.recordingStats.recordedDurationNanos
                        )
                    }
                    is VideoRecordEvent.Status -> {
                        cameraVideoViewModel.onVideoRecordingIndicator(
                            recordEvent.recordingStats.recordedDurationNanos
                        )
                    }
                }
            }
    }

    private fun checkPermissionWriteExternalStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                showError(
                    resources.getString(
                        R.string.common_error_no_granted_write_storage_permission
                    )
                )
                requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun startCamera() {
        cameraProvider.addListener(
            cameraProviderCallback,
            mainExecutor
        )
    }

    private fun showError(message: String) {
        removeViewModel.onErrorShow(message)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider.isCancelled
    }

    companion object{
        private const val TIME_MILLIS = 2000L
    }
}