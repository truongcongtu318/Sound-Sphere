package com.example.soundsphere.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarRoutes(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarRoutes("home", "Home", Icons.Rounded.Home)
    object Search : BottomBarRoutes("search", "Search", Icons.Rounded.Search)
    object Library : BottomBarRoutes("library", "Library", Icons.Rounded.LibraryMusic)
}