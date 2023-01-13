package com.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bgrem.app.R
import com.compose.ui.theme.Typography
import com.compose.ui.theme.lightPalette
import com.compose.presentation.welcome.WelcomeComposeViewModel

@Composable
fun AnimateVisibleButton(
    welcomeComposeViewModel: WelcomeComposeViewModel,
    navController: NavController
) {

    val visible = welcomeComposeViewModel.visibilityAnimate.collectAsState().value

       if (!visible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                .height(50.dp)
                .shadow(4.dp, shape = RoundedCornerShape(30.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = lightPalette.sliderIndicatorColor
            ),
            onClick = { navController
                .navigate(R.id.action_welcomeComposeFragment_to_removeComposeFragment) }
        ) {
            Text(
                text = stringResource(id = R.string.welcome_get_started),
                style = Typography.h2,
                color = Color.White
            )
        }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_by_getting_started),
                    style = Typography.body1,
                    fontSize = 12.sp,
                    color = lightPalette.sliderTextColor
                )
                Text(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .clickable {
                            welcomeComposeViewModel.onTermsLink()
                        },
                    text = stringResource(id = R.string.welcome_terms_and_user),
                    style = Typography.body1,
                    fontSize = 12.sp,
                    color = lightPalette.sliderIndicatorColor
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = stringResource(id = R.string.welcome_and),
                    style = Typography.body1,
                    fontSize = 12.sp,
                    color = lightPalette.sliderTextColor
                )
            }
            Text(
                modifier = Modifier
                    .clickable {
                        welcomeComposeViewModel.onPoliceLink()
                    },
                text = stringResource(id = R.string.welcome_privacy_policy),
                style = Typography.body1,
                fontSize = 12.sp,
                color = lightPalette.sliderIndicatorColor
            )
        }
    }
}