package com.example.soundsphere.ui.song_list

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.R
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.ImageBoxSongList
import com.example.soundsphere.ui.firestore.FireStoreViewModel
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.play.UIEvents
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun SongListScreenArtist(
    idArtist: String,
    playViewModel: PlayViewModel,
    navController: NavHostController,
    fireStoreViewModel: FireStoreViewModel,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    var icons by remember { mutableStateOf(Icons.Filled.FavoriteBorder) }
    val userTracksCollection =
        FirebaseFirestore.getInstance().collection("favourites")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .collection("artists")

    val songsByIdArtist = fireStoreViewModel.songsByArtistId.collectAsState().value

    LaunchedEffect(Unit) {
        fireStoreViewModel.getSongsByArtistId(idArtist)
    }

    if (songsByIdArtist.data != null) {
        playViewModel.trackList = songsByIdArtist.data!!
    }

//    LaunchedEffect(Unit) {
//        userTracksCollection.document(encodeUrl(urlTrackList)).get()
//            .await()
//            ?.let { document ->
//                icons = if (document.exists()) {
//                    Icons.Filled.Favorite
//                } else {
//                    Icons.Filled.FavoriteBorder
//                }
//            }
//    }

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
            if (songsByIdArtist is Resource.Loading) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            if (songsByIdArtist.data != null) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.27f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ImageBoxSongList(
                        imageUrl = songsByIdArtist.data?.firstOrNull()?.picture,
                        text = songsByIdArtist.data?.firstOrNull()?.artist?.name ?: "Unknown",
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
                            text = "Type: Artist",
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
                            text = "${songsByIdArtist.data?.size ?: 0} songs",
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
                                imageVector = icons,
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
                                    if (playViewModel.currentSelectedTrack.url == ""){
                                        songsByIdArtist.data?.firstOrNull()
                                            ?.let { it1 -> playViewModel.setCurrentTrackSelected(it1) }
                                    }
                                    playViewModel.onUiEvent(UIEvents.PlayPause)


                                }
                        )
                    }
                }
            }
            if (songsByIdArtist.data != null) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    if (songsByIdArtist.data.isNullOrEmpty())
                        return@LazyColumn
                    items(songsByIdArtist.data.orEmpty()) { song ->
                        if (song.url == "") return@items
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(horizontal = 20.dp)
                                .clickable {
                                    playViewModel.setCurrentTrackSelected(song)
                                    playViewModel.onUiEvent(UIEvents.PlayPause)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {

                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = song.picture)
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
                                    text = song.title,
                                    color = Color(0xFFFFFFFF),
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = fontInter,
                                    fontSize = 16.sp,
                                    letterSpacing = 1.sp
                                )

                                song.artist?.name?.let { it1 ->
                                    Text(
                                        text = it1,
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


