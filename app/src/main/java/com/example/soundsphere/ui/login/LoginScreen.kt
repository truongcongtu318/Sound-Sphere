package com.example.soundsphere.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.soundsphere.R
import com.example.soundsphere.navigation.BottomBarRoutes
import com.example.soundsphere.ui.components.ButtonComponent
import com.example.soundsphere.ui.components.SocialMediaLogIn
import com.example.soundsphere.ui.theme.roboto
import com.example.soundsphere.utils.Constants
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LoginViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val result = account.getResult(ApiException::class.java)
            val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
            viewModel.loginWithGoogle(credentials)
        } catch (it: ApiException) {
            print(it)
        }
    }
    val googleState = viewModel.googleState.value
    val facebookState = viewModel.facebookState.value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    remember {
        CallbackManager.Factory.create()
    }

    LaunchedEffect(Unit) {
        scope.launch {
            val token = AccessToken.getCurrentAccessToken()
            if (token != null) {
                viewModel.loginWithFacebook(token)
            }
            Log.d("TAG123", facebookState.toString())
        }

    }

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
                if (facebookState.success != null) {
                    navController.navigate(BottomBarRoutes.Home.route)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SocialMediaLogIn(
                icon = R.drawable.google,
                text = "Continue with Google",
                color = Color(0xBFFFFFFF)
            ) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(Constants.CLIENT_ID)
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (googleState.loading) {
                    CircularProgressIndicator()
                }
            }

            LaunchedEffect(key1 = googleState.success) {
                scope.launch {
                    if (googleState.success != null) {
                        navController.navigate(BottomBarRoutes.Home.route)
                    }

                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SocialMediaLogIn(
                icon = R.drawable.facebook,
                text = "Continue with Facebook",
                color = Color(0xBFFFFFFF)
            ) {
                LoginManager.getInstance().logInWithReadPermissions(
                    context as Activity,
                    listOf("email","public_profile"),
                )
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
    LoginScreen()
}