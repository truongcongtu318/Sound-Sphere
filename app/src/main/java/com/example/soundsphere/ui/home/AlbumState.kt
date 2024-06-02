package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dtodeezer.album.AlbumDto

data class AlbumState(
    val isLoading: Boolean = false,
    val isSuccessful: AlbumDto? = null,
    val isError: String? = ""
)