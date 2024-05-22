package com.example.soundsphere.ui.profile

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.soundsphere.R
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.ButtonComponent
import com.example.soundsphere.ui.components.RoundAvatar
import com.example.soundsphere.ui.theme.roboto

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val profileState = viewModel.profileState.collectAsState()
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
            val user = profileState.value.success
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
                Log.d("Profile", "ProfileScreen: ${profileState.value.success?.email}")

            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (user != null) {
                    RoundAvatar(
                        imageUrl = user.photoUrl.toString(),
                        modifier = modifier.size(120.dp)
                    ) {
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = user.displayName!!.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = roboto
                    )
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
                colorText =Color(0xFF121212), ) {
                viewModel.logOut()
                navController.navigate(NavigationRoutes.Login.route)
            }

        }
    }
}

@Composable
private fun YourLibrary(modifier: Modifier) {
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
                Text(text = "120 Songs",
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
                Text(text = "120 Songs",
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
                Text(text = "120 Songs",
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