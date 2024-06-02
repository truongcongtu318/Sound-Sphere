package com.example.soundsphere.ui.play

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.MediaController.MediaPlayerControl
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util.startForegroundService
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.navigation.NavHostController
import com.example.soundsphere.R
import com.example.soundsphere.player.service.JetAudioService
import com.example.soundsphere.ui.components.ImageBoxPlay
import com.example.soundsphere.ui.home.HomeViewModel
import com.example.soundsphere.ui.song_list.SongListViewModel
import com.example.soundsphere.ui.theme.fontInter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import okhttp3.internal.concurrent.formatDuration

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("LogNotTimber")
@Composable
fun PlayScreen(
    navController: NavHostController,
    songListViewModel: SongListViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    playViewModel: PlayViewModel = hiltViewModel(),
    id: String?,
    urlTrackList: String?,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    var isServiceRunning = false
    fun StartService(){
        if (!isServiceRunning){
            val intent = Intent(context, JetAudioService::class.java)
            startForegroundService(context,intent)
            isServiceRunning = true
        }
    }


    val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
    val lastUrl = "/500x500-000000-80-0-0.jpg"

    val trackState = songListViewModel.trackState.collectAsState()
    var currentSecond by remember { mutableStateOf(0) }

    val trackListState = homeViewModel.albumTracksState.collectAsState()
    val trackList = trackListState.value.isSuccessful?.data
    val track = trackState.value.isSuccessful

    LaunchedEffect(Unit) {
        if (id != null) {
            songListViewModel.getTrack(id)
        }

        if (urlTrackList != null) {
            homeViewModel.getAlbumTracks(urlTrackList)
            val dataTrack = trackList?.find { it.id == track?.id }
            dataTrack?.also {
                playViewModel.setCurrentTrackSelected(it)
            }
            playViewModel.loadTrackList(urlTrackList ?: "")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(50.dp))

        IconButton(modifier = Modifier
            .align(Alignment.Start)
            .size(50.dp), onClick = {
            navController.navigateUp()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                contentDescription = null,
                modifier = modifier.size(50.dp),
                tint = Color(0xBFFFFFFF)
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        Column(
            modifier = modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.9f)
                .padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (track != null) {
                ImageBoxPlay(
                    modifier = modifier
                        .size(380.dp)
                        .align(Alignment.CenterHorizontally),
                    imageUrl = (baseUrl + track.md5_image + lastUrl)
                )

                Spacer(modifier = modifier.height(40.dp))

                Text(
                    text = track.title_short,
                    color = Color(0xFFFFFFFF),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = fontInter,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = modifier.height(10.dp))

                Text(
                    text = track.artist.name,
                    color = Color(0xBFFFFFFF),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = fontInter,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Spacer(modifier = modifier.height(0.dp))

            Slider(
                value = playViewModel.progress,
                onValueChange = { playViewModel.onUiEvent(UIEvents.SeekTo(it)) },
                valueRange = 1f..100f,
                modifier = Modifier.padding(vertical = 0.dp),
                colors = SliderDefaults.colors().copy(
                    thumbColor = Color(0xBFFFFFFF), activeTrackColor = Color(0xBFFFFFFF)
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = playViewModel.progressString, color = Color.White)
                Text(text = playViewModel.formatDuration(30), color = Color.White)
            }

            Spacer(modifier = modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    playViewModel.onUiEvent(UIEvents.Backward)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = modifier.size(40.dp),
                        tint = Color(0xBFFFFFFF)
                    )

                }
                IconButton(modifier = modifier.size(60.dp), onClick = {
                    playViewModel.onUiEvent(UIEvents.PlayPause)
                    StartService()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.play_arrow__1_),
                        contentDescription = null,
                        modifier = modifier.size(200.dp),
                        tint = Color(0xBFFFFFFF)
                    )

                }
                IconButton(onClick = { playViewModel.onUiEvent(UIEvents.SeekToNext) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = modifier.size(40.dp),
                        tint = Color(0xBFFFFFFF)
                    )
                }
            }
        }

    }
}