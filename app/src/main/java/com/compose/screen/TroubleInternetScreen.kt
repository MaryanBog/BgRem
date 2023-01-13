package com.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bgrem.app.R
import com.compose.components.SetStatusAndNavigationBarColor
import com.compose.ui.theme.BgRemTheme
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette

@Composable
fun TroubleInternetScreen(
    navController: NavController
) {
    SetStatusAndNavigationBarColor(
        statusBarColor = lightPalette.primaryBackgroundColor,
        navigationBarColor = lightPalette.primaryBackgroundColor,
        darkIcons = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = lightPalette.primaryBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(174.dp))
        Text(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.trouble_check_internet_connection),
            style = Typography.h2,
            color = lightPalette.buttonTextColor,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 90.dp, end = 90.dp),
            painter = painterResource(id = R.drawable.cm_no_internet),
            contentDescription = "no internet",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 24.dp, end = 24.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = lightPalette.sliderIndicatorColor
            ),
            onClick = {

            })
        {
            Text(
                text = stringResource(id = R.string.trouble_button_try_again),
                style = Typography.h2,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TroubleInternetScreenPreview() {
    val navController = rememberNavController()
    BgRemTheme {
        TroubleInternetScreen(navController = navController)
    }
}