package com.compose.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bgrem.app.R
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.presentation.remove.RemoveViewModel

@Composable
fun PreviewImageScreen(
    navController: NavController,
    removeViewModel: RemoveViewModel,
    imageUri: Uri?
) {
    SetStatusAndNavigationBarColor(
        statusBarColor = Color.Black,
        navigationBarColor = Color.Black,
        false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp))
        AsyncImage(
            model = imageUri,
            contentDescription = "preview image",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_replay),
                contentDescription = "replay",
                modifier = Modifier.size(38.dp)
                    .clickable {
                        removeViewModel.onVisiblePhotoVideoButton(false)
                        navController.navigate(
                        R.id.action_previewImageFragment_to_cameraImageFragment
                    ) }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_done),
                contentDescription = "done",
                modifier = Modifier.size(38.dp)
                    .clickable { removeViewModel.onImagePush(imageUri) }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "close",
                modifier = Modifier.size(38.dp)
                    .clickable { navController.popBackStack() }
            )
        }
    }
}