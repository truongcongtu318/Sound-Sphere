package com.example.soundsphere.ui.library

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.soundsphere.ui.components.BottomBar
import com.example.soundsphere.ui.profile.YourLibrary
import com.example.soundsphere.ui.theme.fontInter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LibraryScreen(
    navController: NavHostController,
    libraryViewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
    val state = libraryViewModel.libraryState.collectAsState()
    Log.d("098",state.value.isSuccessful.toString())
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFF121212),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Library",
                modifier = modifier
                    .padding(top = 20.dp),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = fontInter
            )

            YourLibrary(modifier = modifier)
        }
    }

}

@Preview
@Composable
fun LibraryScreenPreview() {

}