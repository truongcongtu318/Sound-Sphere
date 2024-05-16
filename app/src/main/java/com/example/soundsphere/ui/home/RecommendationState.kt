package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dto.recomendation.DataRecommendationDto

data class RecommendationState(
    val isLoading: Boolean = false,
    val isSuccessDataRecommendation: DataRecommendationDto? = null,
    val isError: String? = ""
)