package com.example.soundsphere.ui.search

import android.annotation.SuppressLint
import android.app.appsearch.SearchResults
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.R
import com.example.soundsphere.data.dtodeezer.chart.Artists
import com.example.soundsphere.data.dtodeezer.search.searchAlbum.Data
import com.example.soundsphere.navigation.NavigationRoutes
import com.example.soundsphere.ui.components.ImageBoxExtraLarge
import com.example.soundsphere.ui.components.ImageBoxLarge
import com.example.soundsphere.ui.components.ImageBoxMedium
import com.example.soundsphere.ui.home.HomeViewModel
import com.example.soundsphere.ui.home.encodeUrl
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    viewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val valueSearch = rememberSaveable() {
        mutableStateOf("")
    }

    val searchAlbumState = searchViewModel.searchAlbumState.collectAsState()
    val searchTrackState = searchViewModel.searchTrackState.collectAsState()
    val searchArtistState = searchViewModel.searchArtistState.collectAsState()
    val searchPlayListState = searchViewModel.searchPlayListState.collectAsState()
    val chartState by viewModel.chartState.collectAsState()
    val genreState by searchViewModel.genresState.collectAsState()
    val chartArtist = chartState.isSuccessful?.artists
    val genres = genreState.isSuccessful
    LaunchedEffect(valueSearch.value) {
        if (valueSearch.value.isNotEmpty()) {
            searchViewModel.getSearchAlbum(q = valueSearch.value)
            searchViewModel.getSearchTrack(q = valueSearch.value)
            searchViewModel.getSearchArtist(q = valueSearch.value)
            searchViewModel.getSearchPlayList(q = valueSearch.value)
        }
    }
    val searchAlbum = searchAlbumState.value.isSuccessful?.data
    val searchArtists = searchArtistState.value.isSuccessful?.data
    val searchTrack = searchTrackState.value.isSuccessful?.data
    val searchPlayList = searchPlayListState.value.isSuccessful?.data


    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFF121212),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color(0xFF121212)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (chartArtist == null) {
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

            if (valueSearch.value.isNotEmpty() && (searchAlbum != null || searchArtists != null || searchTrack != null || searchPlayList != null)) {
                SearchResults(
                    searchAlbum = searchAlbum,
                    searchArtists = searchArtists,
                    searchTrack = searchTrack,
                    searchPlayList = searchPlayList,
                    modifier = modifier,
                    navController = navController,
                    scope = rememberCoroutineScope()
                )
            } else if (chartArtist != null) {
                TopArtist(modifier, chartArtist)
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
                    if (genres != null) {
                        items(genres.data) { genres ->
                            ImageBoxBrowse(
                                imageUrl = genres.picture_big,
                                text = genres.name
                            )
                        }
                    }
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
    topArtist: Artists
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
        items(topArtist.data) { artist ->
            RoundArtist(
                imageUrl = artist.picture_big,
                name = artist.name
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
    searchAlbum: List<Data>?,
    searchArtists: List<com.example.soundsphere.data.dtodeezer.search.searchArtist.Data>?,
    searchTrack: List<com.example.soundsphere.data.dtodeezer.search.searchTrack.Data>?,
    searchPlayList: List<com.example.soundsphere.data.dtodeezer.search.searchPlayList.Data>?,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    scope: CoroutineScope
) {
    val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
    val lastUrl = "/500x500-000000-80-0-0.jpg"
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
                    val baseUrl = "https://e-cdns-images.dzcdn.net/images/cover/"
                    val lastUrl = "/500x500-000000-80-0-0.jpg"

                    items(it) { album ->

                        ImageBoxExtraLarge(
                            imageUrl = baseUrl + album.md5_image + lastUrl,
                            text = album.title
                        ) {
                            val urlTrackList = album.tracklist
                            val urlAlbum = album.id
                            val encodeUrlTrackList = encodeUrl(urlTrackList)

                            scope.launch {
                                navController.navigate("songlist/$encodeUrlTrackList/$urlAlbum")
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
                            imageUrl = artist.picture_big,
                            name = artist.name,
                            modifier = modifier.clickable {
                                val urlTrackList = artist.tracklist
                                val encodeUrlTrackList = encodeUrl(urlTrackList)
                                scope.launch {
                                    scope.launch {
                                        navController.navigate("songlistartist/$encodeUrlTrackList")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        item {
            searchTrack?.let {
                Text(
                    text = "Tracks",
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
                    items(searchTrack) { track ->
                        ImageBoxMedium(
                            imageUrl = baseUrl + track.md5_image + lastUrl,
                            text = track.title_short
                        ) {
                            scope.launch {
                                navController.navigate(NavigationRoutes.PlayTrack.route)
                            }
                        }
                    }
                }
            }
        }

        item {
            searchPlayList?.let {
                Text(
                    text = "Playlists",
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
                    items(searchPlayList) { playlist ->
                        ImageBoxLarge(
                            imageUrl = playlist.picture_big,
                            text = playlist.title
                        ) {
                            val urlTrackList = playlist.tracklist
                            val idPlayList = playlist.id
                            val encodeUrlTrackList = encodeUrl(urlTrackList)

                            scope.launch {
                                navController.navigate("songlistplaylist/$encodeUrlTrackList/$idPlayList")
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
fun SearchScreenPreview() {

}

