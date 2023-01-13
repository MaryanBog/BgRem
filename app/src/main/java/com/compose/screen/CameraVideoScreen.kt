package com.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.components.ButtonCamera
import com.compose.components.PreviewVideoCamera
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.presentation.camera.video.CameraVideoViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette

@Composable
fun CameraVideoScreen(
    navController: NavController,
    cameraVideoViewModel: CameraVideoViewModel,
    removeViewModel: RemoveViewModel,
    visible: Boolean,
    enable: Boolean
) {

    val duration = cameraVideoViewModel.currentVideoRecordingDuration.collectAsState().value
    val visibleRecordButton = remember { mutableStateOf(true) }
    val visibleRecordIndicator = remember { mutableStateOf(false) }
    val minutes = (duration % 3600) / 60;
    val seconds = duration % 60;

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (visibleRecordIndicator.value) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp)
                        .padding(vertical = 2.dp)
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(ROUNDED_SHAPE_PERCENT)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = stringResource(
                            id = R.string.capture_video_recording_duration,
                            minutes,
                            seconds
                        ),
                        style = Typography.h1,
                        fontSize = 12.sp
                    )
                }
            }
        }
        PreviewVideoCamera(
            cameraVideoViewModel = cameraVideoViewModel
        )
        ButtonCamera(
            navController = navController,
            removeViewModel = removeViewModel,
            visible = visible,
            buttonPhotoColor = Color.Black,
            buttonVideoColor = lightPalette.buttonCameraBack,
            enable = enable
        )
        ActionButtonCamera(
            visibleRecordButton = visibleRecordButton,
            visibleRecordIndicator = visibleRecordIndicator,
            cameraVideoViewModel = cameraVideoViewModel
        )
    }
}

private const val ROUNDED_SHAPE_PERCENT = 100

@Composable
fun ActionButtonCamera(
    visibleRecordButton: MutableState<Boolean>,
    visibleRecordIndicator: MutableState<Boolean>,
    cameraVideoViewModel: CameraVideoViewModel
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (visibleRecordButton.value) {
            Spacer(modifier = Modifier.size(45.dp))
            Button(
                onClick = {
                    visibleRecordButton.value = false
                    visibleRecordIndicator.value = true
                    cameraVideoViewModel.onRecordingVideo()
                },
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(ROUNDED_SHAPE_PERCENT),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(ROUNDED_SHAPE_PERCENT)
                        )
                )
            }
        } else {
            Spacer(modifier = Modifier.size(45.dp))
            Button(
                onClick = {
                    visibleRecordButton.value = true
                    visibleRecordIndicator.value = false
                    cameraVideoViewModel.onStopRecordingVideo()
                },
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(ROUNDED_SHAPE_PERCENT),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color.Black,
                        )
                )
            }
        }
        Card(
            modifier = Modifier
                .size(45.dp)
                .clickable { cameraVideoViewModel.switchCamera() },
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