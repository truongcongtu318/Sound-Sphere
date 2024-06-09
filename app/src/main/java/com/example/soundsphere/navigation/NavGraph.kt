package com.example.soundsphere.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.soundsphere.MiniPlayer
import com.example.soundsphere.player.service.JetAudioService
import com.example.soundsphere.player.service.JetAudioServiceHandler
import com.example.soundsphere.ui.auth.AuthViewModel
import com.example.soundsphere.ui.components.BottomBar
import com.example.soundsphere.ui.firestore.firestoreViewModel
import com.example.soundsphere.ui.library.LibraryScreen
import com.example.soundsphere.ui.home.HomeScreen
import com.example.soundsphere.ui.home.HomeViewModel
import com.example.soundsphere.ui.library.LibraryViewModel
import com.example.soundsphere.ui.login.LoginScreen
import com.example.soundsphere.ui.login.LoginScreenEmail
import com.example.soundsphere.ui.login.LoginViewModel
import com.example.soundsphere.ui.play.PlayScreen
import com.example.soundsphere.ui.play.PlayViewModel
import com.example.soundsphere.ui.play.UIEvents
import com.example.soundsphere.ui.play.UiState
import com.example.soundsphere.ui.profile.ProfileScreen
import com.example.soundsphere.ui.profile.ProfileViewModel
import com.example.soundsphere.ui.register.RegisterScreen
import com.example.soundsphere.ui.register.RegisterViewModel
import com.example.soundsphere.ui.register.WaitingVerifyEmailScreen
import com.example.soundsphere.ui.search.SearchScreen
import com.example.soundsphere.ui.search.SearchViewModel
import com.example.soundsphere.ui.song_list.SongListScreen
import com.example.soundsphere.ui.song_list.SongListScreenArtist
import com.example.soundsphere.ui.song_list.SongListScreenPlayList
import com.example.soundsphere.ui.song_list.SongListViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(UnstableApi::class)
@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val playViewModel: PlayViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val libraryViewModel: LibraryViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val songListViewModel: SongListViewModel = hiltViewModel()
    val firestoreViewModel: firestoreViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val facebookState = authViewModel.facebookState.collectAsState()

    var isServiceRunning by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isEmailVerified by authViewModel.isEmailVerified.collectAsState()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()

    authViewModel.checkUserAuthentication()
    LaunchedEffect(isUserAuthenticated) {
        if (isUserAuthenticated) {
            authViewModel.checkEmailVerification()
        }
    }

    val startDestination =
        if (!isUserAuthenticated || !isEmailVerified) NavigationRoutes.Login.route else BottomBarRoutes.Home.route

    fun startService() {
        if (!isServiceRunning) {
            val intent = Intent(context, JetAudioService::class.java)
            Util.startForegroundService(context, intent)
            isServiceRunning = true
        }
    }


    LaunchedEffect(playViewModel.isPlaying) {
        if (playViewModel.isPlaying) {
            startService()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.weight(1f)
        ) {
            composable(route = BottomBarRoutes.Home.route) {
                HomeScreen(
                    navController = navController,
                    profileViewModel = profileViewModel,
                    viewModel = homeViewModel,
                    playViewModel = playViewModel
                )
            }
            composable(route = NavigationRoutes.Register.route) {
                RegisterScreen(
                    navController = navController, authViewModel = authViewModel
                )
            }
            composable(route = NavigationRoutes.LoginEmail.route) {
                LoginScreenEmail(
                    navController = navController, authViewModel = authViewModel
                )
            }
            composable(route = NavigationRoutes.WaitingVerifyEmailScreen.route) {
                WaitingVerifyEmailScreen(
                    navController = navController, authViewModel = authViewModel
                )
            }
            composable(route = BottomBarRoutes.Library.route) {
                LibraryScreen(
                    navController = navController,
                    firestoreViewModel = firestoreViewModel,
                    playViewModel = playViewModel,
                )
            }
            composable(route = BottomBarRoutes.Search.route) {
                SearchScreen(
                    navController = navController,
                    viewModel = homeViewModel,
                    searchViewModel = searchViewModel
                )
            }
            composable(route = NavigationRoutes.Login.route) {
                LoginScreen(navController = navController, authViewModel = authViewModel)
            }
            composable(route = NavigationRoutes.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    viewModel = profileViewModel,
                    authViewModel = authViewModel
                )
            }
            composable(
                route = "${NavigationRoutes.SongList.route}/{urlTrackList}/{urlAlbum}",
                arguments = listOf(navArgument("urlTrackList") { type = NavType.StringType },
                    navArgument("urlAlbum") { type = NavType.StringType })
            ) { backStackEntry ->
                val urlTrackList = backStackEntry.arguments?.getString("urlTrackList")
                val urlAlbum = backStackEntry.arguments?.getString("urlAlbum")
                urlTrackList?.let {
                    urlAlbum?.let {
                        SongListScreen(
                            urlTrackList = urlTrackList,
                            urlAlbum = urlAlbum,
                            navController = navController,
                            playViewModel = playViewModel,
                            viewModel = homeViewModel
                        )
                    }
                }
            }

            composable(
                route = "${NavigationRoutes.SongListPlayList.route}/{urlTrackList}/{idPlayList}",
                arguments = listOf(navArgument("idPlayList") { type = NavType.StringType },
                    navArgument("urlTrackList") { type = NavType.StringType })
            ) { backStackEntry ->
                val idPlayList = backStackEntry.arguments?.getString("idPlayList")
                val urlTrackList = backStackEntry.arguments?.getString("urlTrackList")
                urlTrackList?.let {
                    idPlayList?.let {
                        SongListScreenPlayList(
                            urlTrackList = urlTrackList,
                            idPlayList = idPlayList,
                            navController = navController,
                            songListViewModel = songListViewModel,
                            playViewModel = playViewModel
                        )
                    }
                }
            }

            composable(
                route = "${NavigationRoutes.SongListArtist.route}/{urlTrackList}",
                arguments = listOf(navArgument("urlTrackList") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val urlTrackList = backStackEntry.arguments?.getString("urlTrackList")
                urlTrackList?.let {
                    SongListScreenArtist(
                        urlTrackList = urlTrackList,
                        navController = navController,
                        songListViewModel = songListViewModel,
                        playViewModel = playViewModel
                    )
                }
            }

            composable(
                route = NavigationRoutes.PlayTrack.route
            ) {
                PlayScreen(
                    navController = navController,
                    playViewModel = playViewModel,
                )
            }
        }

        if (currentRoute != null && isUserAuthenticated && (isEmailVerified || facebookState.value.success != null)) {
            if (playViewModel.currentSelectedTrack.id != null && currentRoute.substringBefore("/") != NavigationRoutes.PlayTrack.route) {
                MiniPlayer(
                    currentTrack = playViewModel.currentSelectedTrack,
                    isPlaying = playViewModel.isPlaying,
                    onPlayPauseClick = {
                        if (playViewModel.uiState.value == UiState.Initial) {
                            playViewModel.setCurrentTrackSelected(playViewModel.currentSelectedTrack)
                        }
                        playViewModel.onUiEvent(UIEvents.PlayPause)
                    },
                    onTrackClick = {
                        navController.navigate(NavigationRoutes.PlayTrack.route)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (currentRoute !in listOf(
                NavigationRoutes.LoginEmail.route,
                NavigationRoutes.WaitingVerifyEmailScreen.route,
                NavigationRoutes.Register.route,
                NavigationRoutes.Login.route
            )
        ) {
            HorizontalDivider(
                thickness = 1.dp, color = Color.Gray
            )
            BottomBar(
                navController = navController, modifier = Modifier.weight(1f)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun NavGraphPreview() {
    NavGraph()
}


