package com.example.soundsphere

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.soundsphere.data.model.TopSongs
import com.example.soundsphere.data.remote.SoundSphereApiService
import com.example.soundsphere.navigation.BottomNavGraph
import com.example.soundsphere.ui.components.BottomBar
import com.example.soundsphere.ui.theme.SoundSphereTheme
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var soundSphereApi: SoundSphereApiService
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            SoundSphereTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) {
                    BottomNavGraph(navController = navController)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SoundSphereTheme {

    }
}