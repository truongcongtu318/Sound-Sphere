package com.example.soundsphere.data.dto.playlist

data class Track(
    val album: Album,
    val artists: List<ArtistX>,
    val duration_ms: Int,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track: Boolean,
    val track_number: Int,
    val type: String,
    val uri: String
)