package com.example.soundsphere.ui.song_list

import androidx.lifecycle.ViewModel
import com.example.soundsphere.data.dtodeezer.playlisttracks.PlayListTrackDto
import com.example.soundsphere.data.repository.DeezerRepository
import com.example.soundsphere.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val repository: DeezerRepository
) : ViewModel() {

    private val _trackState = MutableStateFlow(TrackState())
    val trackState: StateFlow<TrackState> = _trackState

    private val _playListState = MutableStateFlow(PlayListState())
    val playListState: StateFlow<PlayListState> = _playListState

    private val _playListTrackState = MutableStateFlow(PlayListTrackState())
    val playListTrackState: StateFlow<PlayListTrackState> = _playListTrackState

    private val _artistTopState = MutableStateFlow(ArtistTopState())
    val artistTopState: StateFlow<ArtistTopState> = _artistTopState

    suspend fun getPlayListTracks(id: String) {
        repository.getPlayListTracks(id).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _playListTrackState.value = PlayListTrackState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _playListTrackState.value = PlayListTrackState(isLoading = true)
                }

                is Resource.Success -> {
                    _playListTrackState.value = PlayListTrackState(isSuccessful = result.data)
                }

            }

        }

    }

    suspend fun getTrack(id: String) {
        repository.getTracks(id).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _trackState.value = TrackState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _trackState.value = TrackState(isLoading = true)
                }

                is Resource.Success -> {
                    _trackState.value = TrackState(isSuccessful = result.data)
                }

            }
        }
    }

    suspend fun getPlayList(id: String) {
        repository.getPlayList(id).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _playListState.value = PlayListState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _playListState.value = PlayListState(isLoading = true)
                }

                is Resource.Success -> {
                    _playListState.value = PlayListState(isSuccessful = result.data)

                }

            }

        }
    }

    suspend fun getArtistTop(url: String) {
        repository.getArtistTop(url).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _artistTopState.value = ArtistTopState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _artistTopState.value = ArtistTopState(isLoading = true)
                }
                is Resource.Success -> {
                    _artistTopState.value = ArtistTopState(isSuccessful = result.data)
                }
            }
        }
    }
}