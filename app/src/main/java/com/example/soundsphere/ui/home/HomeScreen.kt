package com.example.soundsphere.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.data.model.Artist
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.components.ImageBoxExtraLarge
import com.example.soundsphere.ui.components.ImageBoxLarge
import com.example.soundsphere.ui.components.ImageBoxMedium
import com.example.soundsphere.ui.components.RoundAvatar
import com.example.soundsphere.ui.firestore.FireStoreViewModel
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.play.UIEvents
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.FirebaseAuth
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


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun HomeScreen(
    playViewModel: PlayViewModel,
    authViewModel: AuthViewModel,
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    val fireStoreViewModel: FireStoreViewModel = hiltViewModel()

    val songState by fireStoreViewModel.allSongs.collectAsState()
    val albumState by fireStoreViewModel.allAlbums.collectAsState()
    val artistState by fireStoreViewModel.allArtists.collectAsState()
    val songByGenreId by fireStoreViewModel.songsByGenreId.collectAsState()

    val isEmailVerified by authViewModel.isEmailVerified.collectAsState()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()
    authViewModel.checkUserAuthentication()
    if (isUserAuthenticated) {
        authViewModel.checkEmailVerification()
    }

    Log.d("isEmailVerified", isEmailVerified.toString())
    LaunchedEffect(Unit) {
        fireStoreViewModel.getAllSongs()
        fireStoreViewModel.getAllAlbums()
        fireStoreViewModel.getAllArtists()
    }


    if (songState.data != null) {
        playViewModel.trackList = songState.data!!
    }


    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabTitles = listOf("For you", "Ballad", "Pop", "Rap")
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
            if (artistState is Resource.Loading || albumState is Resource.Loading || songState is Resource.Loading) {
                CircularProgressIndicator()
            }
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                TopUpHome(modifier = modifier, navController = navController)
            }
            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .background(Color(0xFF121212))
                        .fillMaxWidth(),
                    indicator = {},
                    divider = {},
                    containerColor = Color(0xFF121212),
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = false,
                            onClick = { selectedTabIndex = index },
                            modifier = Modifier
                                .padding(horizontal = 3.dp, vertical = 10.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selectedTabIndex == index) Color.Gray else Color(
                                        0xFF121212
                                    )
                                )
                                .clickable { selectedTabIndex = index }
                                .size(37.dp),
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = roboto,
                                    maxLines = 1,
                                    color = if (selectedTabIndex == index) Color(0xFF121212) else Color.Gray,
                                )
                            }
                        )
                    }
                }

            }


            when (selectedTabIndex) {
                0 -> {
                    item {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Featuring Today",
                                color = Color(0xBFFFFFFF),
                                fontFamily = roboto,
                                fontWeight = FontWeight.Medium,
                                fontSize = 27.sp,
                            )
                            Spacer(modifier = modifier.height(10.dp))
                            LazyRow(
                                Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (songState.data != null) {
                                    items(songState.data!!) { song ->
                                        if (song.url == "") return@items
                                        ImageBoxExtraLarge(
                                            imageUrl = song.picture,
                                            text = song.title
                                        ) {
                                            scope.launch {
                                                playViewModel.setCurrentTrackSelected(
                                                    song
                                                )
                                                navController.navigate(NavigationRoutes.PlayTrack.route)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    item {
                        TopAlbumList(modifier, albumState.data, navController, scope)
                    }
//                    item {
//                        TopPlaylistsList(modifier, chartPlaylists, navController, scope)
//                    }
                    item {
                        artistState.data?.let { it1 ->
                            TopArtistList(
                                modifier,
                                it1, navController, scope
                            )
                        }
                    }
                }

                1 -> item {
                    LaunchedEffect(Unit) {
                        fireStoreViewModel.getSongsByGenreId("balad")
                    }

                    Column(
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Ballad",
                            color = Color(0xBFFFFFFF),
                            fontFamily = roboto,
                            fontWeight = FontWeight.Medium,
                            fontSize = 27.sp,
                        )
                        Spacer(modifier = modifier.height(10.dp))

                        LazyColumn(
                            modifier = modifier
                                .fillParentMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            val songs = songByGenreId.data
                            items(songs.orEmpty()) { song ->
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

                                        song.artist?.let { it1 ->
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
                2 -> item {
                    LaunchedEffect(Unit) {
                        fireStoreViewModel.getSongsByGenreId("pop")
                    }
                    Column(
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Pop",
                            color = Color(0xBFFFFFFF),
                            fontFamily = roboto,
                            fontWeight = FontWeight.Medium,
                            fontSize = 27.sp,
                        )
                        Spacer(modifier = modifier.height(10.dp))

                        LazyColumn(
                            modifier = modifier
                                .fillParentMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            val songs = songByGenreId.data
                            items(songs.orEmpty()) { song ->
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

                                        song.artist?.let { it1 ->
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
                3 -> item {
                    LaunchedEffect(Unit) {
                        fireStoreViewModel.getSongsByGenreId("rap")
                    }
                    Column(
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Rap",
                            color = Color(0xBFFFFFFF),
                            fontFamily = roboto,
                            fontWeight = FontWeight.Medium,
                            fontSize = 27.sp,
                        )
                        Spacer(modifier = modifier.height(10.dp))

                        LazyColumn(
                            modifier = modifier
                                .fillParentMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            val songs = songByGenreId.data
                            items(songs.orEmpty()) { song ->
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

                                        song.artist?.let { it1 ->
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
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun TopArtistList(
    modifier: Modifier = Modifier,
    allArtists: List<Artist>,
    navController: NavHostController,
    scope: CoroutineScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Artists",
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
            if (allArtists.isNotEmpty()) {
                Log.d("allArtists", allArtists.toString())
                items(allArtists) { artist ->
                    ImageBoxLarge(
                        imageUrl = artist.picture,
                        text = artist.name
                    ) {
                        scope.launch {
                            navController.navigate("songlistartist/${artist.id}")
                        }
                    }
                }
            }
        }
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun TopAlbumList(
    modifier: Modifier,
    albums: List<com.example.soundsphere.data.model.Album>?,
    navController: NavHostController,
    scope: CoroutineScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Albums",
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
            if (albums != null) {
                items(albums) { album ->
                    Log.d("album", album.toString())
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

}


fun encodeUrl(url: String): String {
    return URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
}


@Composable
private fun TopUpHome(
    modifier: Modifier,
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
        val user = FirebaseAuth.getInstance().currentUser
        Column(
            modifier = modifier
        ) {

            if (user != null) {
                Text(
                    text = "\uD83D\uDC4BHi ${user.displayName ?: "Anonymous"}",
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


            RoundAvatar(
                imageUrl = (user?.photoUrl
                    ?: "https://inkythuatso.com/uploads/thumbnails/800/2023/03/9-anh-dai-dien-trang-inkythuatso-03-15-27-03.jpg").toString()
            ) {
                navController.navigate(NavigationRoutes.Profile.route)

            }
        }

    }

}


//@Preview
//@Composable
//fun HomeScreenPreview() {
//