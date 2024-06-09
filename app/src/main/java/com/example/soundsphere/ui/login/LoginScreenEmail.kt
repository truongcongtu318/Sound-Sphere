package com.example.soundsphere.ui.login

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.soundsphere.navigation.BottomBarRoutes
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.components.ButtonComponent
import com.example.soundsphere.ui.components.MyTextField
import com.example.soundsphere.ui.register.RegisterViewModel
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("LogNotTimber")
@Composable
fun LoginScreenEmail(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val loginState = authViewModel.loginState.collectAsState().value

    LaunchedEffect(loginState) {
        if (loginState.isSuccess != null && FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
            navController.navigate(BottomBarRoutes.Home.route) {
                popUpTo(NavigationRoutes.Login.route) { inclusive = true }
            }
            Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
        } else if(loginState.isError != "") {
            authViewModel.sendEmailVerification()
            Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFF121212))
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(modifier = Modifier
                .size(50.dp), onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                    contentDescription = null,
                    modifier = modifier.size(50.dp),
                    tint = Color(0xBFFFFFFF)
                )
            }

            Text(
                modifier = modifier
                    .fillMaxWidth(1f)
                    .padding(end = 30.dp),
                text = "Login with email",
                color = Color(0xFFFFFFFF),
                fontSize = 18.sp,
                fontFamily = roboto,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(start = 15.dp),
            text = "What's your email?",
            color = Color(0xFFFFFFFF),
            fontSize = 20.sp,
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(
            value = email,
            onValueChange = { email = it },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(start = 15.dp),
            text = "Password",
            color = Color(0xFFFFFFFF),
            fontSize = 20.sp,
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(
            value = password,
            onValueChange = { password = it },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(start = 15.dp),
            text = "Confirm your password",
            color = Color(0xBFFFFFFF),
            fontSize = 20.sp,
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(40.dp))

        ButtonComponent(
            value = "Login",
            modifier = modifier
                .width(150.dp)
                .height(45.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 20.dp),
            color = Color(0xFF777777),
            colorText = Color(0xFF000000),
            onClicked = {
                authViewModel.loginWithEmailAndPassword(email, password)
            }


        )

    }
}

@Composable
@Preview
fun RegisterScreenPreview() {

}