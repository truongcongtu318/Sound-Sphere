package com.example.soundsphere.navigation

sealed class NavigationRoutes(val route: String) {
    object Login : NavigationRoutes("login")
}