package com.example.soundsphere.ui.search

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.R
import com.example.soundsphere.data.model.Album
import com.example.soundsphere.data.model.Artist
import com.example.soundsphere.data.model.Playlist
import com.example.soundsphere.data.model.Song
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.components.ImageBoxExtraLarge
import com.example.soundsphere.ui.components.ImageBoxMedium
import com.example.soundsphere.ui.firestore.FireStoreViewModel
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    fireStoreViewModel: FireStoreViewModel,
    playViewModel: PlayViewModel,
    authViewModel: AuthViewModel,
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val valueSearch = rememberSaveable() {
        mutableStateOf("")
    }

    val allArtists = fireStoreViewModel.allArtists.collectAsState().value
    val albumByNames = fireStoreViewModel.albumsByName.collectAsState().value
    val artistByNames = fireStoreViewModel.artistsByName.collectAsState().value
    val songByNames = fireStoreViewModel.songsByName.collectAsState().value

    val isEmailVerified by authViewModel.isEmailVerified.collectAsState()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()
    authViewModel.checkUserAuthentication()
    if (isUserAuthenticated) {
        authViewModel.checkEmailVerification()
    }

    LaunchedEffect(Unit) {
        fireStoreViewModel.getAllArtists()
    }


    LaunchedEffect(valueSearch.value) {
        if (valueSearch.value.isNotEmpty()) {
            fireStoreViewModel.getSongsByName(valueSearch.value)
            fireStoreViewModel.getAlbumsByName(valueSearch.value)
            fireStoreViewModel.getArtistsByName(valueSearch.value)
        }
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
            if (allArtists is Resource.Loading) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp)
        ) {
            SearchTextField(
                onValueChange = {
                    valueSearch.value = it
                },
                value = valueSearch.value,
                modifier = modifier
            )
            Spacer(modifier = Modifier.height(30.dp))

            if (valueSearch.value.isNotEmpty() && (songByNames.data != null || albumByNames.data != null || artistByNames.data != null)) {
                SearchResults(
                    searchAlbum = albumByNames.data,
                    searchArtists = artistByNames.data,
                    searchSong = songByNames.data,
                    searchPlayList = emptyList(),
                    modifier = modifier,
                    playViewModel = playViewModel,
                    navController = navController,
                    scope = rememberCoroutineScope()
                )
            } else if (allArtists.data != null) {
                allArtists.data.let {
                    if (it != null) {
                        TopArtist(modifier, it,navController = navController)
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    modifier = modifier.padding(bottom = 20.dp),
                    text = "Browse", style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xBFFFFFFF),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontInter
                    )
                )
                LazyVerticalStaggeredGrid(
                    modifier = modifier
                        .fillMaxSize(1f)
                        .clip(shape = RoundedCornerShape(20.dp)),
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalItemSpacing = 10.dp

                ) {
//                    if (genres != null) {
//                        items(genres.data) { genres ->
//                            ImageBoxBrowse(
//                                imageUrl = genres.picture_big,
//                                text = genres.name
//                            )
//                        }
//                    }
                }
            }
        }
    }
}


@Composable
private fun ImageBoxBrowse(
    modifier: Modifier = Modifier,
    imageUrl: String,
    text: String
) {
    Box(
        modifier = modifier
            .height(98.dp)
            .fillMaxWidth(1f)
            .clip(RoundedCornerShape(16.dp))
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            ),
            contentDescription = "Playlist Cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = modifier
                .matchParentSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
//
                            Color.Black.copy(alpha = 0.2f),
                        )
                    )
                )
        )

        // Văn bản
        Text(
            text = text.uppercase(),
            modifier = Modifier
                .matchParentSize()
                .align(alignment = Alignment.Center)
                .padding(top = 73.dp),
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color(0xCCFFFFFF),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }

}

@Composable
private fun TopArtist(
    modifier: Modifier,
    allArtists: List<Artist>,
    scope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = R.drawable.trending_up),
            contentDescription = null,
            Modifier.size(20.dp),
            tint = Color(0xBFFFFFFF)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Trending artists", style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xC1FFFFFF),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = fontInter
            )
        )
    }
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        items(allArtists) { artist ->
            RoundArtist(
                imageUrl = artist.picture,
                name = artist.name,
                modifier = modifier
                    .clickable {
                        scope.launch {
                            navController.navigate("songlistartist/${artist.id}")
                        }
                    }
            )
        }
    }
}


@Composable
fun SearchTextField(
    onValueChange: (String) -> Unit = {},
    value: String,
    modifier: Modifier,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Search song, artist, album ...",
                color = Color(0xC1000000),
                fontFamily = fontInter,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
        },
        maxLines = 1,
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        shape = MaterialTheme.shapes.medium,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = Color(0xC1000000),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = fontInter
        )
    )
}

@Composable
fun RoundArtist(
    imageUrl: String,
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(end = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            ),
            contentDescription = "Avatar",
            modifier = modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            modifier = modifier.width(80.dp),
            text = name,
            fontSize = 16.sp,
            fontFamily = fontInter,
            fontWeight = FontWeight.Medium,
            color = Color(0xBFFFFFFF),
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }


}


@Composable
fun SearchResults(
    searchAlbum: List<Album>?,
    searchArtists: List<Artist>?,
    searchSong: List<Song>?,
    searchPlayList: List<Playlist>?,
    playViewModel: PlayViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    scope: CoroutineScope
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            searchAlbum?.let {
                Text(
                    text = "Albums",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontInter
                    )
                )
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(it) { album ->

                        ImageBoxExtraLarge(
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
            searchArtists?.let {
                Text(
                    text = "Artists",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontInter
                    )
                )
                LazyRow(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(it) { artist ->
                        RoundArtist(
                            imageUrl = artist.picture,
                            name = artist.name,
                            modifier = modifier.clickable {
                                scope.launch {
                                    navController.navigate("songlistartist/${artist.id}")
                                }
                            }
                        )
                    }
                }
            }
        }

        item {
            searchSong?.let {
                Text(
                    text = "Songs",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontInter
                    )
                )
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(searchSong) { song ->
                        ImageBoxMedium(
                            imageUrl = song.picture,
                            text = song.title
                        ) {
                            scope.launch {
                                playViewModel.setCurrentTrackSelected(song)
                                navController.navigate(NavigationRoutes.PlayTrack.route)
                            }
                        }
                    }
                }
            }
        }

//        item {
//            searchPlayList?.let {
//                Text(
//                    text = "Playlists",
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        color = Color.White,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = fontInter
//                    )
//                )
//                LazyRow(
//                    Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(vertical = 10.dp),
//                    horizontalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    items(searchPlayList) { playlist ->
//                        ImageBoxLarge(
//                            imageUrl = playlist.picture_big,
//                            text = playlist.title
//                        ) {
//                            val urlTrackList = playlist.tracklist
//                            val idPlayList = playlist.id
//                            val encodeUrlTrackList = encodeUrl(urlTrackList)
//
//                            scope.launch {
//                                navController.navigate("songlistplaylist/$encodeUrlTrackList/$idPlayList")
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}


@Preview
@Composable
fun SearchScreenPreview() {

}

