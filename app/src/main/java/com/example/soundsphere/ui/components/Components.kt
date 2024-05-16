package com.example.soundsphere.ui.components

import androidx.annotation.DrawableRes
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.soundsphere.navigation.BottomBarRoutes
import com.example.soundsphere.ui.theme.fontInter
import com.example.soundsphere.ui.theme.linearBottom
import com.example.soundsphere.ui.theme.roboto

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
            containerColor = color  // Thiết lập màu nền của Button
        ),
        onClick = onClicked
    ) {
        Text(
            text = value,
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            color = colorText,
            fontSize = 20.sp
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
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
            }).build()
        ),
        contentDescription = "Avatar",
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape), // Làm tròn hình ảnh
        contentScale = ContentScale.Crop // Giúp hình ảnh phù hợp với kích thước đã cho
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
fun BottomBar(navController: NavHostController) {
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
            modifier = Modifier.align(Alignment.BottomCenter).background(linearBottom)
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
    text: String
) {
    Column {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(28.dp))
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
                ),
                contentDescription = "Playlist Cover",
                contentScale = ContentScale.Crop, // Dùng để ảnh vừa khít và che đầy box
                modifier = Modifier
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
    text: String
) {
    Column {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(28.dp))
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
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
fun ImageBoxExtraLarge(
    modifier: Modifier = Modifier,
    imageUrl: String,
    text: String
) {
    Box(
        modifier = Modifier
            .height(150.dp)
            .width(280.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
            ),
            contentDescription = "Playlist Cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
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
            modifier = Modifier
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

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navController = rememberNavController())
}



