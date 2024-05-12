package com.example.soundsphere.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.soundsphere.ui.LibraryScreen
import com.example.soundsphere.ui.home.HomeScreen
import com.example.soundsphere.ui.search.SearchScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarRoutes.Home.route) {
        composable(route = BottomBarRoutes.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarRoutes.Library.route) {
            LibraryScreen()
        }
        composable(route = BottomBarRoutes.Search.route) {
            SearchScreen()
        }

    }

}