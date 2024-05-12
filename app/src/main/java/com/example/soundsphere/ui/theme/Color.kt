package com.example.soundsphere.ui.theme

import android.annotation.SuppressLint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val white = Color(0xFFFFFFFF)
@SuppressLint("InvalidColorHexValue")
val gray = Color(0xFF7777777)
val black_1 = Color(0xFF282828)
val black = Color(0xFF121212)
val green = Color(0xFF1ED760)

val linearBottom = linearGradient(
    colors = listOf(Color(0xC2000000), Color(0x00000000)),
    start = Offset(0f, 160f),
    end = Offset(0f, 0f)
)
