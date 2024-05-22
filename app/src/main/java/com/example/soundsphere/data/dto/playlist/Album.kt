package com.example.soundsphere.data.dto.playlist

data class Album(
    val album_type: String,
    val artists: List<ArtistX>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)