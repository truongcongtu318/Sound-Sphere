package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.feature_playlist.DataFeaturePlaylistDto

data class FeaturePlayListState (
    val isLoading: Boolean = false,
    val isSuccessDataPlayListFeature: DataFeaturePlaylistDto? = null,
    val isError: String? = ""
)