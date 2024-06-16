package com.example.soundsphere.data.model

data class Playlist(
    val name: String = "",
    val creator: String = "",
    val playlistName: String = "",
    val songs: List<Song> = listOf()
)