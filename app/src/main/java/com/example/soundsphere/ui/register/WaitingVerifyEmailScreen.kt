package com.example.soundsphere.ui.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.theme.roboto

@Composable
fun WaitingVerifyEmailScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    val context = LocalContext.current

    val isEmailVerified by authViewModel.isEmailVerified.collectAsState()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()

    authViewModel.checkEmailVerification()
    LaunchedEffect(isEmailVerified) {
        if (isEmailVerified) {
            navController.navigate(NavigationRoutes.Login.route) {
                popUpTo(NavigationRoutes.WaitingVerifyEmailScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please check your email to verify your account.",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                authViewModel.sendEmailVerification()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF777777)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Resend Verification Link",
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = roboto,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate(NavigationRoutes.Login.route) {
                    popUpTo(NavigationRoutes.WaitingVerifyEmailScreen.route) {
                        inclusive = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF777777)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Reload",
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = roboto,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
