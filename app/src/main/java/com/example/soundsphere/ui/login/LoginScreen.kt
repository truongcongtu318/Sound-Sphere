package com.example.soundsphere.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.soundsphere.MainActivity
import com.example.soundsphere.R
import com.example.soundsphere.navigation.BottomBarRoutes
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.components.ButtonComponent
import com.example.soundsphere.ui.components.SocialMediaLogIn
import com.example.soundsphere.ui.theme.roboto
import com.example.soundsphere.utils.Constants
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("StateFlowValueCalledInComposition", "LogNotTimber")
@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current as MainActivity

    // Google Login launcher
    val googleLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val googleAccount = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
                authViewModel.loginWithGoogle(credentials)
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign in failed", e)
            }
        }


    val googleState = authViewModel.googleState.value
    val facebookState = authViewModel.facebookState.collectAsState().value

    val isEmailVerified by authViewModel.isEmailVerified.collectAsState()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()


    // Facebook Login launcher
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {

    }
    authViewModel.checkUserAuthentication()
    LaunchedEffect(isUserAuthenticated) {
        if (isUserAuthenticated) {
            authViewModel.checkEmailVerification()
        }
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException) {

            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    authViewModel.loginWithFacebook(result.accessToken)
                }
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }
    LaunchedEffect(key1 = facebookState.success) {
        if (facebookState.success != null) {
            navController.navigate(BottomBarRoutes.Home.route)
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
        }
    }


    if (googleState.loading || facebookState.loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }else{
        Column(
            modifier = modifier
                .background(Color(0xFF121212))
                .fillMaxSize()
        ) {


            Column(
                modifier = modifier
                    .background(color = Color(0xFF121212))
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
                    navController.navigate(NavigationRoutes.Register.route)
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
                    googleSignInClient.signOut().addOnCompleteListener {
                        googleLauncher.launch(googleSignInClient.signInIntent)
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
                    launcher.launch(listOf("email", "public_profile"))
                }

                Spacer(modifier = Modifier.height(20.dp))
                ButtonComponent(
                    value = "Log in",
                    color = Color.Transparent,
                    colorText = Color(0xBFFFFFFF)
                ) {
                    navController.navigate(NavigationRoutes.LoginEmail.route)
                }
            }
        }
    }



}

@Preview
@Composable
fun SplashScreenPreview() {
    // Preview implementation
}
