package com.example.soundsphere.ui.library

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.BottomBar
import com.example.soundsphere.ui.components.RoundAvatar
import com.example.soundsphere.ui.firestore.firestoreViewModel
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.play.UIEvents
import com.example.soundsphere.ui.profile.YourLibrary
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LibraryScreen(
    navController: NavHostController,
    firestoreViewModel: firestoreViewModel,
    playViewModel: PlayViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.email?.let { firestoreViewModel.getLikedTracks(it) }
        Log.d("LibraryScreen", "LaunchedEffect")
    }

    val trackListState = firestoreViewModel.likedTracks.collectAsState().value
    Log.d("LibraryScreen", "trackListState: ${trackListState.data}")
    val trackList = trackListState.data
    if (trackList != null) {
        playViewModel.trackList = trackList
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFF121212),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = modifier
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoundAvatar(imageUrl = FirebaseAuth.getInstance().currentUser?.photoUrl.toString(),
                    modifier = modifier.size(37.dp)) {

                }
                Text(
                    text = "Your Library",
                    modifier = modifier,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = roboto
                )
            }


            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Favourites Tracks ",
                    modifier = modifier,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = roboto
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                    .padding(15.dp)

            ) {

                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
                    val lastUrl = "/500x500-000000-80-0-0.jpg"
                    stickyHeader {

                    }
                    items(trackList.orEmpty()) { track ->
                        if (track.preview == "") return@items
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(horizontal = 10.dp)
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
                                    .size(40.dp)
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
                                    fontSize = 15.sp,
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
fun LibraryScreenPreview() {

}
