package com.example.soundsphere.data.dtodeezer.playlisttracks

data class PlayListTrackDto(
    val checksum: String,
    val `data`: List<Data>,
    val next: String,
    val total: Int
)