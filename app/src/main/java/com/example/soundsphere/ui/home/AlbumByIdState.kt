package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.albums.Album

data class AlbumByIdState(
    val isLoading: Boolean = false,
    val isSuccessDataAlbumById: Album? = null,
    val isError: String? = ""
)