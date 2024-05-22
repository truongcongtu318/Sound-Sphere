package com.example.soundsphere.ui.song_list

import com.example.soundsphere.data.dto.track.DataTrackDto
import com.example.soundsphere.data.model.Track


data class TrackByIdState (
    val isLoading: Boolean = false,
    val isSuccessDataTrackState: DataTrackDto? = null,
    val track: Track? = null,
    val isError: String? = ""
)