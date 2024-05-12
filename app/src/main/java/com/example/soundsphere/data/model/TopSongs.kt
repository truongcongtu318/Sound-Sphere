package com.example.soundsphere.data.model

data class TopSongs(
    val chart_items: List<ChartItem>,
    val next_page: Int
)