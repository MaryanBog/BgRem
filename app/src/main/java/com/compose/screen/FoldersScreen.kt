package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bgrem.app.R
import com.compose.data.models.BackgroundType
import com.compose.models.Background
import com.compose.models.Category
import com.compose.presentation.background.BackgroundViewModel
import com.compose.presentation.welcome.WelcomeComposeViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun FoldersScreen(
    navController: NavController,
    viewModel: BackgroundViewModel,
    welcomeViewModel: WelcomeComposeViewModel
) {
    val backgroundsChoose = remember { mutableStateOf(true) }
    val favoritesChoose = remember { mutableStateOf(false) }
    val backgroundsVisible = remember { mutableStateOf(true) }
    val favoritesVisible = remember { mutableStateOf(false) }

    val backgroundChosen = if (backgroundsChoose.value) FontWeight.Bold else FontWeight.Light
    val favoritesChosen = if (favoritesChoose.value) FontWeight.Bold else FontWeight.Light

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        border = BorderStroke(3.dp, colorResource(id = R.color.background_color_select_background))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                navController.navigate(R.id.backgroundsSelectComposeFragment)
                viewModel.saveVideoIds(); viewModel.saveImageIds()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.cm_cancel),
                    contentDescription = "desc", modifier = Modifier.size(22.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.select_background_title).uppercase(),
                modifier = Modifier
                    .padding(top = 40.dp),
                color = colorResource(id = R.color.black),
                fontSize = 20.sp
            )

            Row {
                Text(text = "backgrounds".uppercase(), fontSize = 15.sp,
                    modifier = Modifier
                        .clickable {
                            backgroundsChoose.value = true; favoritesChoose.value = false
                            backgroundsVisible.value = true; favoritesVisible.value = false
                        }
                        .padding(start = 40.dp, top = 20.dp, bottom = 10.dp),
                    fontWeight = backgroundChosen
                )
                Spacer(Modifier.weight(1f, true))
                Text(text = "FAVORITES".uppercase(), fontSize = 15.sp,
                    modifier = Modifier
                        .clickable {
                            backgroundsChoose.value = false; favoritesChoose.value = true
                            backgroundsVisible.value = false; favoritesVisible.value = true
                        }
                        .padding(end = 60.dp, top = 20.dp, bottom = 10.dp),
                    fontWeight = favoritesChosen)
            }

            if (backgroundsVisible.value) {
                when (viewModel.backgroundType) {
                    BackgroundType.IMAGE -> {
                        CategoriesList(
                            viewModel.uiCategoryImageState.value.category, viewModel,
                            viewModel.backgroundType!!, navController
                        )
                    }
                    BackgroundType.VIDEO -> {
                        CategoriesList(
                            viewModel.uiCategoryVideoState.value.category, viewModel,
                            viewModel.backgroundType!!, navController
                        )
                    }
                    else -> CategoriesList(
                        viewModel.uiCategoryVideoState.value.category, viewModel,
                        viewModel.backgroundType!!, navController
                    )
                }

            } else {
                when (viewModel.backgroundType) {
                    BackgroundType.IMAGE -> {
                        FavoriteImageList(viewModel, navController)
                    }
                    BackgroundType.VIDEO -> {
                        FavoriteVideosList(viewModel, navController)
                    }
                    else -> { }

                }

            }

        }
    }
}

@Composable
fun CategoriesList(
    categoryList: List<Category>, viewModel: BackgroundViewModel,
    backgroundType: BackgroundType, navController: NavController
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        itemsIndexed(items = categoryList) { _, item ->
            ItemCategoriesList(category = item, viewModel, backgroundType, navController)
        }

    }
}

@Composable
fun ItemCategoriesList(
    category: Category,
    viewModel: BackgroundViewModel,
    backgroundType: BackgroundType,
    navController: NavController
) {
    Box(modifier = Modifier.padding(5.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(category.image)
                .crossfade(true)
                .placeholder(R.color.background_color_select_background)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 3.dp, bottom = 3.dp)
                .size(180.dp)
                .clickable {
                    if (backgroundType == BackgroundType.VIDEO) {
                        viewModel.getVideoFromCategory(category.id)
                    } else if (backgroundType == BackgroundType.IMAGE) {
                        viewModel.getImageFromCategory(category.id)
                    }
                    navController.navigate(R.id.action_foldersBottomSheetComposeFragment_to_backgroundsComposeFragment)
                }

        )

        Text(
            text = category.name.uppercase(), textAlign = TextAlign.Center,
            color = colorResource(id = R.color.white),
            fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }

}

@Composable
fun FavoriteImageList(viewModel: BackgroundViewModel, navController: NavController) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        itemsIndexed(items = viewModel.favImagesList) { _, item ->
            ItemFavoriteImageList(background = item, viewModel, navController)
        }

    }
}

@Composable
fun ItemFavoriteImageList(background: Background, viewModel: BackgroundViewModel,
                          navController: NavController) {
    var liked by remember { mutableStateOf(false) }

    liked = viewModel.favoritesImages.contains(background.id)

    Box(modifier = Modifier.padding(5.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(background.thumbnail_url)
                .crossfade(true)
                .placeholder(R.color.background_color_select_background)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 3.dp, bottom = 3.dp)
                .size(180.dp)
                .clickable {
                    viewModel.getCurrentBg(background); viewModel.backgroundType = BackgroundType.IMAGE
                    navController.navigate(R.id.backgroundsSelectComposeFragment)
                }

        )

        IconButton(
            onClick = { liked = !liked },
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp)
        ) {
            if (liked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cm_favorite),
                    contentDescription = " ",
                    tint = colorResource(id = R.color.red)
                )
                viewModel.favoritesImages.add(background.id)
                if(!viewModel.favImagesList.contains(background)) {
                    viewModel.favImagesList.add(background)
                }

            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cm_favorite),
                    contentDescription = " ",
                    tint = colorResource(id = R.color.white)
                )
                viewModel.favoritesImages.remove(background.id)
                viewModel.favImagesList.remove(background)
            }

        }

    }

}

@Composable
fun FavoriteVideosList(viewModel: BackgroundViewModel, navController: NavController) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        itemsIndexed(items = viewModel.favVideosList) { _, item ->
            ItemFavoriteVideosList(background = item, viewModel, navController)
        }

    }
}

@Composable
fun ItemFavoriteVideosList(background: Background, viewModel: BackgroundViewModel, navController: NavController) {
    var liked by remember { mutableStateOf(false) }

    liked = viewModel.favoritesVideos.contains(background.id)

    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(background.poster_url)
                .crossfade(true)
                .placeholder(R.color.background_color_select_background)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(5.dp)
                .size(180.dp)
                .clickable {
                    viewModel.getCurrentBg(background); viewModel.backgroundType = BackgroundType.VIDEO
                    navController.navigate(R.id.backgroundsSelectComposeFragment)
                    viewModel.saveVideoIds()
                    viewModel.getFavoritesVideosForFolders()
                }
        )

        IconButton(
            onClick = { liked = !liked },
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp)
        ) {
            if (liked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cm_favorite),
                    contentDescription = " ",
                    tint = colorResource(id = R.color.red)
                )
                viewModel.favoritesVideos.add(background.id)
                if(!viewModel.favVideosList.contains(background)) {
                    viewModel.favVideosList.add(background)
                }

            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cm_favorite),
                    contentDescription = " ",
                    tint = colorResource(id = R.color.white)
                )
                viewModel.favoritesVideos.remove(background.id)
                viewModel.favVideosList.remove(background)
            }


        }

    }

}

@Preview(showBackground = true)
@Composable
fun FoldersScreenPreview() {
//    FoldersScreen()
}
