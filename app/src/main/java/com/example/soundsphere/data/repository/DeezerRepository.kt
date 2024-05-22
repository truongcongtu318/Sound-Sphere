package com.example.soundsphere.data.repository

import com.example.soundsphere.data.dtodeezer.chart.ChartDto
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DeezerRepository {
    fun getCharts() : Flow<Resource<ChartDto>>
}