package com.compose.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bgrem.app.R
import com.compose.presentation.remove.RemoveViewModel
import com.compose.ui.theme.BgRemTheme
import com.compose.ui.theme.Typography

@Composable
fun ButtonCamera(
    navController: NavController,
    removeViewModel: RemoveViewModel,
    visible: Boolean,
    buttonPhotoColor: Color,
    buttonVideoColor: Color,
    enable: Boolean
) {

    Log.d("ButtonCamera", "enable = ${enable}")

    if (visible) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    if (enable){
                        removeViewModel.onEnableNavButton(false)
                        navController.navigate(
                            R.id.action_cameraVideoFragment_to_cameraImageFragment
                        )
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(32.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = buttonPhotoColor
                )
            ) {
                Text(
                    text = "Photo".uppercase(),
                    style = Typography.body1,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            Button(
                onClick = {
                    if (!enable){
                        removeViewModel.onEnableNavButton(true)
                        navController.navigate(
                            R.id.action_cameraImageFragment_to_cameraVideoFragment
                        )
                    }
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(32.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = buttonVideoColor
                )
            ) {
                Text(
                    text = "VIDEO RECORDING".uppercase(),
                    style = Typography.body1,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    } else {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(top = 30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonCameraPreview() {
//    val navController = rememberNavController()
//    BgRemTheme {
//        ButtonCamera(
//            navController = navController,
//            visible = true,
//            buttonPhotoColor = Color.Black,
//            buttonVideoColor = Color.Black
//        )
//    }
}