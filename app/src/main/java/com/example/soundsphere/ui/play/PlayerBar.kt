package com.example.soundsphere.ui.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.soundsphere.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


val SmallDp: Dp = 4.dp
val MediumDp: Dp = 8.dp
val LargeDp: Dp = 16.dp
val FloatingPlaybackBarHeight: Dp = 56.dp
val FloatingPlaybackBarCoverSize: Dp = 48.dp
val FloatingPlaybackBarButtonSize: Dp = 40.dp
val FloatingPlaybackBarButtonIconSize: Dp = 28.dp

val PlaybackBarColor = Color(0xFF925700)
val PrimaryWhite = Color(0xFFF3F6F5)

val FloatingPlaybackBarPrimaryTextStyle= TextStyle(
    color = PrimaryWhite,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
)

val FloatingPlaybackBarSecondaryTextStyle = TextStyle(
    color = PrimaryWhite,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    letterSpacing = 0.5.sp
)
data class SelectedTrackState(
    val track: TrackItemData = TrackItemData(),
    val playbackState: PlaybackState = PlaybackState.PAUSED
)

enum class PlaybackState {
    PLAYING, PAUSED
}

data class TrackItemData(
    val id: Int = -1,
    val title: String = "No Title",
    val artist: String = "No Artist",
    val coverDrawableId: Int = -1
)
@Composable
fun PlayBar(
    selectedTrackStateFlow: Flow<SelectedTrackState> = flowOf(SelectedTrackState()),
    onPreviousClicked: () -> Unit = {},
    onPlayPauseClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {}
) {

    val selectedTrackState = selectedTrackStateFlow.collectAsState(initial = SelectedTrackState()).value
    val imagePainter = rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(
        data =
        if (selectedTrackState.track.coverDrawableId == -1) null
        else selectedTrackState.track.coverDrawableId
    ).apply(block = fun ImageRequest.Builder.() {
        fallback(R.drawable.image_9)
    }).build()
    )
    Card(
        modifier = Modifier
            .padding(MediumDp)
            .height(FloatingPlaybackBarHeight)
            .background(Color.Transparent)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = LargeDp),
        shape = RoundedCornerShape(MediumDp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(PlaybackBarColor)
                .padding(SmallDp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(FloatingPlaybackBarCoverSize)
                    .clip(RoundedCornerShape(MediumDp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = LargeDp)
                    .weight(1f)
            ) {
                Text(
                    style = FloatingPlaybackBarPrimaryTextStyle,
                    text = selectedTrackState.track.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    style = FloatingPlaybackBarSecondaryTextStyle,
                    text = selectedTrackState.track.artist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            // 6. Wrapping Icon inside IconButton
            IconButton(
                onClick = { onPreviousClicked() },
                modifier = Modifier.size(FloatingPlaybackBarButtonSize)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = PrimaryWhite,
                    modifier = Modifier.size(FloatingPlaybackBarButtonIconSize)
                )
            }
            // 7. Responsive Control Icons
            val iconId =
                if (selectedTrackState.playbackState == PlaybackState.PLAYING) {
                    R.drawable.ic_library
                } else {
                    R.drawable.ic_home
                }
            IconButton(
                onClick = { onPlayPauseClicked() },
                modifier = Modifier.size(FloatingPlaybackBarButtonSize)
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    tint = PrimaryWhite,
                    modifier = Modifier.size(FloatingPlaybackBarButtonIconSize)
                )
            }
            IconButton(
                onClick = { onNextClicked() },
                modifier = Modifier.size(FloatingPlaybackBarButtonSize)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_library),
                    contentDescription = null,
                    tint = PrimaryWhite,
                    modifier = Modifier.size(FloatingPlaybackBarButtonIconSize)
                )
            }
        }
    }
}

@Preview
@Composable
fun PlayBarPreview() {
    PlayBar()
}