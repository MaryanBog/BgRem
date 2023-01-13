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
import com.compose.presentation.gallery.image.OpenImageContract
import com.compose.ui.theme.lightPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun YourBgImageScreen(
    yourBgViewModel: YourBgViewModel
) {

    val pickImageContract = rememberLauncherForActivityResult(
        contract = OpenImageContract(),
        onResult = { uri ->
            yourBgViewModel.onAddImage(uri)
        }
    )

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            yourBgViewModel.onAddImage(uri)
        }
    )

    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        true
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ){
            LaunchedEffect(true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pickImage.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                } else {
                    GetImageTypesParams(yourBgViewModel.imageMimeTypes).let {
                        pickImageContract.launch(it)
                    }
                }
            }
    }
}