package com.example.soundsphere.data.dtodeezer.albumtracks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val id: String,
    val name: String,
    val tracklist: String,
    val type: String
): Parcelable