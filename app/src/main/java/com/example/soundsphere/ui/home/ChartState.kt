package com.example.soundsphere.ui.home

import com.example.soundsphere.data.dtodeezer.chart.ChartDto

data class ChartState(
    val isLoading: Boolean = false,
    val isSuccessful: ChartDto? = null,
    val isError: String? = ""
)