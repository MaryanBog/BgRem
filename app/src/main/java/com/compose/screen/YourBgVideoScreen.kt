package com.compose.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.data.models.GetImageTypesParams
import com.compose.presentation.background.gallery.YourBgViewModel
import com.compose.presentation.gallery.video.OpenVideoContract
import com.compose.ui.theme.lightPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun YourBgVideoScreen(
    yourBgViewModel: YourBgViewModel
) {

    val pickVideoContract = rememberLauncherForActivityResult(
        contract = OpenVideoContract(),
        onResult = { uri ->
            yourBgViewModel.onAddVideo(uri)
        })

    val pickVideo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            yourBgViewModel.onAddVideo(uri)
        })

    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        true
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green),
    ){
            LaunchedEffect(true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pickVideo.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.VideoOnly
                        )
                    )
                } else {
                    GetImageTypesParams(yourBgViewModel.videoMimeTypes).let {
                        pickVideoContract.launch(it)
                    }
                }
            }
    }
}