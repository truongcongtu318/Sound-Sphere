package com.example.soundsphere.ui.song_list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.R
import com.example.soundsphere.data.dtodeezer.album.AlbumDto
import com.example.soundsphere.data.dtodeezer.albumtracks.AlbumTrackDto
import com.example.soundsphere.data.dtodeezer.artist_top.ArtistTopDto
import com.example.soundsphere.data.dtodeezer.playlist.PlayListDto
import com.example.soundsphere.data.dtodeezer.playlisttracks.PlayListTrackDto
import com.example.soundsphere.data.dtodeezer.track.TrackDto
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.ImageBoxSongList
import com.example.soundsphere.ui.home.HomeViewModel
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun SongListScreenArtist(
    urlTrackList: String,
    songListViewModel: SongListViewModel = hiltViewModel(),
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val artistTopState = songListViewModel.artistTopState.collectAsState()
    LaunchedEffect(Unit) {
        songListViewModel.getArtistTop(urlTrackList)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF121212)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (artistTopState.value.isLoading) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            val artistTop = artistTopState.value.isSuccessful

            if (artistTop != null) {
                TopUpList(modifier, artistTop = artistTop)
            }
            if (artistTop != null) {
                SongList(
                    modifier,
                    trackList = artistTop,
                    navController = navController,
                )
            }
        }

    }
}

@SuppressLint("RememberReturnType")
@Composable
private fun SongList(
    modifier: Modifier,
    trackList: ArtistTopDto,
    navController: NavHostController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(1f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val tracks = trackList.data
        val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
        val lastUrl = "/500x500-000000-80-0-0.jpg"
        items(tracks) { track ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                Image(

                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = baseUrl + track.md5_image + lastUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                            }).build()
                    ),
                    contentDescription = "Playlist Cover",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(50.dp)
                        .clip(shape = RoundedCornerShape(5.dp))
                        .clickable {
                            navController.navigate(NavigationRoutes.PlayTrack.route + "/${track.id}")
                        }
                )

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = track.title ?: "",
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Medium,
                        fontFamily = fontInter,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = track.artist.name ?: "",
                        color = Color(0x80FFFFFF),
                        fontWeight = FontWeight.Medium,
                        fontFamily = fontInter,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )

                }
            }

        }
    }

}


@Composable
private fun TopUpList(
    modifier: Modifier,
    artistTop: ArtistTopDto,
) {
    val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
    val lastUrl = "/500x500-000000-80-0-0.jpg"
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.27f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ImageBoxSongList(
            imageUrl = baseUrl + artistTop.data.firstOrNull()!!.md5_image + lastUrl,
            text = artistTop.data.firstOrNull()!!.album.title,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(0.dp))
        )
    }
    Column(modifier = modifier.padding(20.dp)) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Type: ${artistTop.data.firstOrNull()!!.type}",
                color = Color(0xBFFFFFFF),
                fontWeight = FontWeight.Medium,
                fontFamily = roboto,
                fontSize = 16.sp
            )
            Image(
                painter = painterResource(id = R.drawable.ellipse),
                contentDescription = null,
                modifier = modifier.size(5.dp)
            )

            Text(
                text = "${artistTop.data.size} songs",
                color = Color(0xBFFFFFFF),
                fontWeight = FontWeight.Medium,
                fontFamily = roboto,
                fontSize = 16.sp
            )

        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    imageVector = Icons.Filled.FavoriteBorder, contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xBFFFFFFF)),
                    modifier = modifier
                        .padding(top = 10.dp)
                        .size(30.dp)
                )
                Image(
                    imageVector = Icons.Filled.Downloading, contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xBFFFFFFF)),
                    modifier = modifier
                        .padding(top = 10.dp)
                        .size(30.dp)
                )
                Image(
                    imageVector = Icons.Filled.Share, contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xBFFFFFFF)),
                    modifier = modifier
                        .padding(top = 10.dp)
                        .size(30.dp)
                )
            }
            Image(
                imageVector = Icons.Filled.PlayCircleFilled,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color(0xBFFFFFFF)),
                modifier = modifier.size(60.dp)
            )
        }
    }
}

