package com.example.soundsphere.ui.search

import com.example.soundsphere.data.dtodeezer.genre.GenreDto
import com.example.soundsphere.data.dtodeezer.playlisttracks.PlayListTrackDto

data class GenresState(
    val isLoading: Boolean = false,
    val isSuccessful: GenreDto? = null,
    val isError: String? = ""
)