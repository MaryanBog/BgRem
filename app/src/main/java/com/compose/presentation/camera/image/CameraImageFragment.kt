package com.compose.presentation.camera.image

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.bumptech.glide.load.ImageHeaderParser
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.CameraImageScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraImageFragment : Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()
    private val cameraImageViewModel: CameraImageViewModel by viewModels()

    private val cameraProvider by lazy { ProcessCameraProvider.getInstance(requireContext()) }
    private val mainExecutor by lazy { ContextCompat.getMainExecutor(requireContext()) }
    private val imageCapture by lazy { ImageCapture.Builder().build() }
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val cameraProviderCallback = Runnable {
        cameraImageViewModel.currentCamera.value
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                startCamera()
            } else {
                showError(
                    resources.getString(
                        R.string.common_error_no_granted_write_storage_permission
                    )
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            lifecycleScope.launchWhenStarted {
                removeViewModel.visibilityPhotoVideoButton.collect{ visible ->
                    removeViewModel.enableNavButton.collect { enable ->
                        val navController = findNavController()
                        setContent {
                            BgRemTheme {
                                CameraImageScreen(
                                    navController = navController,
                                    cameraProvider = cameraProvider,
                                    cameraImageViewModel = cameraImageViewModel,
                                    removeViewModel = removeViewModel,
                                    imageCapture = imageCapture,
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
        checkPermissions()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            cameraImageViewModel.cameraImageActionState.collect { action ->
                when (action) {
                    CameraImageAction.Start -> {}
                    CameraImageAction.Error -> {
                        showError(
                            resources.getString(
                                R.string.common_error_something_went_wrong
                            )
                        )
                    }
                    CameraImageAction.TakePhoto -> {
                        takePhoto()
                    }
                }
            }
        }
    }

    private fun takePhoto() {
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(cameraImageViewModel.mediaFile).build()
        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        removeViewModel.onPreviewPhoto(it)
                        lifecycleScope.launch(Dispatchers.Main) {
                            findNavController().navigate(
                                R.id.action_cameraImageFragment_to_previewImageFragment
                            )
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            })
    }

    private fun checkPermissions() {
        val isAllGranted = REQUEST_PERMISSIONS.all { permissions ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permissions
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            if (isAllGranted) {
                startCamera()
            } else {
                cameraLauncher.launch(REQUEST_PERMISSIONS)
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

    companion object {
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider.isCancelled
    }
}