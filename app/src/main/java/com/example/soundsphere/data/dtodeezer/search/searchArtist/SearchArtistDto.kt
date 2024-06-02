package com.example.soundsphere.data.dtodeezer.search.searchArtist

data class SearchArtistDto(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)