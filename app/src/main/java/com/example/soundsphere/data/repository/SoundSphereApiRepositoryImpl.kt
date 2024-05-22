package com.example.soundsphere.data.repository

import android.util.Log
import com.example.soundsphere.data.dto.albums.Album
import com.example.soundsphere.data.dto.albums.DataAlbumsDto
import com.example.soundsphere.data.dto.albums.Tracks
import com.example.soundsphere.data.dto.browse_category.DataBrowseCategoryDto
import com.example.soundsphere.data.dto.feature_playlist.DataFeaturePlaylistDto
import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto
import com.example.soundsphere.data.dto.playlist.DataPlayListDto
import com.example.soundsphere.data.dto.recomendation.DataRecommendationDto
import com.example.soundsphere.data.dto.search.DataSearchDto
import com.example.soundsphere.data.dto.top_artists.DataTopArtistDto
import com.example.soundsphere.data.dto.track.DataTrackDto
import com.example.soundsphere.data.model.Track
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
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getPlayListByIdData(playlistId: String): Flow<Resource<DataPlayListDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getPlaylistById(playlistId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
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
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: SocketTimeoutException) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getAlbumsByIdData(id: String): Flow<Resource<Album>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getAlbumsById(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getTrackByAlbumIdData(id: String): Flow<Resource<Tracks>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getTrackByAlbumId(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getTrackByIdData(id: String): Flow<Resource<DataTrackDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getTracksById(id)
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        val track = Track(
                            id = data.id,
                            name = data.name,
                            artist = data.artists.firstOrNull()!!.name,
                            preview_url = data.preview_url,
                            duration = data.duration_ms,
                            title = data.name
                        )
                        emit(Resource.Track(track))
                    }
                    emit(Resource.Success(response.body()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }


    override fun getFeaturedPlayListData(): Flow<Resource<DataFeaturePlaylistDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getFeaturedPlaylists()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }

        }
    }

    override fun getTopArtistData(): Flow<Resource<DataTopArtistDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getTopArtists()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun getBrowseCategoryData(): Flow<Resource<DataBrowseCategoryDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = soundSphereApi.getCategoriesBrowse()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}
