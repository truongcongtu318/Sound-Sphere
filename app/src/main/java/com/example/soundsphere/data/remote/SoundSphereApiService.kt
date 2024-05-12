package com.example.soundsphere.data.remote

import com.example.soundsphere.data.model.TopSongs
import com.example.soundsphere.utils.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface SoundSphereApiService {

    @Headers(
        "X-RapidAPI-Key: ${Constants.RAPID_API_KEY}",
        "X-RapidAPI-Host: ${Constants.RAPID_API_HOST}"
    )
    @GET("chart/songs/")
    suspend fun getTopChartSongs(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ) : Response<TopSongs>
}