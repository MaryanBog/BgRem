package com.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.bgrem.app.R
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette
import com.compose.presentation.welcome.WelcomeComposeViewModel

@Composable
fun AnimateVisibleSlider(
    welcomeComposeViewModel: WelcomeComposeViewModel
) {
    val visible = welcomeComposeViewModel.visibilityAnimate.collectAsState().value
    val sliderPosition = welcomeComposeViewModel.sliderPosition.collectAsState().value

    val alphaText = when (sliderPosition) {
        0f -> 1f
        else -> {
            0f
        }
    }

    if (visible)
    {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                .height(50.dp)
                .shadow(4.dp, shape = RoundedCornerShape(30.dp))
                .background(color = lightPalette.secondaryBackgroundColor)
        ) {
            SliderValueHorizontal(
                value = sliderPosition,
                onValueChange = { welcomeComposeViewModel.onSliderPosition(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                thumbHeightMax = false,
                valueRange = 0f..VALUE_RANGE,
//                onValueChangeFinished = {
//                    welcomeComposeViewModel.onVisibilityAnimate(false)
//                },
                track = { modifier: Modifier,
                          progress: Float,
                          _, _, _ ->

                    Box(
                        Modifier
                            .height(50.dp).then(modifier)
                    ) {

                        val bgProgress = Modifier.background(
                            Brush.linearGradient(
                                listOf(
                                    lightPalette.sliderIndicatorColor,
                                    Color.White
                                )
                            ),
                            RoundedCornerShape(100)
                        )

                        Spacer(
                            bgProgress.fillMaxHeight()
                                .fillMaxWidth(fraction = progress)
                                .then(modifier)
                        )
                    }
                }, thumb = { modifier,
                             _: Dp,
                             mutableSource,
                             _, _ ->

                    var isPressed by remember { mutableStateOf(false) }
                    mutableSource.ListenOnPressed { isPressed = it }

                    Image(
                        modifier = modifier,
                        painter = painterResource(id = R.drawable.ic_logo2),
                        contentDescription = "logo"
                    )
                },
                thumbSizeInDp = DpSize(50.dp, 50.dp)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(alphaText),
                text = stringResource(id = R.string.welcome_slide_to_remove_background),
                style = Typography.body2,
                color = lightPalette.sliderTextColor
            )
        }
    }
}

private const val VALUE_RANGE = 4f