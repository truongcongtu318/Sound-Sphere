package com.example.soundsphere.ui.song_list

import com.example.soundsphere.data.dtodeezer.artist_top.ArtistTopDto
import com.example.soundsphere.data.dtodeezer.playlist.PlayListDto

data class ArtistTopState(
    val isLoading: Boolean = false,
    val isSuccessful: ArtistTopDto? = null,
    val isError: String? = ""
)