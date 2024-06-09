package com.example.soundsphere.ui.play

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.soundsphere.ui.components.ImageBoxPlay
import com.example.soundsphere.ui.firestore.firestoreViewModel
import com.example.soundsphere.ui.theme.fontInter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint(
    "LogNotTimber", "StateFlowValueCalledInComposition", "PrivateResource",
    "UnrememberedMutableState"
)
@Composable
fun PlayScreen(
    navController: NavHostController,
    playViewModel: PlayViewModel,
    firestoreViewModel: firestoreViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {


    val scope = rememberCoroutineScope()
    val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
    val lastUrl = "/500x500-000000-80-0-0.jpg"
    val likeTrackState = firestoreViewModel.likedTracks.collectAsState().value
    val fireStoreState = firestoreViewModel.savedTracks.collectAsState().value
    val context = LocalContext.current
    var icons by remember { mutableStateOf(Icons.Filled.FavoriteBorder) } // Default icon is set to border
    val userTracksCollection =
        FirebaseFirestore.getInstance().collection("favourites")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .collection("tracks")

    Log.d("currentTrack", playViewModel.currentSelectedTrack.toString())

    LaunchedEffect(playViewModel.currentSelectedTrack) {
        userTracksCollection.document(playViewModel.currentSelectedTrack.id.toString()).get()
            .await()
            ?.let { document ->
                Log.d("Document", "${document.exists()}")
                icons = if (document.exists()) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                }

            }
    }




    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
        Spacer(modifier = modifier.height(20.dp))

        Column(
            modifier = modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .padding(horizontal = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            playViewModel.currentSelectedTrack.let { track ->
                ImageBoxPlay(
                    modifier = modifier
                        .size(380.dp)
                        .align(Alignment.CenterHorizontally),
                    imageUrl = (baseUrl + track.md5_image + lastUrl)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        track.title_short?.let {
                            Text(
                                text = it,
                                color = Color(0xFFFFFFFF),
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth(1f),
                                fontFamily = fontInter,
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        track.artist?.let {
                            Text(
                                text = it.name,
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
                    }
                    IconButton(
                        onClick = {
                            Log.d("liked", track.toString())
                            Log.d("liked",  FirebaseAuth.getInstance().currentUser?.email.toString() )
                            firestoreViewModel.savedLikedTrack(
                                track,
                                FirebaseAuth.getInstance().currentUser?.email.toString()
                            )
                            icons = if (icons == Icons.Filled.FavoriteBorder) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            }
                        }) {
                        Icon(
                            imageVector = icons,
                            contentDescription = null,
                            tint = Color(0xBFFFFFFF),
                            modifier = modifier.size(45.dp)

                        )
                    }

                }
            }



            Spacer(modifier = modifier.height(10.dp))

            Slider(
                value = playViewModel.progress,
                onValueChange = { playViewModel.onUiEvent(UIEvents.SeekTo(it)) },
                valueRange = 1f..190f,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(1f),
                colors = SliderDefaults.colors().copy(
                    thumbColor = Color(0xBFFFFFFF), activeTrackColor = Color(0xBFFFFFFF)
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = playViewModel.formatDuration(30), color = Color.White)
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = modifier.size(55.dp), onClick = {
                    playViewModel.onUiEvent(UIEvents.Backward)
                }
                ) {
                    Icon(
                        painter = painterResource(id = androidx.media3.ui.R.drawable.exo_icon_previous),
                        contentDescription = null,
                        modifier = modifier.size(100.dp),
                        tint = Color(0xBFFFFFFF)
                    )

                }
                IconButton(modifier = modifier.size(90.dp), onClick = {
                    playViewModel.onUiEvent(UIEvents.PlayPause)
                }) {
                    Icon(
                        painter = if (!playViewModel.isPlaying) {
                            painterResource(id = androidx.media3.ui.R.drawable.exo_icon_play)
                        } else {
                            painterResource(id = androidx.media3.ui.R.drawable.exo_icon_pause)
                        },
                        contentDescription = null,
                        modifier = modifier.size(500.dp),
                        tint = Color(0xBFFFFFFF)
                    )

                }
                IconButton(modifier = modifier.size(55.dp), onClick = {
                    playViewModel.onUiEvent(UIEvents.SeekToNext)
                }) {
                    Icon(
                        painter = painterResource(id = androidx.media3.ui.R.drawable.exo_icon_next),
                        contentDescription = null,
                        modifier = modifier.size(100.dp),
                        tint = Color(0xBFFFFFFF)
                    )
                }
            }
        }

    }

}

@Preview
@Composable
fun PreviewPlayScreen(modifier: Modifier = Modifier) {

}