package com.example.soundsphere.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val id: String,
    val name: String,
    val tracklist: String,
    val type: String
): Parcelable