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
import com.example.soundsphere.data.dtodeezer.chart.Album
import com.example.soundsphere.data.dtodeezer.chart.Albums
import com.example.soundsphere.data.dtodeezer.chart.Artists
import com.example.soundsphere.data.dtodeezer.chart.Playlists
import com.example.soundsphere.data.dtodeezer.chart.Podcasts
import com.example.soundsphere.data.dtodeezer.chart.Tracks
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(DelicateCoroutinesApi::class, ExperimentalFoundationApi::class)
@SuppressLint(
    "CoroutineCreationDuringComposition", "SuspiciousIndentation",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val profileState = profileViewModel.profileState.collectAsState()
    val chartState = viewModel.chartState.collectAsState()

    val chartTracks = chartState.value.isSuccessful?.tracks
    val chartAlbums = chartState.value.isSuccessful?.albums
    val chartPlaylists = chartState.value.isSuccessful?.playlists
    val chartArtists = chartState.value.isSuccessful?.artists
    val chartPodcasts = chartState.value.isSuccessful?.podcasts

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
            if (chartState.value.isLoading) {
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
                TopTrackList(modifier, chartTracks, navController, scope)
            }
            item {
                TopAlbumList(modifier, chartAlbums, navController, scope)
            }
            item {
                TopPlaylistsList(modifier, chartPlaylists, navController, scope)
            }
            item {
                TopArtistList(modifier, chartArtists, navController, scope)
            }
        }
    }
}


@Composable
private fun TopArtistList(
    modifier: Modifier = Modifier,
    chartArtists: Artists?,
    navController: NavHostController,
    scope: CoroutineScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Top Artists",
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
            if (chartArtists != null) {
                items(chartArtists.data) { artist ->
                    ImageBoxLarge(
                        imageUrl = artist.picture_xl,
                        text = artist.name
                    ) {
                        val urlTrackList = artist.tracklist
                        val encodeUrlTrackList = encodeUrl(urlTrackList)
                        scope.launch {
                            scope.launch {
                                navController.navigate("songlistartist/$encodeUrlTrackList")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopPlaylistsList(
    modifier: Modifier,
    chartPlaylists: Playlists?,
    navController: NavHostController,
    scope: CoroutineScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Top Playlists",
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
            if (chartPlaylists != null) {
                items(chartPlaylists.data) { playlist ->
                    ImageBoxLarge(
                        imageUrl = playlist.picture_big,
                        text = playlist.title
                    ) {
                        val urlTrackList = playlist.tracklist
                        val idPlayList = playlist.id
                        val encodeUrlTrackList = encodeUrl(urlTrackList)
                        Log.d("URL", "TopPlaylistsList: $encodeUrlTrackList")

                        scope.launch {
                            navController.navigate("songlistplaylist/$encodeUrlTrackList/$idPlayList")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAlbumList(
    modifier: Modifier,
    chartAlbums: Albums?,
    navController: NavHostController,
    scope: CoroutineScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Top Albums",
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
            if (chartAlbums != null) {
                items(chartAlbums.data) { album ->
                    ImageBoxMedium(
                        imageUrl = album.cover_big,
                        text = album.title
                    ) {
                        val urlTrackList = album.tracklist
                        val urlAlbum = album.id
                        val encodeUrlTrackList = encodeUrl(urlTrackList)

                        scope.launch {
                            navController.navigate("songlist/$encodeUrlTrackList/$urlAlbum")
                        }
                    }
                }
            }

        }
    }

}


fun encodeUrl(url: String): String {
    return URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
}

@Composable
private fun TopTrackList(
    modifier: Modifier,
    chartTrack: Tracks?,
    navController: NavHostController,
    scope: CoroutineScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Top Tracks",
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
            val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
            val lastUrl = "/500x500-000000-80-0-0.jpg"
            if (chartTrack != null) {
                items(chartTrack.data) { track ->

                    ImageBoxExtraLarge(
                        imageUrl = baseUrl + track.md5_image + lastUrl,
                        text = track.title
                    ) {
                        val id = track.id
                        scope.launch {
                            navController.navigate(NavigationRoutes.PlayTrack.route + "/${track.id}")
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
            .padding(horizontal = 15.dp),
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


//@Preview
//@Composable
//fun HomeScreenPreview() {
//