package com.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bgrem.app.R
import com.bgrem.presentation.common.extensions.dpToPx
import com.compose.ui.theme.BgRemTheme
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette

@Composable
fun RemoveCardBack(
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .drawBehind {
                val x = size.width
                val path = Path().apply {
                    moveTo(0f, 0f)
                    if (size.width <= 1440) {
                        lineTo(440.dp.toPx(), 0f)
                        lineTo(440.dp.toPx(), 150.dp.toPx())
                        quadraticBezierTo(
                            206.dp.toPx(), 220.dp.toPx(),
                            0f, 150.dp.toPx()
                        )
                    } else {
                        lineTo(x.dpToPx, 0f)
                        lineTo(x.dpToPx, 150.dp.toPx())
                        quadraticBezierTo(
                            (x / 2).dpToPx, 220.dp.toPx(),
                            0f, 140.dp.toPx()
                        )
                    }
                    close()
                }
                drawPath(
                    path = path,
                    color = lightPalette.gradientFirstColor
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.remove_background),
                    style = Typography.h2,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(start = 22.dp, top = 22.dp),
                    fontSize = 30.sp,
                    maxLines = 2
                )
                IconButton(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .align(Alignment.CenterEnd),
                    onClick = {
                        navController
                            .navigate(R.id.action_removeComposeFragment_to_aboutComposeFragment)
                    }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.cm_info),
                        contentDescription = "attention icon",
                        tint = Color.White
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, top = 10.dp),
                text = stringResource(id = R.string.portrait_photos_and_videos_only),
                style = Typography.body2,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemoveCardBackPreview() {
    val navController = rememberNavController()
    BgRemTheme {
        RemoveCardBack(navController = navController)
    }
}