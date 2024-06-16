package com.example.soundsphere.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    val id: String = "",
    val title: String = "",
    val artist: Artist? = null,
    val albumId: String? = null,
    val genreId: String = "",
    val duration: Float = 0f,
    val url: String = "",
    val picture: String = ""
) : Parcelable