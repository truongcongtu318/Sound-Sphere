package com.example.soundsphere.data.dtodeezer.search.searchAlbum

data class SearchAlbumDto(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)