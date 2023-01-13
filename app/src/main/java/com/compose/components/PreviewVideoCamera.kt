package com.compose.components

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.compose.presentation.camera.video.CameraVideoViewModel

@Composable
fun PreviewVideoCamera(
    cameraVideoViewModel: CameraVideoViewModel
) {

    val scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER

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
            cameraVideoViewModel.getPreview(previewUseCase)
            previewView
        }
    )
}

private const val FRACTION_BOX = 0.75f