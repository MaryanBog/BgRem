package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.components.ButtonPhotoVideo
import com.compose.components.RemoveCardBack
import com.compose.components.RoundButtonPlus
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.ui.theme.lightPalette
import com.compose.presentation.remove.RemoveViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveScreen(
    navController: NavController,
    errorMessage: String?,
    removeViewModel: RemoveViewModel
) {

    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.gradientFirstColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        true
    )

    var visibleString by rememberSaveable { mutableStateOf(false) }
    var visibleDialog by rememberSaveable { mutableStateOf(false) }
    var navIdGallery by rememberSaveable { mutableStateOf(
        R.id.action_removeComposeFragment_to_videoGalleryFragment) }
    var navIdCamera by rememberSaveable { mutableStateOf(
        R.id.action_removeComposeFragment_to_cameraVideoFragment) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier
            .fillMaxSize()
    ) {
        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                removeViewModel.onErrorShow(null)
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
                .background(color = Color.White),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RemoveCardBack(navController = navController)
            ButtonPhotoVideo(
                onTextChange = { visibleString = it },
                onDialogChange = {visibleDialog = it},
                onNavChangeGallery = {navIdGallery = it},
                onNavChangeCamera = {navIdCamera = it}
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                contentAlignment = Alignment.Center
            ) {

            }
            RoundButtonPlus(
                navController = navController,
                removeViewModel = removeViewModel
            )
        }
    }
    DialogScreen(
        navController = navController,
        visibleString = visibleString,
        visibleDialog = visibleDialog,
        onDialogChange = {visibleDialog = it},
        navIdGallery = navIdGallery,
        navIdCamera = navIdCamera,
        removeViewModel = removeViewModel
    )
}

