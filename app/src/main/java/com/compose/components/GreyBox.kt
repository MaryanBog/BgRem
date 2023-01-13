package com.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.bgrem.app.R

@Composable
fun GreyBox() {
    Box(
        modifier = Modifier
            .background(colorResource(id = R.color.background_color_select_background))
            .height(60.dp)
            .fillMaxWidth()
    )
}