package com.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bgrem.app.R
import com.compose.data.models.BackgroundType
import com.compose.models.Background
import com.compose.presentation.background.BackgroundViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun BackgroundsImageScreen(
    navController: NavController,
    backgroundViewModel: BackgroundViewModel,
    list: List<Background>
) {

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        border = BorderStroke(3.dp, colorResource(id = R.color.background_color_select_background))
    ) {
        Scaffold {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    navController.navigate(R.id.foldersBottomSheetComposeFragment)
                    backgroundViewModel.saveImageIds(); backgroundViewModel.getFavoritesImagesForFolders()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.cm_back),
                        contentDescription = "button",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate(R.id.foldersBottomSheetComposeFragment)
                        backgroundViewModel.saveImageIds(); backgroundViewModel.getFavoritesImagesForFolders()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cm_cancel),
                        contentDescription = "arrow back",
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                val catList = backgroundViewModel.uiCategoryImageState.value.category
                val currentCategory =
                    catList.find { category -> category.id == backgroundViewModel.categoryId }

                Text(
                    text = currentCategory?.name!!.uppercase(),
                    modifier = Modifier
                        .padding(top = 40.dp),
                    color = colorResource(id = R.color.black),
                    fontSize = 20.sp
                )

                BackgroundsImageList(backgroundsList = list, backgroundViewModel, navController)

            }
        }
    }
}

@Composable
fun BackgroundsImageList(
    backgroundsList: List<Background>, viewModel: BackgroundViewModel,
    navController: NavController
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(top = 30.dp)) {
        itemsIndexed(items = backgroundsList) { _, item ->
            ItemBackgroundsImageList(background = item, viewModel, navController)
        }
    }
}

@Composable
fun ItemBackgroundsImageList(
    background: Background,
    viewModel: BackgroundViewModel,
    navController: NavController
) {
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
                    viewModel.getCurrentBg(background); viewModel.backgroundType =
                    BackgroundType.IMAGE
                    navController.navigate(R.id.backgroundsSelectComposeFragment)
                }

        )

        IconButton(
            onClick = {
                liked = !liked
                background.is_favorite = !background.is_favorite
            },
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
