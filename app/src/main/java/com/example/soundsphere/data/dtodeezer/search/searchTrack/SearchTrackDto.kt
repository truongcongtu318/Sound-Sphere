package com.example.soundsphere.data.dtodeezer.search.searchTrack

data class SearchTrackDto(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)