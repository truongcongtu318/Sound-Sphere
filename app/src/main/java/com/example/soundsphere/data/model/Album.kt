package com.example.soundsphere.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: String = "",
    val title: String = "",
    val artistId: String = "",
    val picture: String = "",
): Parcelable
