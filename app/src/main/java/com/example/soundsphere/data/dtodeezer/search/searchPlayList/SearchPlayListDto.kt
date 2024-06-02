package com.example.soundsphere.data.dtodeezer.search.searchPlayList

data class SearchPlayListDto(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)