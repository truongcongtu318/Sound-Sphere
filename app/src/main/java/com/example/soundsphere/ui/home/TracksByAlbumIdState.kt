package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.albums.Tracks

data class TracksByAlbumIdState(
    val isLoading: Boolean = false,
    val isSuccessDataTrackByAlbumId:Tracks? = null,
    val isError: String? = ""
)