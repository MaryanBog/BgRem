package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.components.DotsPulsing
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.presentation.remove.RemoveViewModel
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScreen(
    navController: NavController,
    progress: Float,
    removeViewModel: RemoveViewModel
) {

    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        darkIcons = true
    )

    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(DURATION_MILLIS, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = lightPalette.primaryBackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.padding(8.dp),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cm_back),
                    contentDescription = "arrow back",
                    tint = Color.Black
                )
            }
            Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 165.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(
                            R.string.loading_loading).uppercase(),
                        style = Typography.h2,
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                    Box(modifier = Modifier.padding(top = 26.dp)) {
                        DotsPulsing()
                    }
                }
                    Image(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(20.dp)
                            .rotate(angle),
                        painter = painterResource(id = R.drawable.logo_bgrem),
                        contentDescription = "logo bgrem")
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(horizontal = 26.dp),
                    color = lightPalette.gradientFirstColor
                )
                    Text(
                        modifier = Modifier.padding(24.dp),
                        text = "${(progress * PROGRESS_100).toInt()}%",
                        style = Typography.h2,
                        fontSize = 32.sp,
                        color = Color.Black
                    )
                }
        }
    }
    if(removeViewModel.state.value == 1.0f) {
        navController.navigate(
            R.id.action_loadingComposeFragment_to_backgroundsSelectComposeFragment
        )
    } else if (removeViewModel.serverError.value == 1) {
        navController.navigate(
            R.id.action_loadingComposeFragment_to_troubleServerFragment)
    }
}

private const val DURATION_MILLIS = 2000
private const val PROGRESS_100 = 100