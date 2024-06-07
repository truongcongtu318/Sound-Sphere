package com.example.soundsphere.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val artist: Artist? = null,
    val album: Album? =null,
    val disk_number: Int?= null,
    val duration: String? = null,
    val id: String? = null,
    val isrc: String? = null,
    val link: String? = null,
    val md5_image: String? = null,
    val preview: String? = null,
    val rank: String? = null,
    val readable: Boolean? = null,
    val title: String? = null,
    val title_short: String? = null,
    val title_version: String? = null,
    val track_position: Int = 0,
    val type: String? = null
): Parcelable