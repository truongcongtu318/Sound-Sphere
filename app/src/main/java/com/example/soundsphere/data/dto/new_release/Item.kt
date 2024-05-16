package com.example.soundsphere.data.dto.new_release

data class Item(
    val album_type: String,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsX,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val restrictions: Restrictions,
    val total_tracks: Int,
    val type: String,
    val uri: String
)