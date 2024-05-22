package com.example.soundsphere.data.remote

import com.example.soundsphere.data.dtodeezer.chart.ChartDto
import retrofit2.Response
import retrofit2.http.GET

interface DeezerApiService {
    @GET("chart/0")
    suspend fun getCharts() : Response<ChartDto>
}