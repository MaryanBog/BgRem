package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bgrem.app.BuildConfig
import com.bgrem.app.R
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.presentation.about.AboutComposeViewModel
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    navController: NavController,
    aboutComposeViewModel: AboutComposeViewModel
) {
    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        darkIcons = true
    )

    val errorMessage = aboutComposeViewModel.errorMessage.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val linear = Brush.linearGradient(
        listOf(
            lightPalette.gradientFirstColor,
            lightPalette.sliderIndicatorColor
        )
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(color = lightPalette.primaryBackgroundColor)
    ) {
        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                aboutComposeViewModel.onErrorShow(null)
                scope.launch {
                    snackBarHostState.showSnackbar(
                        errorMessage
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(linear),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = stringResource(id = R.string.about_what_is_bgrem),
                    style = Typography.h2,
                    color = Color.White,
                    fontSize = 26.sp
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, start = 18.dp, end = 18.dp),
                text = stringResource(id = R.string.about_automatic_background),
                style = Typography.h2,
                color = Color.Black,
                fontSize = 16.sp,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                text = stringResource(id = R.string.about_portrait_photo),
                style = Typography.h2,
                color = lightPalette.sliderTextColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .padding(top = 14.dp),
                painter = painterResource(id = R.drawable.cm_girl_photo),
                contentDescription = "Girl photo",
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                text = stringResource(id = R.string.about_social_media),
                style = Typography.h2,
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { aboutComposeViewModel.onFacebookLink() }
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.cm_facebook),
                        contentDescription = "facebook"
                    )
                }
                IconButton(
                    onClick = {
                        aboutComposeViewModel.onInstagramLink()
                    }
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.cm_instagram),
                        contentDescription = "instagram"
                    )
                }
                IconButton(
                    onClick = { aboutComposeViewModel.onYoutubeLink() }
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.cm_youtube),
                        contentDescription = "youtube"
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                text = stringResource(id = R.string.about_questions_issues),
                style = Typography.h2,
                color = lightPalette.sliderTextColor,
                fontSize = 12.sp,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { aboutComposeViewModel.onSendEmail() },
                text = stringResource(id = R.string.common_support_email),
                style = Typography.h2,
                color = lightPalette.sliderIndicatorColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.about_view_our),
                    style = Typography.body1,
                    fontSize = 12.sp,
                    color = lightPalette.sliderTextColor
                )
                Text(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .clickable { aboutComposeViewModel.onPoliceLink() },
                    text = stringResource(id = R.string.welcome_privacy_policy),
                    style = Typography.body1,
                    fontSize = 12.sp,
                    color = lightPalette.sliderIndicatorColor
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                text = stringResource(R.string.about_version, BuildConfig.VERSION_NAME),
                style = Typography.h2,
                color = lightPalette.sliderTextColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}