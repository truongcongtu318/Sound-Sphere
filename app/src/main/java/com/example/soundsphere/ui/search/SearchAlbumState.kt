package com.example.soundsphere.ui.search

import com.example.soundsphere.data.dtodeezer.search.searchAlbum.SearchAlbumDto

data class SearchAlbumState(
    val isLoading: Boolean = false,
    val isSuccessful: SearchAlbumDto? = null,
    val isError: String? = ""
)