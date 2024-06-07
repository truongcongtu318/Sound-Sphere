package com.example.soundsphere.data.dtodeezer.playlisttracks

import com.example.soundsphere.data.model.Album
import com.example.soundsphere.data.model.Artist

data class Data(
    val album: Album,
    val artist: Artist,
    val duration: String,
    val explicit_content_cover: Int,
    val explicit_content_lyrics: Int,
    val explicit_lyrics: Boolean,
    val id: String,
    val isrc: String,
    val link: String,
    val md5_image: String,
    val preview: String,
    val rank: String,
    val readable: Boolean,
    val time_add: Int,
    val title: String,
    val title_short: String,
    val title_version: String,
    val type: String
)