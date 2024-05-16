package com.example.soundsphere.data.repository

import android.util.Log
import com.example.soundsphere.data.dto.albums.DataAlbumsDto
import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto
import com.example.soundsphere.data.dto.playlist.DataPlayListDto
import com.example.soundsphere.data.dto.recomendation.DataRecommendationDto
import com.example.soundsphere.data.dto.search.DataSearchDto
import com.example.soundsphere.data.remote.SoundSphereApiService
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import javax.inject.Inject

class SoundSphereApiRepositoryImpl @Inject constructor(
    private var soundSphereApi: SoundSphereApiService
) : SoundSphereApiRepository {

    override fun getSearchData(query: String): Flow<Resource<DataSearchDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getSearchData(query)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("Response", it.toString())
                        emit(Resource.Success(it))
                    }
                }
                emit(Resource.Success(response.body()!!))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getRecommendationData(): Flow<Resource<DataRecommendationDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getRecommendations()
                if (response.isSuccessful) {
                    emit(Resource.Success(response.body()!!))
                    response.body()?.let {
                        Log.d("Response", it.toString())
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getPlayListData(playlistId: String): Flow<Resource<DataPlayListDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getPlaylist(playlistId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("Response", it.toString())
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getNewReleaseData(): Flow<Resource<DataNewReleaseDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getNewReleases()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("Response", it.toString())
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: SocketTimeoutException) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getAlbumsData(): Flow<Resource<DataAlbumsDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getAlbums()
                if (response.isSuccessful){
                    response.body()?.let {
                        Log.d("Response", it.toString())
                        emit(Resource.Success(it))
                    }
                }
            }catch (e : SocketTimeoutException){
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}
