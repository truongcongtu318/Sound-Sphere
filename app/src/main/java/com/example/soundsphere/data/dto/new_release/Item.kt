package com.example.soundsphere.data.dto.new_release

data class Item(
    val album_type: String,
    val artists: List<Artist>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val total_tracks: Int,
    val type: String,
)