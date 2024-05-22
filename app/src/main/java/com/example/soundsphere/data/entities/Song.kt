package com.example.soundsphere.data.entities

data class Song(
    val songId : Int = 0,
    val title : String = "",
    val artist : String = "",
    val imageUrl : String = "",
    val songUrl : String = "",
    val genre : String = ""
)