package com.example.soundsphere.data.dto.search

data class Episodes(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)