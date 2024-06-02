package com.example.soundsphere.ui.song_list

import com.example.soundsphere.data.dtodeezer.playlist.PlayListDto


data class PlayListState(
    val isLoading: Boolean = false,
    val isSuccessful: PlayListDto? = null,
    val isError: String? = ""
)