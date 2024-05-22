package com.example.soundsphere.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.soundsphere.ui.LibraryScreen
import com.example.soundsphere.ui.home.HomeScreen
import com.example.soundsphere.ui.login.LoginScreen
import com.example.soundsphere.ui.login.LoginViewModel
import com.example.soundsphere.ui.play.PlayScreen
import com.example.soundsphere.ui.profile.ProfileScreen
import com.example.soundsphere.ui.search.SearchScreen
import com.example.soundsphere.ui.song_list.SongListScreen
import com.example.soundsphere.ui.song_list.SongListScreenPlayList

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val viewModel: LoginViewModel = hiltViewModel()
    val checkUser = viewModel.isLoggedIn.value
    val startDestination = rememberSaveable() {
        mutableStateOf(
            if (checkUser) BottomBarRoutes.Home.route
            else NavigationRoutes.Login.route
        )
    }
    NavHost(navController = navController, startDestination = startDestination.value) {
        composable(route = BottomBarRoutes.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = BottomBarRoutes.Library.route) {
            LibraryScreen(navController = navController)
        }
        composable(route = BottomBarRoutes.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(route = NavigationRoutes.Login.route) {
            LoginScreen(navController)
        }
        composable(route = NavigationRoutes.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.SongList.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("id")
            albumId?.let {
                SongListScreen(id = it, navController = navController)
            }
        }
        composable(
            route = "${NavigationRoutes.SongListPlayList.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                SongListScreenPlayList(id = id, navController = navController)
            }

        }

        composable(
            route = "${NavigationRoutes.PlayTrack.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                PlayScreen(navController = navController , id = id)
            }

        }
    }
}


