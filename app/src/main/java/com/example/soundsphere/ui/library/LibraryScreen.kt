package com.example.soundsphere.ui.library

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.components.CreatePlayListDialog
import com.example.soundsphere.ui.components.ImageBoxMedium
import com.example.soundsphere.ui.components.RoundAvatar
import com.example.soundsphere.ui.firestore.FireStoreViewModel
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "LogNotTimber")
@Composable
fun LibraryScreen(
    navController: NavHostController,
    firestoreViewModel: FireStoreViewModel,
    playViewModel: PlayViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val allPlaylist = firestoreViewModel.allPlaylists.collectAsState().value
    val createPlaylist = firestoreViewModel.createPlaylist.collectAsState().value
    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.email?.let {
            firestoreViewModel.getLikedSongs(it)
            firestoreViewModel.getLikedAlbums(it) }
    }
    LaunchedEffect(key1 = createPlaylist.data) {
        FirebaseAuth.getInstance().currentUser?.email?.let {
            firestoreViewModel.getAllPlaylists(FirebaseAuth.getInstance().currentUser?.email.toString())
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    val songListState = firestoreViewModel.likedTracks.collectAsState().value
    val trackListAlbumState = firestoreViewModel.likedAlbums.collectAsState().value
    val songList = songListState.data
    val songListAlbum = trackListAlbumState.data


    val isEmailVerified by authViewModel.isEmailVerified.collectAsState()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()
    authViewModel.checkUserAuthentication()
    if (isUserAuthenticated) {
        authViewModel.checkEmailVerification()
    }

    androidx.media3.common.util.Log.d("isEmailVerified", isEmailVerified.toString())


    val scope = rememberCoroutineScope()

    if (showDialog) {
        CreatePlayListDialog(onDismiss = { showDialog = false }) {
            firestoreViewModel.createPlaylist(
                it,
                FirebaseAuth.getInstance().currentUser?.email.toString()
            )
            showDialog = false
        }
    }

    if (songList != null) {
        playViewModel.trackList = songList
    }

    if (songList == null || songListAlbum == null) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFF121212),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoundAvatar(
                    imageUrl = (FirebaseAuth.getInstance().currentUser?.photoUrl
                        ?: "https://inkythuatso.com/uploads/thumbnails/800/2023/03/9-anh-dai-dien-trang-inkythuatso-03-15-27-03.jpg").toString(),
                    modifier = Modifier.size(37.dp)
                ) {

                }
                Text(
                    text = "Your Library",
                    modifier = Modifier,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = roboto
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                item {
                    Text(
                        text = "Favourites Tracks",
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = roboto
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                            .padding(15.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            items(songList.orEmpty()) { song ->
                                if (song.url == "") {
                                    return@items
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .padding(horizontal = 10.dp)
                                        .clickable {
                                            playViewModel.setCurrentTrackSelected(song)
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
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(shape = RoundedCornerShape(5.dp))
                                            .clickable {
                                                navController.navigate(NavigationRoutes.PlayTrack.route)
                                                playViewModel.setCurrentTrackSelected(song)
                                            }
                                    )

                                    Column(
                                        modifier = Modifier
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
                                            fontSize = 15.sp,
                                            letterSpacing = 1.sp
                                        )

                                        Text(
                                            text = song.artist?.name ?: "",
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
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp)
                                        .height(1.dp),
                                    color = Color(0x80FFFFFF)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Text(
                        text = "Favourites Albums",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = roboto
                    )

                    LazyRow(
                        Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (songListAlbum != null) {
                            items(songListAlbum) { album ->
                                ImageBoxMedium(
                                    imageUrl = album.picture,
                                    text = album.title
                                ) {
                                    scope.launch {
                                        navController.navigate("songlist/${album.id}")
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Your Playlists",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = roboto
                        )

                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    LazyRow(
                        Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (allPlaylist.data != null) {
                            allPlaylist.data?.let {
                                items(it) { playlist ->
                                    Log.d("playlist", playlist.playlistName)
                                    if (playlist.songs.isEmpty()){
                                        ImageBoxMedium(
                                            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQWQ3WxTAcEvU0f2NiPyDs_V44kvJHB1nqTVg&s",
                                            text = playlist.playlistName
                                        ) {
                                            scope.launch {
                                                navController.navigate("songlistplaylist/${playlist.playlistName}")
                                            }
                                        }
                                    }
                                    playlist.songs.firstOrNull()?.let { it1 ->
                                        ImageBoxMedium(
                                            imageUrl = it1.picture,
                                            text = playlist.playlistName
                                        ) {
                                            scope.launch {
                                                navController.navigate("songlistplaylist/${playlist.playlistName}")
                                            }
                                        }
                                    }
                                }
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
