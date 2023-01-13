package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun YourBgGalleryScreen(
    navController: NavController,
    visibleDialog: MutableState<Boolean>
) {

   Scaffold(
            backgroundColor = lightPalette.dialogBackgroundColor,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    visibleDialog.value = false
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
            ) {
                Card(
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.Bottom),
                    shape = RoundedCornerShape(0)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 18.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .clickable {
                                    navController.navigate(
                                        R.id.action_backgroundsSelectComposeFragment_to_yourBgImageFragment
                                    )
                                },
                            text = stringResource(id = R.string.select_background_photo),
                            style = Typography.body1,
                            fontSize = 21.sp,
                            color = Color.Black
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .clickable {
                                    navController.navigate(
                                        R.id.action_backgroundsSelectComposeFragment_to_yourBgVideoFragment
                                    )
                                },
                            text = stringResource(id = R.string.select_background_video),
                            style = Typography.body1,
                            fontSize = 21.sp,
                            color = Color.Black
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(align = Alignment.End)
                                .clickable {
                                    visibleDialog.value = false
                                },
                            text = stringResource(id = R.string.common_cancel).uppercase(),
                            style = Typography.body1,
                            fontSize = 21.sp,
                            color = lightPalette.sliderIndicatorColor
                        )
                    }
                }
            }
        }
}