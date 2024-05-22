package com.example.soundsphere.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavHostController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val valueSearch = rememberSaveable() {
        mutableStateOf("")
    }
    val topArtistState = viewModel.topArtistState.collectAsState()
    val browseCategoryState = viewModel.browseCategoryState.collectAsState()
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        containerColor = Color(0xFF121212),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color(0xFF121212)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (topArtistState.value.isLoading || browseCategoryState.value.isLoading) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 20.dp)
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
            TopArtist(modifier, topArtistState)
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
                    .padding(bottom = 130.dp)
                    .clip(shape = RoundedCornerShape(20.dp)),
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp

                ) {
                if (browseCategoryState.value.isSuccessDataBrowseCategory != null) {
                    val categories = browseCategoryState.value.isSuccessDataBrowseCategory?.categories?.items
                    items(categories!!) { category ->
                        ImageBoxBrowse(
                            imageUrl = category.icons.first().url,
                            text = category.name
                        )
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
    topArtistState: State<TopArtistState>
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
        if (topArtistState.value.isSuccessDataTopArtist != null) {
            val artists = topArtistState.value.isSuccessDataTopArtist?.artists
            items(artists!!) { artist ->
                RoundArtist(
                    imageUrl = artist.images.first().url,
                    name = artist.name
                )
            }
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

@Preview
@Composable
fun SearchScreenPreview() {
    ImageBoxBrowse(
        imageUrl = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
        text = "Song name"
    )
}