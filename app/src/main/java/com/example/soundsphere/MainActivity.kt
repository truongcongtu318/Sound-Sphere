package com.example.soundsphere

//import com.example.soundsphere.ui.theme.SoundSphereTheme
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.soundsphere.data.remote.SoundSphereApiService
import com.example.soundsphere.navigation.NavGraph
import com.example.soundsphere.ui.home.HomeViewModel
import com.facebook.FacebookSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var soundSphereApi: SoundSphereApiService

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            val viewModel: HomeViewModel = hiltViewModel()
            val navController = rememberNavController()

            Scaffold(
            ) {
                NavGraph(navController = navController)
                Log.d("checkLogin", navController.currentDestination?.route.toString())
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}