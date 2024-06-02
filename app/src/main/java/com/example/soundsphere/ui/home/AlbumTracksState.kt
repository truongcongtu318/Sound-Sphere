package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dtodeezer.albumtracks.AlbumTrackDto

data class AlbumTracksState(
    val isLoading: Boolean = false,
    val isSuccessful: AlbumTrackDto? = null,
    val isError: String? = ""
)