package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.components.AnimateVisibleButton
import com.compose.components.AnimateVisibleSlider
import com.compose.components.ImageHeader
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette
import com.compose.presentation.welcome.WelcomeComposeViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WelcomeScreen(
    navController: NavController,
    welcomeComposeViewModel: WelcomeComposeViewModel,
    errorMessage: String?
) {
    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        darkIcons = true
    )

    val snackBarHostState = remember { SnackbarHostState() }
    val sliderPosition = welcomeComposeViewModel.sliderPosition.collectAsState().value
    var visibleSplash by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(color = lightPalette.primaryBackgroundColor),
        ){
        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                welcomeComposeViewModel.onErrorShow(null)
                scope.launch {
                    snackBarHostState.showSnackbar(
                        errorMessage
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(FRACTION_BOX),
            ) {
                when (sliderPosition) {
                    in 0.0..1.5 -> {
                        ImageHeader(paintRes = R.drawable.cm_welcome_one)
                    }
                    in 1.5..3.5 -> {
                        ImageHeader(paintRes = R.drawable.cm_welcome_two)
                    }
                    in 3.5..3.8 -> {
                        ImageHeader(paintRes = R.drawable.cm_welcome_three)
                    }
                    in 3.8..4.0 -> {
                        ImageHeader(paintRes = R.drawable.cm_welcome_three)
                        welcomeComposeViewModel.onVisibilityAnimate(false)
                    }
                }
            }
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.welcome_automatic),
                style = Typography.body1
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.common_photo_and_video),
                style = Typography.h1
            )
            AnimateVisibleSlider(welcomeComposeViewModel)
            AnimateVisibleButton(welcomeComposeViewModel, navController)
        }
    }
    SplashScreen(
        navController = navController,
        visibleSplash = visibleSplash,
        onSplashChange = { visibleSplash = it },
        welcomeComposeViewModel = welcomeComposeViewModel
    )
}

private const val FRACTION_BOX = 0.7f
