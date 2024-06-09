package com.example.soundsphere.data.dtodeezer.chart

import com.example.soundsphere.data.model.Album
import com.example.soundsphere.data.model.Artist

data class DataXXXX(
    val album: Album,
    val artist: Artist,
    val duration: Int,
    val explicit_content_cover: Int,
    val explicit_content_lyrics: Int,
    val explicit_lyrics: Boolean,
    val id: Long,
    val link: String,
    val md5_image: String,
    val position: Int,
    val preview: String,
    val rank: Int,
    val title: String,
    val title_short: String,
    val title_version: String,
    val type: String
)