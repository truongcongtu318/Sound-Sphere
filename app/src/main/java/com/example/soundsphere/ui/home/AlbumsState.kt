package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.albums.DataAlbumsDto
import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto

data class AlbumsState(
    val isLoading: Boolean = false,
    val isSuccessDataAlbums: DataAlbumsDto? = null,
    val isError: String? = ""
)