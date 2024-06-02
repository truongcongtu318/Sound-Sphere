package com.example.soundsphere.ui.song_list

import com.example.soundsphere.data.dtodeezer.playlist.PlayListDto
import com.example.soundsphere.data.dtodeezer.playlisttracks.PlayListTrackDto

data class PlayListTrackState(
    val isLoading: Boolean = false,
    val isSuccessful: PlayListTrackDto? = null,
    val isError: String? = ""
)