package com.example.soundsphere.ui.search

import com.example.soundsphere.data.dtodeezer.search.searchAlbum.SearchAlbumDto
import com.example.soundsphere.data.dtodeezer.search.searchPlayList.SearchPlayListDto

data class SearchPlayListState(
    val isLoading: Boolean = false,
    val isSuccessful: SearchPlayListDto? = null,
    val isError: String? = ""
)