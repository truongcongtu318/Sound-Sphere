package com.example.soundsphere.ui.song_list

import com.example.soundsphere.data.dtodeezer.track.TrackDto

data class TrackState(
    val isLoading: Boolean = false,
    val isSuccessful: TrackDto? = null,
    val isError: String? = ""
)