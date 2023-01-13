package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.bgrem.domain.common.media.MediaType
import com.compose.components.ImageForResult
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.components.VideoPlayerScreen
import com.compose.data.models.BackgroundType
import com.compose.models.Task
import com.compose.presentation.background.BackgroundViewModel
import com.compose.presentation.finalFragment.FinalComposeViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(
    navController: NavController,
    task: Task,
    removeViewModel: RemoveViewModel,
    backgroundViewModel: BackgroundViewModel,
    size: String,
    finalComposeViewModel: FinalComposeViewModel
) {
    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        darkIcons = true
    )

    val errorMessage = finalComposeViewModel.errorMessage.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val visibleProgressBar = finalComposeViewModel.visibleProgressBar.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .background(color = lightPalette.primaryBackgroundColor)
    ) {
        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                finalComposeViewModel.onErrorShow(null)
                scope.launch {
                    snackBarHostState.showSnackbar(
                        errorMessage
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FinalTopBar(
                navController = navController,
                removeViewModel = removeViewModel,
                backgroundViewModel = backgroundViewModel
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(HEIGHT_FLOAT)
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(),
                            text = stringResource(id = R.string.final_your_result),
                            style = Typography.h2,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        if(removeViewModel.selectMediaInfo.value.mediaType == MediaType.VIDEO || backgroundViewModel.backgroundType == BackgroundType.VIDEO
                            || backgroundViewModel.yourBgType == "video") {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                text = "MP4 - $size kB",
                                style = Typography.h2,
                                fontSize = 12.sp,
                                color = lightPalette.sliderTextColor,
                                textAlign = TextAlign.Center
                            )
                        } else if (backgroundViewModel.backgroundType == BackgroundType.TRANSPARENT && removeViewModel.selectMediaInfo.value.mediaType == MediaType.IMAGE) {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                text = "PNG - $size kB",
                                style = Typography.h2,
                                fontSize = 12.sp,
                                color = lightPalette.sliderTextColor,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                text = "JPG - $size kB",
                                style = Typography.h2,
                                fontSize = 12.sp,
                                color = lightPalette.sliderTextColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                if(removeViewModel.selectMediaInfo.value.mediaType == MediaType.VIDEO || backgroundViewModel.backgroundType == BackgroundType.VIDEO
                    || backgroundViewModel.yourBgType == "video") {
                    task.resultUrl?.let { it1 -> VideoPlayerScreen(videoUrl = it1) }
                } else {
                    task.resultUrl?.let { it1 -> ImageForResult(resultUrl = it1) }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = lightPalette.sliderIndicatorColor
                    ),
                    onClick = {
                        if(backgroundViewModel.yourBgType == "video" ||
                            backgroundViewModel.backgroundType == BackgroundType.VIDEO ||
                            removeViewModel.selectMediaInfo.value.mediaType == MediaType.VIDEO) {
                            finalComposeViewModel.saveMedia(
                                mediaType = MediaType.VIDEO,
                                task = task
                            )
                        } else {
                            finalComposeViewModel.saveMedia(
                                mediaType = MediaType.IMAGE,
                                task = task
                            )
                        }

                        backgroundViewModel.backgroundType = null
                        backgroundViewModel.getBgEmpty()
                    })
                {
                    Text(
                        modifier = Modifier
                            .padding(),
                        text = stringResource(id = R.string.download),
                        style = Typography.h2,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 6.dp),
                        painter = painterResource(id = R.drawable.cm_file_download),
                        contentDescription = "file download"
                    )
                }
            }
            FinalBottomButton(
                backgroundViewModel = backgroundViewModel,
                removeViewModel = removeViewModel,
                finalComposeViewModel = finalComposeViewModel,
                task = task
            )
        }
        if (visibleProgressBar.value){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = lightPalette.dialogBackgroundColor),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun FinalTopBar(
    navController: NavController,
    removeViewModel: RemoveViewModel,
    backgroundViewModel: BackgroundViewModel
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
        Text(
            modifier = Modifier
                .padding(top = 10.dp, end = 10.dp)
                .clickable {
                    removeViewModel.clearTaskMedia()
                    backgroundViewModel.backgroundType = null
                    backgroundViewModel.getBgEmpty()
                    navController.navigate(R.id.removeComposeFragment)
                },
            text = stringResource(id = R.string.upload_new_file),
            style = Typography.h2,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun FinalBottomButton(
    backgroundViewModel: BackgroundViewModel,
    removeViewModel: RemoveViewModel,
    finalComposeViewModel: FinalComposeViewModel,
    task: Task
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { finalComposeViewModel.plugClick() }
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.cm_facebook),
                contentDescription = "facebook"
            )
        }
        IconButton(
            onClick = { finalComposeViewModel.publicationInInstagram(
                mediaType = MediaType.VIDEO,
                task = task
            ) }
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.cm_instagram),
                contentDescription = "facebook"
            )
        }
        IconButton(
            onClick = { finalComposeViewModel.plugClick()}
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.cm_wats_up),
                contentDescription = "wats up"
            )
        }
        IconButton(
            onClick = { finalComposeViewModel.plugClick() }
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.cm_telegram),
                contentDescription = "telegram"
            )
        }
        IconButton(
            onClick = {
                if(backgroundViewModel.yourBgType == "video" ||
                    backgroundViewModel.backgroundType == BackgroundType.VIDEO ||
                    removeViewModel.selectMediaInfo.value.mediaType == MediaType.VIDEO) {
                    finalComposeViewModel.sharedFile(
                        mediaType = MediaType.VIDEO,
                        task = task
                    )
                } else {
                    finalComposeViewModel.sharedFile(
                        mediaType = MediaType.IMAGE,
                        task = task
                    )
                }
            }
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.cm_arrow_end),
                contentDescription = "arrow end"
            )
        }
        IconButton(
            onClick = { finalComposeViewModel.sharedApp()}
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.cm_file_upload),
                contentDescription = "file upload"
            )
        }
    }
}

const val HEIGHT_FLOAT = 0.9f
