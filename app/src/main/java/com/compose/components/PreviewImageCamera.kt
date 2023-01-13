package com.compose.components

import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.bgrem.app.R
import com.compose.presentation.camera.image.CameraImageViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PreviewImageCamera(
    cameraProvider: ListenableFuture<ProcessCameraProvider>,
    removeViewModel: RemoveViewModel,
    cameraImageViewModel: CameraImageViewModel,
    imageCapture: ImageCapture
) {

    val scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(FRACTION_BOX),
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
            }
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            coroutineScope.launch {
                lifecycleOwner.lifecycleScope.launch {
                    cameraImageViewModel.currentCamera.collect{ cameraSelector ->
                        val provider =
                            withContext(Dispatchers.IO) {
                                cameraProvider.get()
                            }

                        try {
                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner, cameraSelector, imageCapture, previewUseCase
                            )
                        } catch (_: Exception) {
                            removeViewModel.onErrorShow(
                                context.resources.getString(
                                    R.string.common_error_something_went_wrong)
                            )
                        }
                    }
                }
            }
            previewView
        }
    )
}

private const val FRACTION_BOX = 0.75f