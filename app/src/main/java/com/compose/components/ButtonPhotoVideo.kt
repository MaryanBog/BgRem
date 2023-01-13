package com.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bgrem.app.R
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette

@Composable
fun ButtonPhotoVideo(
    onTextChange: (Boolean) -> Unit,
    onDialogChange: (Boolean) -> Unit,
    onNavChangeGallery: (Int) -> Unit,
    onNavChangeCamera: (Int) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(),
                text = "Upload file",
                style = Typography.body1,
                color = Color.Black,
                fontSize = 20.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onTextChange.invoke(false)
                        onDialogChange.invoke(true)
                        onNavChangeGallery.invoke(
                            R.id.action_removeComposeFragment_to_imageGalleryFragment
                        )
                        onNavChangeCamera.invoke(
                            R.id.action_removeComposeFragment_to_cameraImageFragment
                        )
                              },
                    modifier = Modifier
                        .width(154.dp)
                        .height(48.dp)
                        .padding(horizontal = 6.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = lightPalette.secondaryBackgroundColor
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.select_background_photo_test),
                        style = Typography.body1,
                        color = lightPalette.buttonTextColor,
                        fontSize = 18.sp
                    )
                }
                Button(
                    onClick = {
                        onTextChange.invoke(true)
                        onDialogChange.invoke(true)
                        onNavChangeGallery.invoke(
                            R.id.action_removeComposeFragment_to_videoGalleryFragment
                        )
                        onNavChangeCamera.invoke(
                            R.id.action_removeComposeFragment_to_cameraVideoFragment
                        )
                              },
                    modifier = Modifier
                        .width(154.dp)
                        .height(48.dp)
                        .padding(horizontal = 6.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = lightPalette.secondaryBackgroundColor
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.select_background_video_test),
                        style = Typography.body1,
                        color = lightPalette.buttonTextColor,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}