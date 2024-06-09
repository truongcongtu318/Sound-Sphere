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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.R
import com.example.soundsphere.data.dtodeezer.playlisttracks.Data
import com.example.soundsphere.data.model.Track
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.ImageBoxSongList
import com.example.soundsphere.ui.home.encodeUrl
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.play.UIEvents
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto

fun com.example.soundsphere.data.dtodeezer.playlist.Data.toTrack(): Track {
    return Track(
        id = this.id,
        artist = this.artist,
        duration = this.duration,
        link = this.link,
        md5_image = this.md5_image,
        preview = this.preview,
        rank = this.rank,
        readable = this.readable,
        title = this.title,
        title_short = this.title_short,
        title_version = this.title_version,
        type = this.type
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun SongListScreenPlayList(
    urlTrackList: String,
    idPlayList: String,
    songListViewModel: SongListViewModel,
    navController: NavHostController,
    playViewModel: PlayViewModel,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val playListTrackState = songListViewModel.playListTrackState.collectAsState()
    val playListState = songListViewModel.playListState.collectAsState()
    val trackList = playListState.value.isSuccessful?.tracks?.data?.map {
        it.toTrack()
    }
    playViewModel.trackList = trackList.orEmpty()
    LaunchedEffect(Unit) {
        songListViewModel.getPlayListTracks(urlTrackList)
        songListViewModel.getPlayList(idPlayList)
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
            if (playListState.value.isLoading || playListTrackState.value.isLoading) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            val playListTrack = playListTrackState.value.isSuccessful
            val playlist = playListState.value.isSuccessful
            if (playlist != null) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.27f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ImageBoxSongList(
                        imageUrl = playlist.picture_big,
                        text = playlist.title,
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
                            text = "Type: ${playlist.type}",
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
                            text = "${playlist.tracks.data.size} songs",
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
                            imageVector = Icons.Filled.PlayCircleFilled,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color(0xBFFFFFFF)),
                            modifier = modifier
                                .size(60.dp)
                                .clickable {
                                    trackList?.let { it1 ->
                                        playViewModel.setCurrentTrackSelected(
                                            it1.first()
                                        )
                                    }
                                    playViewModel.onUiEvent(UIEvents.PlayPause)
                                }
                        )
                    }
                }
            }
            if (playListTrack != null) {
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
                        if (track.preview == "") return@items
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(horizontal = 20.dp)
                                .clickable {
                                    Log.d("TrackList", "Preview: ${track.preview}")
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
                                        navController.navigate(NavigationRoutes.PlayTrack.route)
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

                                track.artist?.let { it1 ->
                                    Text(
                                        text = it1.name,
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
}

