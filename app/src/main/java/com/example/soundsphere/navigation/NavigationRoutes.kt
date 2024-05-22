package com.example.soundsphere.navigation

sealed class NavigationRoutes(val route: String) {
    object Login : NavigationRoutes("login")
    object Profile : NavigationRoutes("profile")
    object SongList : NavigationRoutes("songlist")
    object SongListPlayList : NavigationRoutes("songlistplaylist")
    object PlayTrack : NavigationRoutes("playtrack")
}