package com.example.soundsphere.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.soundsphere.ui.library.LibraryScreen
import com.example.soundsphere.ui.home.HomeScreen
import com.example.soundsphere.ui.login.LoginScreen
import com.example.soundsphere.ui.login.LoginViewModel
import com.example.soundsphere.ui.play.PlayScreen
import com.example.soundsphere.ui.profile.ProfileScreen
import com.example.soundsphere.ui.search.SearchScreen
import com.example.soundsphere.ui.song_list.SongListScreen
import com.example.soundsphere.ui.song_list.SongListScreenArtist
import com.example.soundsphere.ui.song_list.SongListScreenPlayList

@RequiresApi(Build.VERSION_CODES.S)
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
            route = "${NavigationRoutes.SongList.route}/{urlTrackList}/{urlAlbum}",
            arguments = listOf(
                navArgument("urlTrackList") { type = NavType.StringType },
                navArgument("urlAlbum") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val urlTrackList = backStackEntry.arguments?.getString("urlTrackList")
            val urlAlbum = backStackEntry.arguments?.getString("urlAlbum")
            urlTrackList?.let {
                urlAlbum?.let {
                    SongListScreen(
                        urlTrackList = urlTrackList,
                        urlAlbum = urlAlbum,
                        navController = navController
                    )
                }
            }
        }

        composable(
            route = "${NavigationRoutes.SongListPlayList.route}/{urlTrackList}/{idPlayList}",
            arguments = listOf(
                navArgument("idPlayList") { type = NavType.StringType },
                navArgument("urlTrackList") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idPlayList = backStackEntry.arguments?.getString("idPlayList")
            val urlTrackList = backStackEntry.arguments?.getString("urlTrackList")
            urlTrackList?.let {
                idPlayList?.let {
                    SongListScreenPlayList(
                        urlTrackList = urlTrackList,
                        idPlayList = idPlayList,
                        navController = navController
                    )
                }
            }
        }

        composable(
            route = "${NavigationRoutes.SongListArtist.route}/{urlTrackList}",
            arguments = listOf(
                navArgument("urlTrackList") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val urlTrackList = backStackEntry.arguments?.getString("urlTrackList")
            urlTrackList?.let {
                SongListScreenArtist(
                    urlTrackList = urlTrackList,
                    navController = navController
                )
            }
        }

        composable(
            route = "${NavigationRoutes.PlayTrack.route}/{id}/{urlTrackList}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("urlTrackList") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val urlTrackList = backStackEntry.arguments?.getString("urlTrackList")
            if (id != null) {
                PlayScreen(navController = navController, urlTrackList = urlTrackList, id = id)
            }

        }

    }
}


