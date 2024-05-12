package com.example.soundsphere.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundsphere.R
import com.example.soundsphere.ui.components.ButtonComponent
import com.example.soundsphere.ui.components.SocialMediaLogIn
import com.example.soundsphere.ui.theme.roboto

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFF1E1E1E))
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .background(color = Color(0xFF1E1E1E))
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Image(
                modifier = modifier
                    .width(260.dp)
                    .height(100.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )
            Spacer(modifier = modifier.height(28.dp))
            Text(
                text = "Just keep on vibin'",
                color = Color(0x80FFFFFF),
                fontWeight = FontWeight.Medium,
                fontFamily = roboto,
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonComponent(
                value = "Sign up",
                color = Color(0xBFFFFFFF),
                colorText = Color(0xBF000000)
            ) {

            }
            Spacer(modifier = Modifier.height(20.dp))
            SocialMediaLogIn(
                icon = R.drawable.google,
                text = "Continue with Google",
                color = Color(0xBFFFFFFF)
            ) {

            }
            Spacer(modifier = Modifier.height(20.dp))
            SocialMediaLogIn(
                icon = R.drawable.facebook,
                text = "Continue with Facebook",
                color = Color(0xBFFFFFFF)
            ) {

            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonComponent(
                value = "Log in",
                color = Color.Transparent,
                colorText = Color(0xBFFFFFFF)
            ) {

            }
        }
    }
}


@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}