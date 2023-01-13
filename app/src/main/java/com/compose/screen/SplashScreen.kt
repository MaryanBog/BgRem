package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.presentation.welcome.WelcomeComposeViewModel
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplashScreen(
    navController: NavController,
    visibleSplash: Boolean,
    onSplashChange: (Boolean) -> Unit,
    welcomeComposeViewModel: WelcomeComposeViewModel
) {
    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        darkIcons = true
    )

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 2.0f,
            animationSpec =
                        tween(
                durationMillis = 2000
            )
        )
        if (welcomeComposeViewModel.getStateWelcomeScreen()){
            onSplashChange.invoke(false)
            welcomeComposeViewModel.saveStateWelcomeScreen(false)
        } else {
            navController.navigate(
                R.id.action_welcomeComposeFragment_to_removeComposeFragment
            )
        }
    }

    if (visibleSplash) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = lightPalette.primaryBackgroundColor),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.scale(scale.value),
                    painter = painterResource(id = R.drawable.ic_logo2),
                    contentDescription = "logo"
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale.value)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name_bg),
                        style = Typography.h1,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.app_name_rem),
                        style = Typography.h2,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}