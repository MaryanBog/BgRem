package com.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.presentation.remove.RemoveViewModel
import com.compose.ui.theme.lightPalette

@Composable
fun RoundButtonPlus(
    navController: NavController,
    removeViewModel: RemoveViewModel
) {
    val buttonBrush = Brush.verticalGradient(
        listOf(Color.White, lightPalette.secondaryBackgroundColor),
        startY = 50.0f,
        endY = 100.0f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = lightPalette.secondaryBackgroundColor)
    ) {
        Card(
            modifier = Modifier
                .size(80.dp)
                .offset(0.dp, (-40).dp)
                .background(brush = buttonBrush)
                .align(Alignment.Center),
            shape = RoundedCornerShape(100),
            contentColor = Color.White
        ) {
            Card(
                modifier = Modifier
                    .size(80.dp)
                    .padding(6.dp)
                    .background(color = Color.White),
                shape = RoundedCornerShape(100),
                elevation = 0.dp
            ) {
                Button(
                    modifier = Modifier
                        .size(80.dp)
                        .background(color = Color.White),
                    onClick = {
                        removeViewModel.onVisiblePhotoVideoButton(true)
                        navController.navigate(
                            R.id.action_removeComposeFragment_to_cameraImageFragment
                        )
                    },
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = lightPalette.sliderIndicatorColor
                    )
                ) {
                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(id = R.drawable.cm_plus),
                        contentDescription = "plus"
                    )
                }
            }
        }
    }
}
