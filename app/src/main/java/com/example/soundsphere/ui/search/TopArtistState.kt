package com.example.soundsphere.ui.search

import com.example.soundsphere.data.dto.top_artists.DataTopArtistDto

data class TopArtistState(
    val isLoading: Boolean = false,
    val isSuccessDataTopArtist: DataTopArtistDto? = null,
    val isError: String = ""
)