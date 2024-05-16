package com.example.soundsphere.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.soundsphere.ui.LibraryScreen
import com.example.soundsphere.ui.home.HomeScreen
import com.example.soundsphere.ui.login.LoginScreen
import com.example.soundsphere.ui.login.LoginViewModel
import com.example.soundsphere.ui.search.SearchScreen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val checkUser = viewModel.isLoggedIn.value
    val startDestination = rememberSaveable() {
        mutableStateOf(NavigationRoutes.Login.route)
    }
    if (checkUser) {
        startDestination.value = BottomBarRoutes.Home.route
    }else{
        startDestination.value = NavigationRoutes.Login.route
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

    }

}