package com.example.soundsphere.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.ImageBoxExtraLarge
import com.example.soundsphere.ui.components.ImageBoxLarge
import com.example.soundsphere.ui.components.ImageBoxMedium
import com.example.soundsphere.ui.components.RoundAvatar
import com.example.soundsphere.ui.profile.ProfileState
import com.example.soundsphere.ui.profile.ProfileViewModel
import com.example.soundsphere.ui.theme.roboto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class, ExperimentalFoundationApi::class)
@SuppressLint(
    "CoroutineCreationDuringComposition", "SuspiciousIndentation",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val recommendationState = viewModel.recommendationState.collectAsState()
    val newReleaseState = viewModel.newReleaseState.collectAsState()
    val albumsState = viewModel.albumsState.collectAsState()
    val featurePlayListState = viewModel.playlistFeatureState.collectAsState()
    val profileState = profileViewModel.profileState.collectAsState()
    val albumByIdState = viewModel.albumByIdState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val charts = viewModel.chartState.collectAsState()
    Log.d("123", charts.value.toString())


    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFF121212),
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
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                TopUpHome(modifier, profileState, navController)
            }
            item {
                RecommendationList(modifier, recommendationState, navController, scope, context)
            }
            item {
                NewReleaseList(modifier, newReleaseState, navController, scope, context)
            }
            item {
                MixesForYouList(modifier, albumsState, navController, scope, context)
            }
            item {
                PlayListForYou(modifier, featurePlayListState, navController, scope, context)
            }
        }
    }
}

@Composable
private fun PlayListForYou(
    modifier: Modifier = Modifier,
    featurePlayListState: State<FeaturePlayListState>,
    navController: NavHostController,
    scope: CoroutineScope,
    context: Context
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 120.dp)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Feature Playlist For You",
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
            if (featurePlayListState.value.isSuccessDataPlayListFeature != null) {
                val playlists =
                    featurePlayListState.value.isSuccessDataPlayListFeature!!.playlists.items
                items(playlists.size) { result ->
                    val playlist = playlists[result]
                    val playlistImages = playlist.images
                    if (playlistImages.isNotEmpty()) {
                        val imageUrl =
                            playlistImages.first().url
                        ImageBoxLarge(
                            imageUrl = imageUrl,
                            text = playlist.name
                        ) {
                            val id = playlist.id
                            Log.d("id999", id)
                            scope.launch {
                                navController.navigate(NavigationRoutes.SongListPlayList.route + "/$id")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MixesForYouList(
    modifier: Modifier,
    albumsState: State<AlbumsState>,
    navController: NavHostController,
    scope: CoroutineScope,
    context: Context
) {
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
                items(albums) { album ->
                    ImageBoxLarge(
                        imageUrl = album.images.firstOrNull()!!.url,
                        text = album.name
                    ) {
                        val id = album.id
                        scope.launch {
                            navController.navigate(NavigationRoutes.SongList.route + "/$id")
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun NewReleaseList(
    modifier: Modifier,
    newReleaseState: State<NewReleaseState>,
    navController: NavHostController,
    scope: CoroutineScope,
    context: Context
) {
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
                            albumImages.first().url
                        ImageBoxMedium(
                            imageUrl = imageUrl,
                            text = album.name
                        ) {
                            val id = album.id
                            scope.launch {
                                navController.navigate(NavigationRoutes.SongList.route + "/$id")
                            }

                        }
                    }
                }

            }
        }

    }
}

@Composable
private fun RecommendationList(
    modifier: Modifier,
    recommendationState: State<RecommendationState>,
    navController: NavHostController,
    scope: CoroutineScope,
    context: Context,
    viewModel: HomeViewModel = hiltViewModel()
) {
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
                        ) {
                            val id = track.album.id
                            Log.d("TAG", "RecommendationList: $id")
                            scope.launch {
                                navController.navigate(NavigationRoutes.SongList.route + "/$id")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TopUpHome(
    modifier: Modifier,
    profileState: State<ProfileState>,
    navController: NavHostController
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.15f)
            .background(Color(0xFF121212))
            .padding(top = 30.dp, bottom = 20.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val user = profileState.value.success
        Column(
            modifier = modifier
        ) {

            if (user != null) {
                Text(
                    text = "\uD83D\uDC4BHi ${user.displayName}",
                    color = Color(0xBFFFFFFF),
                    fontFamily = roboto,
                    fontWeight = FontWeight.Light,
                    fontSize = 23.sp
                )
            }

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
            RoundAvatar(imageUrl = user?.photoUrl.toString()) {
                navController.navigate(NavigationRoutes.Profile.route)
            }
        }

    }

}


@Preview
@Composable
fun HomeScreenPreview() {

}