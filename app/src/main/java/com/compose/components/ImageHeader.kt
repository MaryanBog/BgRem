package com.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ImageHeader(
    paintRes: Int
) {
    Image(
        modifier = Modifier
            .fillMaxWidth(),
        painter = painterResource(id = paintRes),
        contentScale = ContentScale.Crop,
        contentDescription = "image_welcome"
    )
}