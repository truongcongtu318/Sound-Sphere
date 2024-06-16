package com.example.soundsphere.ui.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.soundsphere.R
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.components.ButtonComponent
import com.example.soundsphere.ui.components.ReAuthDialog
import com.example.soundsphere.ui.components.RoundAvatar
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.theme.roboto
import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "LogNotTimber", "UnrememberedMutableState"
)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    playViewModel: PlayViewModel,
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDialog = mutableStateOf(false)
    val showDialogReAuth = mutableStateOf(false)

    val isEmailVerified by authViewModel.isEmailVerified.collectAsState()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()
    authViewModel.checkUserAuthentication()
    if (isUserAuthenticated) {
        authViewModel.checkEmailVerification()
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFF121212),
        contentColor = Color(0xBFFFFFFF)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 60.dp)
        ) {
            val user = FirebaseAuth.getInstance().currentUser
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My Profile",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )

            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (user != null) {
                    RoundAvatar(
                        imageUrl = (user.photoUrl
                            ?: "https://inkythuatso.com/uploads/thumbnails/800/2023/03/9-anh-dai-dien-trang-inkythuatso-03-15-27-03.jpg").toString(),
                        modifier = modifier.size(90.dp)
                    ) {
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    user.displayName?.let { it1 ->
                        Text(
                            text = it1,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = roboto
                        )
                    }
                }
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${user?.email}",
                    fontSize = 16.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    color = Color(0x80FFFFFF)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Phone number",
                    fontSize = 20.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = user?.phoneNumber ?: "No phone number",
                    fontSize = 16.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    color = Color(0x80FFFFFF)
                )
            }

            YourLibrary(modifier)
            Spacer(modifier = Modifier.height(30.dp))

            ButtonComponent(
                value = "Log out",
                color = Color(0x80FFFFFF),
                colorText = Color(0xFF121212),
            ) {
                authViewModel.logout()
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                navController.navigate(NavigationRoutes.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            ButtonComponent(
                value = "Delete Account",
                color = Color(0x80FFFFFF),
                colorText = Color(0xFF121212),
            ) {
                showDialog.value = true
            }


            DeleteConfirmationDialog(
                showDialog = showDialog,
                onConfirm = {
                    showDialog.value = false
                    showDialogReAuth.value = true
                },
                onDismiss = {
                    showDialog.value = false
                }
            )
            ReAuthDialog(
                onReAuth = { email, password ->
                    authViewModel.deleteUser(email, password, {
                        navController.navigate(NavigationRoutes.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                        Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                    }, { exception ->
                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                    })
            }, showDialog = showDialogReAuth) {
                showDialogReAuth.value = false
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun DeleteConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            iconContentColor = Color.White,
            containerColor = Color(0xFF121212),
            textContentColor = Color.White,
            shape = MaterialTheme.shapes.small,
            tonalElevation = 0.dp,
            title = {
                Text(
                    "Confirmation deletion", color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete your account?",
                    color = Color(0x80FFFFFF),
                    fontSize = 16.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                )
            },

            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF121212)
                    ),
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Delete",
                        color = Color(0x80FFFFFF),
                        fontSize = 16.sp,
                        fontFamily = roboto,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF121212)
                    ),
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Cancel",
                        color = Color(0x80FFFFFF),
                        fontSize = 16.sp,
                        fontFamily = roboto,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }
}

@Composable
fun YourLibrary(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        Box(
            modifier = modifier
                .fillMaxWidth(0.27f)
                .height(100.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(28.dp)),
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x3EFFFFFF),
                                Color(0xFF000000)
                            )
                        ),
                        shape = RoundedCornerShape(17.dp)
                    ),
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    modifier = modifier
                        .padding(bottom = 10.dp)
                        .size(30.dp),
                    colorFilter = ColorFilter.tint(Color(0xBFFFFFFF))
                )
                Text(
                    text = "120 Songs",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    color = Color(0x80FFFFFF)
                )
            }

        }


        Box(
            modifier = modifier
                .fillMaxWidth(0.6f)
                .height(100.dp)
                .padding(horizontal = 30.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(28.dp))
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x3EFFFFFF),
                                Color(0xFF000000)
                            )
                        ),
                        shape = RoundedCornerShape(17.dp)
                    )
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    imageVector = Icons.Filled.QueueMusic,
                    contentDescription = null,
                    modifier = modifier
                        .padding(bottom = 10.dp)
                        .size(30.dp),
                    colorFilter = ColorFilter.tint(Color(0xBFFFFFFF))
                )
                Text(
                    text = "120 Songs",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    color = Color(0x80FFFFFF)
                )
            }

        }
        Box(
            modifier = modifier
                .fillMaxWidth(1f)
                .height(100.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(28.dp))
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x3EFFFFFF),
                                Color(0xFF000000)
                            )
                        ),
                        shape = RoundedCornerShape(17.dp)
                    )
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vector_),
                    contentDescription = null,
                    modifier = modifier
                        .padding(bottom = 10.dp)
                        .size(30.dp),
                    colorFilter = ColorFilter.tint(Color(0xBFFFFFFF))
                )
                Text(
                    text = "120 Songs",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    color = Color(0x80FFFFFF)
                )
            }

        }

    }
}