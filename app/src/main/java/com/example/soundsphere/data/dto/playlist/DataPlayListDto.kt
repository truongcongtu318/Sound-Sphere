package com.example.soundsphere.data.dto.playlist

data class DataPlayListDto(
    val collaborative: Boolean,
    val description: String,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val primary_color: Any,
    val `public`: Boolean,
    val snapshot_id: String,
    val tracks: Tracks,
    val type: String,
    val uri: String
)