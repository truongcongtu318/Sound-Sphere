package com.example.soundsphere.data.dto.albums

data class Item(
    val artists: List<Artist>,
    val duration_ms: Int,
    val href: String,
    val id: String,
    val name: String,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String
)