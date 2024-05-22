package com.example.soundsphere.ui.song_list

import com.example.soundsphere.data.dto.playlist.DataPlayListDto

data class PlayListState(
    val isLoading: Boolean = false,
    val isSuccessDataPlayList: DataPlayListDto? = null,
    val isError: String? = ""
)