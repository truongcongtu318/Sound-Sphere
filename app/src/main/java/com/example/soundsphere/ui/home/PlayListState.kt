package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto
import com.example.soundsphere.data.dto.playlist.DataPlayListDto

data class PlayListState(
    val isLoading: Boolean = false,
    val isSuccessDataPlayList: DataPlayListDto? = null,
    val isError: String? = ""
)