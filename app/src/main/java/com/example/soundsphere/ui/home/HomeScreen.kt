package com.example.soundsphere.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soundsphere.R
import com.example.soundsphere.data.model.TopSongs
import com.example.soundsphere.ui.components.ImageBoxCategories
import com.example.soundsphere.ui.components.ImageBoxFeature
import com.example.soundsphere.ui.components.LocalImageRoundAvatar
import com.example.soundsphere.ui.components.RoundAvatar
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType", "UnrememberedMutableState")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val homeState = viewModel.homeState.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isDataLoaded by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.fetchTopChartSongs()
        isDataLoaded = true
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFF1E1E1E))
    ) {
        TopUpHome(modifier)

        Text(text = viewModel.fetchTopChartSongs().toString(), color = Color.White)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color(0xFF1E1E1E))
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Featuring Today",
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
//                items(topSongs.size) {
//                    ImageBoxCategories(imageUrl = topSongs[it].chart_items[it].item.header_image_url, text = topSongs[it].chart_items[it].item.title)
//                }
            }
        }


        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color(0xFF1E1E1E))
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Categories",
                color = Color(0xBFFFFFFF),
                fontFamily = roboto,
                fontWeight = FontWeight.Medium,
                fontSize = 27.sp,
            )
            if (isDataLoaded){
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
//                    items(topSongs.size) {
//                        ImageBoxCategories(imageUrl = topSongs[it].chart_items[it].item.header_image_url, text = topSongs[it].chart_items[it].item.title)
//                    }
                }
            }

        }
    }
}

@Composable
private fun TopUpHome(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.15f)
            .padding(top = 30.dp)
            .background(color = Color(0xFF1E1E1E))
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = "\uD83D\uDC4BHi Logan",
                color = Color(0xBFFFFFFF),
                fontFamily = roboto,
                fontWeight = FontWeight.Light,
                fontSize = 23.sp
            )
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
            //RoundAvatar(imageUrl = "https://avatars.githubusercontent.com/u/104177755?v=4")
            LocalImageRoundAvatar(resourceId = R.drawable.avatar)
        }

    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}