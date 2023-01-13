package com.compose.screen

import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.components.ButtonCamera
import com.compose.components.PreviewImageCamera
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.presentation.camera.image.CameraImageViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.compose.ui.theme.lightPalette
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun CameraImageScreen(
    navController: NavController,
    cameraProvider: ListenableFuture<ProcessCameraProvider>,
    cameraImageViewModel: CameraImageViewModel,
    removeViewModel: RemoveViewModel,
    imageCapture: ImageCapture,
    visible: Boolean,
    enable: Boolean
) {

    SetStatusAndNavigationBarColor(
        statusBarColor = Color.Black,
        navigationBarColor = Color.Black,
        false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PreviewImageCamera(
            cameraProvider = cameraProvider,
            removeViewModel = removeViewModel,
            cameraImageViewModel = cameraImageViewModel,
            imageCapture = imageCapture
        )
        ButtonCamera(
            navController = navController,
            removeViewModel = removeViewModel,
            visible = visible,
            buttonPhotoColor = lightPalette.buttonCameraBack,
            buttonVideoColor = Color.Black,
            enable = enable
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(45.dp))
            Button(
                onClick = { cameraImageViewModel.onTakePhoto() },
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(ROUNDED_SHAPE_PERCENT),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                )
            ) {}
            Card(
                modifier = Modifier
                    .size(45.dp)
                    .clickable { cameraImageViewModel.switchCamera() },
                shape = RoundedCornerShape(ROUNDED_SHAPE_PERCENT),
                backgroundColor = lightPalette.buttonCameraBack
            ) {
                Image(
                    modifier = Modifier.padding(4.dp),
                    painter = painterResource(R.drawable.cm__autorenew),
                    contentDescription = "switch"
                )
            }
        }
    }
}

private const val ROUNDED_SHAPE_PERCENT = 100