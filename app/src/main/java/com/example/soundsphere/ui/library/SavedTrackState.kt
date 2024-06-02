package com.example.soundsphere.ui.library

import com.example.soundsphere.data.dtodeezer.albumtracks.AlbumTrackDto
import com.example.soundsphere.data.repository.SavedTrack

data class SavedTrackState (
    val isLoading: Boolean = false,
    val isSuccessful: List<SavedTrack>? = null,
    val isError: String? = ""
)