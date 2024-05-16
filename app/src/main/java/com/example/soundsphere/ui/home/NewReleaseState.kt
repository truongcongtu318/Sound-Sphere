package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto

data class NewReleaseState(
    val isLoading: Boolean = false,
    val isSuccessDataNewRelease: DataNewReleaseDto? = null,
    val isError: String? = ""
)