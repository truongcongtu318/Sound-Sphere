package com.example.soundsphere.data.repository

import com.example.soundsphere.data.model.TopSongs
import com.example.soundsphere.data.remote.SoundSphereApiService
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SoundSphereApiRepositoryImpl @Inject constructor (
    private val soundSphereApi: SoundSphereApiService
) : SoundSphereApiRepository {
    override fun getTopChartSongs(): Flow<Resource<TopSongs>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getTopChartSongs().body()
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }
}