package com.example.soundsphere.data.repository

import android.annotation.SuppressLint
import com.example.soundsphere.data.dtodeezer.album.AlbumDto
import com.example.soundsphere.data.dtodeezer.albumtracks.AlbumTrackDto
import com.example.soundsphere.data.dtodeezer.artist_top.ArtistTopDto
import com.example.soundsphere.data.dtodeezer.chart.ChartDto
import com.example.soundsphere.data.dtodeezer.genre.GenreDto
import com.example.soundsphere.data.dtodeezer.playlist.PlayListDto
import com.example.soundsphere.data.dtodeezer.playlisttracks.PlayListTrackDto
import com.example.soundsphere.data.dtodeezer.search.searchAlbum.SearchAlbumDto
import com.example.soundsphere.data.dtodeezer.search.searchArtist.SearchArtistDto
import com.example.soundsphere.data.dtodeezer.search.searchPlayList.SearchPlayListDto
import com.example.soundsphere.data.dtodeezer.search.searchTrack.SearchTrackDto
import com.example.soundsphere.data.dtodeezer.track.TrackDto
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
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getAlbumTracks(url: String): Flow<Resource<AlbumTrackDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getAlbumTracks(url)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    @SuppressLint("LogNotTimber")
    override fun getPlayListTracks(url: String): Flow<Resource<PlayListTrackDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getPlayListTracks(url)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getAlbum(id: String): Flow<Resource<AlbumDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getAlbum(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getTracks(id: String): Flow<Resource<TrackDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getTrack(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getPlayList(id: String): Flow<Resource<PlayListDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getPlayList(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getGenres(): Flow<Resource<GenreDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getGenres()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getArtistTop(url: String): Flow<Resource<ArtistTopDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getArtistTop(url)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getSearchAlbum(q: String): Flow<Resource<SearchAlbumDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getSearchAlbum(q)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getSearchTrack(q: String): Flow<Resource<SearchTrackDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getSearchTrack(q)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getSearchPlayList(q: String): Flow<Resource<SearchPlayListDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getSearchPlayList(q)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

    override fun getSearchArtist(q: String): Flow<Resource<SearchArtistDto>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = deezerApiService.getSearchArtist(q)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(it))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }
}
