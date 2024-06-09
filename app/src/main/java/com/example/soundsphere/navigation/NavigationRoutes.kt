package com.example.soundsphere.navigation

sealed class NavigationRoutes(val route: String) {
    object Login : NavigationRoutes("login")
    object LoginEmail : NavigationRoutes("loginemail")
    object Register : NavigationRoutes("register")
    object WaitingVerifyEmailScreen : NavigationRoutes("wait")
    object Profile : NavigationRoutes("profile")
    object SongList : NavigationRoutes("songlist")
    object SongListPlayList : NavigationRoutes("songlistplaylist")
    object SongListArtist : NavigationRoutes("songlistartist")
    object PlayTrack : NavigationRoutes("playtrack")
}