package com.example.soundsphere.ui.search

import com.example.soundsphere.data.dtodeezer.search.searchAlbum.SearchAlbumDto
import com.example.soundsphere.data.dtodeezer.search.searchArtist.SearchArtistDto

data class SearchArtistState(
    val isLoading: Boolean = false,
    val isSuccessful: SearchArtistDto? = null,
    val isError: String? = ""
)