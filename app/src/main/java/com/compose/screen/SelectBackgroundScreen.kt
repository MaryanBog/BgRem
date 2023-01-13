package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bgrem.app.R
import com.compose.components.*
import com.compose.data.models.BackgroundType
import com.compose.models.Background
import com.compose.models.FavoritesFirstFiveResult
import com.compose.models.Task
import com.compose.presentation.background.BackgroundViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.compose.presentation.welcome.WelcomeComposeViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun SelectBackgroundScreen(
    navController: NavController,
    viewModel: BackgroundViewModel,
    welcomeViewModel: WelcomeComposeViewModel,
    background: Background,
    task: Task,
    removeViewModel: RemoveViewModel
) {

    val transparentChoose = remember { mutableStateOf(false) }
    val colorChoose = remember { mutableStateOf(false) }
    val yourBgChoose = remember { mutableStateOf(false) }
    val videoChoose = remember { mutableStateOf(false) }
    val imageChoose = remember { mutableStateOf(false) }
    val bgChoose = remember { mutableStateOf(false) }
    val moveChoose = remember { mutableStateOf(false) }
    val scaleChoose = remember { mutableStateOf(false) }

    when (viewModel.backgroundType) {
        BackgroundType.VIDEO -> videoChoose.value = true
        BackgroundType.IMAGE -> imageChoose.value = true
        BackgroundType.COLOR -> colorChoose.value = true
        BackgroundType.YOURBG -> yourBgChoose.value = true
        BackgroundType.TRANSPARENT -> transparentChoose.value = true
        else -> {
            colorChoose.value = true
            viewModel.backgroundType = BackgroundType.COLOR
        }
    }

    val chosenColorBackground =
        if (colorChoose.value) colorResource(id = R.color.button_color_selected_background)
        else colorResource(id = R.color.background_color_select_background)
    val chosenColorText = if (colorChoose.value) colorResource(id = R.color.white)
    else colorResource(id = R.color.black)

    val chosenYourBgBackground =
        if (yourBgChoose.value) colorResource(id = R.color.button_color_selected_background)
        else colorResource(id = R.color.background_color_select_background)
    val chosenYourBgText = if (yourBgChoose.value) colorResource(id = R.color.white)
    else colorResource(id = R.color.black)

    val chosenImageBackground =
        if (imageChoose.value) colorResource(id = R.color.button_color_selected_background)
        else colorResource(id = R.color.background_color_select_background)
    val chosenImageText = if (imageChoose.value) colorResource(id = R.color.white)
    else colorResource(id = R.color.black)

    val chosenVideoBackground =
        if (videoChoose.value) colorResource(id = R.color.button_color_selected_background)
        else colorResource(id = R.color.background_color_select_background)
    val chosenVideoText = if (videoChoose.value) colorResource(id = R.color.white)
    else colorResource(id = R.color.black)

    val chosenTransparentBackground =
        if (transparentChoose.value) colorResource(id = R.color.button_color_selected_background)
        else colorResource(id = R.color.background_color_select_background)
    val chosenTransparentText = if (transparentChoose.value) colorResource(id = R.color.white)
    else colorResource(id = R.color.black)

    val visibleDialog = remember { mutableStateOf(false) }

    Surface(modifier = Modifier
        .fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.background_color_select_background)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack()
                                    viewModel.getFavoritesVideosForFolders(); viewModel.getFavoritesImagesForFolders()
                viewModel.saveImageIds(); viewModel.saveVideoIds()}) {
                    Icon(
                        painter = painterResource(R.drawable.cm_back),
                        contentDescription = "button",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(22.dp)
                    )
                }

//                IconButton(onClick = { }) {
//                    Icon(
//                        painter = painterResource(R.drawable.cm_roll_start),
//                        contentDescription = "button",
//                        tint = Color.Black,
//                        modifier = Modifier
//                            .size(15.dp)
//                    )
//                }
//
//                IconButton(onClick = { }) {
//                    Icon(
//                        painter = painterResource(R.drawable.cm_roll_end),
//                        contentDescription = "button",
//                        tint = Color.Black,
//                        modifier = Modifier
//                            .size(15.dp)
//                    )
//                }

                IconButton(onClick = {
                    when (viewModel.backgroundType) {
                        BackgroundType.YOURBG -> {
                            if (viewModel.yourBgType == "image") {
                                removeViewModel.observeRemovingBackgroundProgress()
                                removeViewModel.getNewYourBackgroundImage()
                                navController.navigate(
                                    R.id.action_backgroundsSelectComposeFragment_to_loadingFinalComposeFragment)
                            } else if (viewModel.yourBgType == "video") {
                                removeViewModel.observeRemovingBackgroundProgress()
                                removeViewModel.getNewYourBackgroundVideo()
                                navController.navigate(
                                    R.id.action_backgroundsSelectComposeFragment_to_loadingFinalComposeFragment)
                            }
                        }
                        BackgroundType.TRANSPARENT -> {
                            navController.navigate(R.id.finalComposeFragment)
                        }
                        else -> {
                            removeViewModel.observeRemovingBackgroundProgress()
                            removeViewModel.getNewBackground(background.id)
                            navController.navigate(
                                R.id.action_backgroundsSelectComposeFragment_to_loadingFinalComposeFragment)
                        }

                    }
                    viewModel.saveImageIds(); viewModel.saveVideoIds()
                    viewModel.getFavoritesVideosForFolders(); viewModel.getFavoritesImagesForFolders()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.cm_tick),
                        contentDescription = "button",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(22.dp)
                    )
                }

            }

            Box(
                modifier = Modifier
                    .background(colorResource(id = R.color.background_color_select_background))
                    .height(30.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .background(colorResource(id = R.color.background_color_select_background))
                    .height(320.dp)
                    .fillMaxWidth()
            ) {

                when (viewModel.backgroundType) {
                    BackgroundType.VIDEO -> {
                        background.thumbnail_url?.let { VideoBackground(videoBgUrl = it) }
                    }
                    BackgroundType.IMAGE -> {
                        background.thumbnail_url?.let { BackgroundImageForBgEditor(url = it) }
                    }
                    BackgroundType.COLOR -> {
                        background.color?.let { ColorForBgEditor(colorBg = it) }
                    }
                    BackgroundType.TRANSPARENT -> {
                        TransparentChoose()
                    }
                    BackgroundType.YOURBG -> {
                        if (viewModel.yourBgType == "image") {
                            viewModel.imageUri?.let { YourBgImageForBgEditor(url = it) }
                        } else if (viewModel.yourBgType == "video") {
                            viewModel.videoUri?.let { VideoYourBackground(videoBgUrl = it) }
                        }

                    }
                    else -> {}
                }
                task.resultUrl?.let { ImageForBgEditor(url = it) }
            }

            GreyBox()

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 10.dp, top = 10.dp, bottom = 5.dp)
            ) {

                Button(
                    modifier = Modifier
                        .width(170.dp)
                        .height(40.dp)
                        .padding(end = 10.dp),
                    onClick = {
                        viewModel.getColors(); transparentChoose.value = false
                        colorChoose.value = true
                        yourBgChoose.value = false
                        imageChoose.value = false
                        videoChoose.value = false
                        viewModel.getBgEmpty()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = chosenColorBackground, contentColor = chosenColorText
                    )

                ) {

                    Text(text = stringResource(id = R.string.select_background_color).uppercase())
                }

                Button(
                    onClick = {
                        viewModel.getYourBg(); transparentChoose.value = false
                        colorChoose.value = false; yourBgChoose.value =
                        true; imageChoose.value =
                        false
                        videoChoose.value = false
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = chosenYourBgBackground,
                        contentColor = chosenYourBgText
                    ), modifier = Modifier
                        .width(170.dp)
                        .height(40.dp)
                        .padding(end = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.select_background_your_bg).uppercase())
                }

                Button(
                    onClick = {
                        viewModel.getFirstFiveFavoriteImages();
                        transparentChoose.value = false
                        colorChoose.value = false; yourBgChoose.value =
                        false; imageChoose.value =
                        true; videoChoose.value = false
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = chosenImageBackground, contentColor = chosenImageText
                    ), modifier = Modifier
                        .width(170.dp)
                        .height(40.dp)
                        .padding(end = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.select_background_image).uppercase())
                }

                Button(
                    onClick = {
                        viewModel.getFirstFiveFavoriteVideos();
                        transparentChoose.value = false
                        colorChoose.value = false; yourBgChoose.value = false
                        imageChoose.value = false; videoChoose.value = true
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = chosenVideoBackground, contentColor = chosenVideoText
                    ), modifier = Modifier
                        .width(170.dp)
                        .height(40.dp)
                        .padding(end = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.select_background_video).uppercase())
                }

                Button(
                    onClick = {
                        viewModel.getTransparent(); transparentChoose.value = true
                        colorChoose.value = false; yourBgChoose.value =
                        false; imageChoose.value =
                        false; videoChoose.value = false
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = chosenTransparentBackground,
                        contentColor = chosenTransparentText
                    ), modifier = Modifier
                        .width(170.dp)
                        .height(40.dp)
                        .padding(end = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.select_background_transparent).uppercase())
                }
            }

            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
            ) {

                LazyRow(modifier = Modifier.padding(top = 20.dp)) {
                    when (viewModel.backgroundType) {
                        BackgroundType.COLOR -> {
                            itemsIndexed(items = welcomeViewModel.uiBackColorState.value.backColor) { _, item ->
                                ItemColorsLists(backgroundItem = item, viewModel)
                            }
                        }
                        BackgroundType.IMAGE -> {
                            itemsIndexed(items = viewModel.favImagesList) { _, item ->
                                ItemFavoriteImagesLists(background = item, viewModel)
                            }
                        }
                        BackgroundType.VIDEO -> {
                                itemsIndexed(items = viewModel.favVideosList) { _, item ->
                                    ItemFavoriteVideosLists(background = item, viewModel)
                                }
                        }
                        BackgroundType.YOURBG -> {

                        }
                        BackgroundType.TRANSPARENT -> {

                        }
                        else -> {

                        }

                    }

                    item {
                        IconButton(
                            onClick = {
                                when (viewModel.backgroundType) {
                                    BackgroundType.COLOR -> viewModel.getColors()
                                    BackgroundType.IMAGE -> {
                                        viewModel.uiCategoryImageState
                                        navController.navigate(
                                            R.id.action_backgroundsSelectComposeFragment_to_foldersBottomSheetComposeFragment
                                        )
                                    }
                                    BackgroundType.VIDEO -> {
                                        viewModel.uiCategoryVideoState
                                        navController.navigate(
                                            R.id.action_backgroundsSelectComposeFragment_to_foldersBottomSheetComposeFragment
                                        )
                                    }
                                    BackgroundType.TRANSPARENT -> viewModel.getTransparent()
                                    BackgroundType.YOURBG -> {
                                        viewModel.getYourBg()
                                        visibleDialog.value = true
                                    }
                                    else -> viewModel.getTransparent()
                                }
                            },
                            modifier = Modifier
                                .height(100.dp)
                                .width(120.dp)

                        ) {

                            if (colorChoose.value || transparentChoose.value) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cm_add),
                                    tint = colorResource(id = R.color.white),
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp),
                                    contentDescription = ""
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cm_add),
                                    tint = colorResource(id = R.color.button_color_selected_background),
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp),
                                    contentDescription = ""
                                )
                            }

                        }
                    }
                }
            }
//            IconChooseScaleMove(
//                bgChoose = bgChoose,
//                moveChoose = moveChoose,
//                scaleChoose = scaleChoose
//            )
        }
    }

    if (visibleDialog.value) {
        YourBgGalleryScreen(
            navController = navController,
            visibleDialog = visibleDialog
        )
    }
}

@Composable
fun IconChooseScaleMove(
    bgChoose: MutableState<Boolean>,
    moveChoose: MutableState<Boolean>,
    scaleChoose: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .background(color = Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        Row {
            IconButton(
                onClick = {
                    bgChoose.value = true
                    moveChoose.value = false
                    scaleChoose.value = false
                },
                modifier = Modifier
                    .size(80.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (bgChoose.value) {
                        Icon(
                            painter = painterResource(id = R.drawable.cm_choose_bg),
                            tint = colorResource(id = R.color.black),
                            modifier = Modifier
                                .size(25.dp),
                            contentDescription = ""
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Choose bg",
                            color = colorResource(id = R.color.black)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.cm_choose_bg),
                            tint = colorResource(id = R.color.not_choose_button),
                            modifier = Modifier
                                .size(25.dp),
                            contentDescription = ""
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Choose bg",
                            color = colorResource(id = R.color.not_choose_button)
                        )
                    }

                }

            }
        }
        Row {
            IconButton(
                onClick = {
                    bgChoose.value = false
                    moveChoose.value = false
                    scaleChoose.value = true
//                    navController.navigate(R.id.scaleFragment)
                },
                modifier = Modifier
                    .size(80.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (scaleChoose.value) {
                        Icon(
                            painter = painterResource(id = R.drawable.cm_scale),
                            tint = colorResource(id = R.color.black),
                            modifier = Modifier
                                .size(25.dp),
                            contentDescription = ""
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Scale",
                            color = colorResource(id = R.color.black)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.cm_scale),
                            tint = colorResource(id = R.color.not_choose_button),
                            modifier = Modifier
                                .size(25.dp),
                            contentDescription = ""
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Scale",
                            color = colorResource(id = R.color.not_choose_button)
                        )
                    }

                }

            }
        }
        Row {
            IconButton(
                onClick = {
                    bgChoose.value = false
                    moveChoose.value = true
                    scaleChoose.value = false
//                    navController.navigate(R.id.moveFragment)
                },
                modifier = Modifier
                    .size(80.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (moveChoose.value) {
                        Icon(
                            painter = painterResource(id = R.drawable.cm_move),
                            tint = colorResource(id = R.color.black),
                            modifier = Modifier
                                .size(27.dp),
                            contentDescription = ""
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Move",
                            color = colorResource(id = R.color.black)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.cm_move),
                            tint = colorResource(id = R.color.not_choose_button),
                            modifier = Modifier
                                .size(27.dp),
                            contentDescription = ""
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Move",
                            color = colorResource(id = R.color.not_choose_button)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemFavoriteVideosLists(
    background: Background,
    viewModel: BackgroundViewModel)
{
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(background.poster_url)
            .crossfade(true)
            .placeholder(R.color.background_color_select_background)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(end = 3.dp, bottom = 5.dp)
            .size(95.dp)
            .clickable { viewModel.getCurrentBg(background) }

    )

}

@Composable
fun ItemFavoriteImagesLists(
    background: Background,
    viewModel: BackgroundViewModel)
{
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(background.small_thumbnail_url)
            .crossfade(true)
            .placeholder(R.color.background_color_select_background)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(end = 3.dp, bottom = 5.dp)
            .size(95.dp)
            .clickable {
                viewModel.getCurrentBg(background)
            }

    )

}

@Composable
fun ItemColorsLists(
    backgroundItem: Background,
    viewModel: BackgroundViewModel)
{
    val hex = backgroundItem.color!!.replace("#", "")
    val red: Int = hex.substring(0, 2).toInt(16)
    val green: Int = hex.substring(2, 4).toInt(16)
    val blue: Int = hex.substring(4, 6).toInt(16)

    @Stable
    fun getColor(
        red: Int, green: Int, blue: Int, alpha: Int = 0xFF
    ): Color {
        val color = ((alpha and 0xFF) shl 24) or
                ((red and 0xFF) shl 16) or
                ((green and 0xFF) shl 8) or
                (blue and 0xFF)
        return Color(color)
    }

    val newColor = getColor(red, green, blue)

    Card(
        modifier = Modifier
            .clickable { viewModel.getCurrentBg(backgroundItem) }
            .padding(end = 3.dp, bottom = 5.dp)
            .size(95.dp),
        backgroundColor = newColor
    ) {

    }
}

@Composable
fun ItemUserLists(background: Background) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(background.file_url)
            .crossfade(true)
            .placeholder(R.color.background_color_select_background)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(end = 3.dp, bottom = 5.dp)
            .size(95.dp)

    )
}
