package com.example.soundsphere.data.repository

import android.util.Log
import com.example.soundsphere.data.dtodeezer.chart.ChartDto
import com.example.soundsphere.data.remote.DeezerApiService
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeezerRepositoryImpl @Inject constructor(
    private val deezerApiService: DeezerApiService
) : DeezerRepository {
    override fun getCharts(): Flow<Resource<ChartDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getCharts()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                    Log.d("DeezerRepositoryImpl", "getCharts: ${response.body()}")
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }
}