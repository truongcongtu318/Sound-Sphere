package com.example.soundsphere.ui.song_list

import android.annotation.SuppressLint
import androidx.annotation.OptIn
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
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.R
import com.example.soundsphere.data.dtodeezer.albumtracks.Data
import com.example.soundsphere.data.model.Track
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.ImageBoxSongList
import com.example.soundsphere.ui.home.HomeViewModel
import com.example.soundsphere.ui.home.encodeUrl
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.play.UIEvents
import com.example.soundsphere.ui.play.UiState
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto

fun Data.toTrack(): Track {
    return Track(
        id = this.id.toString(),
        artist = this.artist,
        disk_number = this.disk_number ?: 0,
        duration = (this.duration ?: 0).toString(),
        isrc = this.isrc ?: "",
        link = this.link ?: "",
        md5_image = this.md5_image ?: "",
        preview = this.preview ?: "",
        rank = (this.rank ?: 0).toString(),
        readable = this.readable ?: false,
        title = this.title ?: "",
        title_short = this.title_short ?: "",
        title_version = this.title_version ?: "",
        track_position = this.track_position,
        type = this.type ?: ""
    )
}
@OptIn(UnstableApi::class)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "LogNotTimber"
)
@Composable
fun SongListScreen(
    urlTrackList: String,
    urlAlbum: String,
    viewModel: HomeViewModel,
    playViewModel: PlayViewModel,
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {


    val albumTracksState = viewModel.albumTracksState.collectAsState()
    val albumState = viewModel.albumState.collectAsState()
    val trackList = albumTracksState.value.isSuccessful?.data?.map {
        it.toTrack()
    }
    Log.d("TAG", "Current Selected Track: ${playViewModel.currentSelectedTrack}")
    playViewModel.trackList = trackList.orEmpty()
    playViewModel.trackListUrl = encodeUrl(urlTrackList)
    LaunchedEffect(Unit) {
        viewModel.getAlbumTracks(urlTrackList)
        viewModel.getAlbum(urlAlbum)
    }


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
            if (albumState.value.isLoading || albumTracksState.value.isLoading) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            val albumTrack = albumTracksState.value.isSuccessful
            val album = albumState.value.isSuccessful
            if (album != null) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.27f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ImageBoxSongList(
                        imageUrl = album.cover_big,
                        text = album.title,
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
                            text = "Type: ${album.type}",
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
                            text = "${album.tracks.data.size} songs",
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
                                imageVector = Icons.Filled.FavoriteBorder,
                                contentDescription = null,
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
                            imageVector = if (playViewModel.isPlaying) Icons.Filled.PauseCircleFilled
                                    else Icons.Filled.PlayCircleFilled,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color(0xBFFFFFFF)),
                            modifier = modifier
                                .size(60.dp)
                                .clickable {
                                    if (playViewModel.uiState.value == UiState.Initial) {
                                        trackList?.let { playViewModel.setCurrentTrackSelected(it.first()) }
                                    }
                                    playViewModel.onUiEvent(UIEvents.PlayPause)
                                }
                        )
                    }
                }
            }


            //List
            if (albumTrack != null) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
                    val lastUrl = "/500x500-000000-80-0-0.jpg"
                    items(trackList.orEmpty()) { track ->
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(horizontal = 20.dp)
                                .clickable {
                                    playViewModel.setCurrentTrackSelected(track)
                                    playViewModel.onUiEvent(UIEvents.PlayPause)
                                },
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
                                        val url = encodeUrl(urlTrackList)
                                        navController.navigate(NavigationRoutes.PlayTrack.route + "/${track.id}" + "/$url")
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
                                    text = track.artist?.name ?: "",
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
        }

    }
}


@Preview
@Composable
fun SongListScreenPreview() {

}