package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.playlist.DataPlayListDto
import com.example.soundsphere.data.dto.search.DataSearchDto

data class SearchState(
    val isLoading: Boolean = false,
    val isSuccessDataPlayList: DataSearchDto? = null,
    val isError: String? = ""
)