package com.example.soundsphere.ui.play

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import com.example.soundsphere.R
import com.example.soundsphere.ui.components.ImageBoxPlay
import com.example.soundsphere.ui.song_list.SongListViewModel
import com.example.soundsphere.ui.theme.fontInter
import okhttp3.internal.concurrent.formatDuration

@SuppressLint("LogNotTimber")
@Composable
fun PlayScreen(
    navController: NavHostController,
    songListViewModel: SongListViewModel = hiltViewModel(),
    id: String?,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    LaunchedEffect(id) {
        if (id != null) {
            songListViewModel.getTrackById(id)
        }
    }
    val trackByIdState = songListViewModel.trackByIdState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentSecond by remember { mutableStateOf(0) }
    val duration = 20f

    var isPlaying by remember { mutableStateOf(false) }

    val tracks = trackByIdState.value.track
    Log.d("999", "PlayScreen: ${trackByIdState.value}")

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> isPlaying = true
                        Player.STATE_ENDED -> isPlaying = false
                        Player.STATE_IDLE -> isPlaying = false
                        Player.STATE_BUFFERING -> {} // Handle buffering state if needed
                    }
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    LaunchedEffect(tracks) {
        tracks?.let {
            player.setMediaItem(MediaItem.fromUri(it.preview_url.toUri()))
            player.prepare()
        }
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(50.dp))

        IconButton(
            modifier = Modifier
                .align(Alignment.Start)
                .size(50.dp),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = null,
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
            if (tracks != null) {
                ImageBoxPlay(
                    modifier = modifier
                        .size(380.dp)
                        .align(Alignment.CenterHorizontally),
                    imageUrl = tracks.preview_url
                        ?: "https://i.scdn.co/image/ab67616d0000b273d8601e15fa1b4351fe1fc6ae",
                )

                Spacer(modifier = modifier.height(40.dp))

                Text(
                    text = tracks.name,
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
                    text = tracks.artist ?: "Son Tung",
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
                value = currentSecond.toInt().toFloat(),
                onValueChange = { currentSecond = it.toInt() },
                valueRange = currentSecond.toFloat()..duration.toFloat(),
                modifier = Modifier.padding(vertical = 0.dp),
                colors = SliderDefaults.colors().copy(
                    thumbColor = Color(0xBFFFFFFF),
                    activeTrackColor = Color(0xBFFFFFFF)
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = formatDuration(10L), color = Color.White)
                Text(text = formatDuration(duration.toLong()), color = Color.White)
            }

            Spacer(modifier = modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = modifier.size(40.dp),
                        tint = Color(0xBFFFFFFF)
                    )

                }
                IconButton(
                    modifier = modifier.size(60.dp),
                    onClick = {
                        if (isPlaying) {
                            player.pause()
                        } else {
                            player.play()
                        }
                        isPlaying = !isPlaying
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.play_arrow__1_),
                        contentDescription = null,
                        modifier = modifier.size(200.dp),
                        tint = Color(0xBFFFFFFF)
                    )

                }
                IconButton(onClick = { /*TODO*/ }) {
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

