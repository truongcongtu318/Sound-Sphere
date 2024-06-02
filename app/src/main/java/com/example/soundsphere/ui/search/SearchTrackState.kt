package com.example.soundsphere.ui.search

import com.example.soundsphere.data.dtodeezer.search.searchAlbum.SearchAlbumDto
import com.example.soundsphere.data.dtodeezer.search.searchTrack.SearchTrackDto

data class SearchTrackState(
    val isLoading: Boolean = false,
    val isSuccessful: SearchTrackDto? = null,
    val isError: String? = ""
)