package com.example.soundsphere.data.dto.feature_playlist

data class Item(
    val collaborative: Boolean,
    val description: String,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val type: String,
)