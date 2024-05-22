package com.example.soundsphere.data.dto.albums

data class Album(
    val album_type: String,
    val artists: List<Artist>,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val total_tracks: Int,
    val tracks: Tracks,
    val type: String,
)