package com.example.soundsphere.ui.components

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.R
import com.example.soundsphere.data.model.Song
import com.example.soundsphere.navigation.BottomBarRoutes
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.roboto
import androidx.compose.material3.*
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun ReAuthDialog(
    onReAuth: (String, String) -> Unit,
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = { Text("Re-authenticate") },
            text = {
                Column {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReAuth(email, password)
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismissRequest() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SelectPlayListDialog(
    playlists: List<String>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    var selectedPlaylist by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        iconContentColor = Color.White,
        containerColor = Color(0xFF121212),
        textContentColor = Color.White,
        shape = MaterialTheme.shapes.small,
        tonalElevation = 0.dp,
        title = { Text("Select Playlist", color = Color.White) },
        text = {
            Column {
                Text("Choose a playlist to add the song to", color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                playlists.forEach { playlist ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedPlaylist = playlist
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPlaylist == playlist,
                            onClick = { selectedPlaylist = playlist }
                        )
                        Text(playlist, color = Color.White)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF121212)
                ),
                border = BorderStroke(1.dp, Color.White),
                onClick = {
                    if (selectedPlaylist.isEmpty()) {
                        Toast.makeText(context, "Please select a playlist", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        onSelect(selectedPlaylist)
                        onDismiss()
                    }
                }
            ) {
                Text("Select")
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF121212)
                ),
                border = BorderStroke(1.dp, Color.White),
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun CreatePlayListDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit,
) {
    var playlistName by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        iconContentColor = Color.White,
        containerColor = Color(0xFF121212),
        textContentColor = Color.White,
        shape = MaterialTheme.shapes.small,
        tonalElevation = 0.dp,
        title = { Text("Create New Playlist", color = Color.White) },
        text = {
            Column {
                TextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = { Text("Playlist Name") }
                )

            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF121212)
                ),
                border = BorderStroke(1.dp, Color.White),
                onClick = {
                    if (playlistName.isEmpty()) {
                        Toast.makeText(context, "Please enter playlist name", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        onCreate(playlistName)
                        onDismiss()
                    }
                }) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF121212)
                ),
                border = BorderStroke(1.dp, Color.White),
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MiniPlayer(
    currentTrack: Song,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onTrackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xFF121212))
            .clickable { onTrackClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = currentTrack.picture ?: "")
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            ),
            contentDescription = "Playlist Cover",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .width(70.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentTrack.title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            currentTrack.artist?.let {
                Text(
                    text = it.name,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        IconButton(onClick = onPlayPauseClick) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = if (!isPlaying) R.drawable.play else R.drawable.pause),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    value: String,
    color: Color,
    colorText: Color,
    onClicked: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(70.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        onClick = onClicked
    ) {
        Text(
            text = value,
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            color = colorText,
            fontSize = 19.sp
        )
    }
}

@Composable
fun SocialMediaLogIn(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    color: Color,
    onClick: () -> Unit
) {

    Row(modifier = modifier
        .clip(RoundedCornerShape(30.dp))
        .clickable {
            onClick()
        }
        .height(60.dp)
        .background(color = Color.Transparent)
        .border(
            width = 0.8.dp, color = Color.White,
            shape = RoundedCornerShape(30.dp)
        )
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround

    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
        )


        Text(
            text = text,
            color = color,
            modifier = modifier.fillMaxWidth(0.7f),
            fontSize = 20.sp,
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )

    }
}

@Composable
fun RoundAvatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentScale = ContentScale.Crop,

        )
}

@Composable
fun LocalImageRoundAvatar(
    resourceId: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = "Local Avatar",
        modifier = modifier
            .size(55.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier) {
    val screens = listOf(
        BottomBarRoutes.Home,
        BottomBarRoutes.Search,
        BottomBarRoutes.Library
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color(0xFF121212))
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    navController = navController,
                    currentDestination = navBackStackEntry?.destination
                )
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomBarRoutes,
    navController: NavHostController,
    currentDestination: NavDestination?,
) {
    NavigationBarItem(
        label = {
            Text(
                text = screen.title,
                fontSize = 12.sp,
                fontFamily = roboto,
                fontWeight = FontWeight.Bold,
                color = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                    Color(0xBFFFFFFF)
                } else {
                    Color(0x80FFFFFF)
                }
            )
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon",
                tint = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                    Color(0xBFFFFFFF)
                } else {
                    Color(0x80FFFFFF)
                },
                modifier = Modifier.size(34.dp)
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
        ),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun ImageBoxMedium(
    modifier: Modifier = Modifier,
    imageUrl: String,
    text: String,
    onClick: () -> Unit
) {
    Column {
        Box(
            modifier = modifier
                .size(100.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(28.dp)
                )
                .clickable { onClick() }
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                ),
                contentDescription = "Playlist Cover",
                contentScale = ContentScale.Crop, // Dùng để ảnh vừa khít và che đầy box
                modifier = modifier
                    .height(100.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = modifier.width(100.dp),
            text = text,
            fontFamily = fontInter,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color(0xBFFFFFFF),
            maxLines = 1, // Giới hạn text hiển thị chỉ một hàng
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Composable
fun ImageBoxLarge(
    modifier: Modifier = Modifier,
    imageUrl: String,
    text: String,
    onClick: () -> Unit
) {
    Column {
        Box(
            modifier = modifier
                .size(140.dp)
                .clickable { onClick() }
                .background(color = Color.Transparent, shape = RoundedCornerShape(28.dp))
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                ),
                contentDescription = "Playlist Cover",
                contentScale = ContentScale.Crop, // Dùng để ảnh vừa khít và che đầy box
                modifier = Modifier
                    .height(150.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = modifier.width(100.dp),
            text = text,
            fontFamily = fontInter,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color(0xBFFFFFFF),
            maxLines = 1, // Giới hạn text hiển thị chỉ một hàng
            overflow = TextOverflow.Ellipsis
        )
    }

}


@Composable
fun ImageBoxSongList(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    text: String
) {
    Box(
        modifier = modifier
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
            modifier = modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )


        Text(
            text = text,
            modifier = modifier
                .fillMaxHeight(1f)
                .fillMaxWidth(fraction = 1f)
                .padding(top = 200.dp),
            fontFamily = fontInter,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color(0xBFFFFFFF),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageBoxExtraLarge(
    modifier: Modifier = Modifier,
    imageUrl: String,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(150.dp)
            .width(280.dp)
            .clickable { onClick() }

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
            modifier = modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Văn bản
        Text(
            text = text,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = 0.5f)
                .align(alignment = Alignment.CenterStart)
                .padding(
                    horizontal = 15.dp,
                    vertical = 35.dp
                ), // Đệm cho văn bản để không dính sát cạnh
            fontFamily = fontInter,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = Color(0xBFFFFFFF),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun ImageBoxPlay(
    modifier: Modifier = Modifier,
    imageUrl: String,
) {
    Box(
        modifier = modifier
            .size(380.dp)
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
            modifier = modifier.matchParentSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        value = value,
        onValueChange = onValueChange,
        maxLines = 1,
        shape = RoundedCornerShape(7.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = Color(0xFFFFFFFF),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = fontInter
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color(0xFF777777)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Row(modifier = modifier) {
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    val image = if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFFFFFFFF),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = fontInter
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color(0xFF777777)
            )
        )
    }
}


@Preview
@Composable
fun BottomBarPreview() {

}



