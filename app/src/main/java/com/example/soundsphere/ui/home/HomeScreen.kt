package com.example.soundsphere.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.soundsphere.R
import com.example.soundsphere.ui.components.BottomBar
import com.example.soundsphere.ui.components.ImageBoxExtraLarge
import com.example.soundsphere.ui.components.ImageBoxLarge
import com.example.soundsphere.ui.components.ImageBoxMedium
import com.example.soundsphere.ui.components.LocalImageRoundAvatar
import com.example.soundsphere.ui.theme.roboto
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint(
    "CoroutineCreationDuringComposition", "SuspiciousIndentation",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val recommendationState = viewModel.recommendationState.collectAsState()
    val newReleaseState = viewModel.newReleaseState.collectAsState()
    val albumsState = viewModel.albumsState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        bottomBar = {
            BottomBar(navController = navController)
        },
        containerColor = Color(0xFF1E1E1E),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (recommendationState.value.isLoading || newReleaseState.value.isLoading || albumsState.value.isLoading) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopUpHome(modifier)
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Featuring Today",
                    color = Color(0xBFFFFFFF),
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    fontSize = 27.sp,
                )
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (recommendationState.value.isSuccessDataRecommendation != null) {
                        val tracks =
                            recommendationState.value.isSuccessDataRecommendation!!.tracks
                        items(tracks.size) { item ->
                            val track = tracks[item]
                            val albumImages = track.album.images
                            if (albumImages.isNotEmpty()) {
                                val imageUrl =
                                    albumImages.first().url
                                ImageBoxExtraLarge(
                                    imageUrl = imageUrl,
                                    text = track.album.name
                                )
                            }
                        }
                    }
                }
            }


            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "New releases",
                    color = Color(0xBFFFFFFF),
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    fontSize = 27.sp,
                )

                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (newReleaseState.value.isSuccessDataNewRelease != null) {
                        val newReleases =
                            newReleaseState.value.isSuccessDataNewRelease!!.albums.items
                        Log.d("TAG", "HomeScreen1: ${newReleases.size}")
                        items(newReleases.size) { result ->
                            val album = newReleases[result]
                            val albumImages = album.images
                            if (albumImages.isNotEmpty()) {
                                val imageUrl =
                                    albumImages.first().url // Lấy hình ảnh đầu tiên của album
                                ImageBoxMedium(
                                    imageUrl = imageUrl,
                                    text = album.name
                                )
                            }

                        }

                    }
                }

            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Mixes for you",
                    color = Color(0xBFFFFFFF),
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    fontSize = 27.sp,
                )
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (albumsState.value.isSuccessDataAlbums != null) {
                        val albums = albumsState.value.isSuccessDataAlbums!!.albums
                        Log.d("TAG", "HomeScreen1: ${albums.size}")
                        items(albums.size) { result ->
                            val album = albums[result]
                            val albumImages = album.images
                            if (albumImages.isNotEmpty()) {
                                val imageUrl =
                                    albumImages.first().url // Lấy hình ảnh đầu tiên của album
                                ImageBoxLarge(
                                    imageUrl = imageUrl,
                                    text = album.name
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TopUpHome(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.15f)
            .padding(top = 30.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = "\uD83D\uDC4BHi Logan",
                color = Color(0xBFFFFFFF),
                fontFamily = roboto,
                fontWeight = FontWeight.Light,
                fontSize = 23.sp
            )
            Text(
                text = "Welcome Back!",
                color = Color(0xBFFFFFFF),
                fontFamily = roboto,
                fontWeight = FontWeight.Medium,
                fontSize = 27.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = modifier.size(35.dp),
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xBFFFFFFF)
            )
            LocalImageRoundAvatar(resourceId = R.drawable.avatar)
        }

    }

}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}