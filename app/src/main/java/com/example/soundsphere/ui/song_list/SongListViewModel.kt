package com.example.soundsphere.ui.song_list

import androidx.lifecycle.ViewModel
import com.example.soundsphere.data.repository.SoundSphereApiRepository
import com.example.soundsphere.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val repository: SoundSphereApiRepository
) : ViewModel() {
    private val _trackByIdState = MutableStateFlow(TrackByIdState())
    val trackByIdState: StateFlow<TrackByIdState> = _trackByIdState

    private val _playlistState = MutableStateFlow(PlayListState())
    val playlistState: StateFlow<PlayListState> = _playlistState

    suspend fun getTrackById(id: String) {
        repository.getTrackByIdData(id).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _trackByIdState.value = TrackByIdState(isError = result.msg)
                }

                is Resource.Loading -> {
                    _trackByIdState.value = TrackByIdState(isLoading = true)
                }

                is Resource.Success -> {
                    _trackByIdState.value = TrackByIdState(isSuccessDataTrackState = result.data)
                }

                is Resource.Track -> {
                    _trackByIdState.value = TrackByIdState(track = result.track)
                }
            }
        }
    }

    suspend fun getPlayListByIdData(playlistId: String) {
        repository.getPlayListByIdData(playlistId).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _playlistState.value = PlayListState(isError = result.msg)
                }

                is Resource.Loading -> {
                    _playlistState.value = PlayListState(isLoading = true)

                }
                is Resource.Success -> {
                    _playlistState.value = PlayListState(isSuccessDataPlayList = result.data)
                }

                is Resource.Track -> TODO()
            }
        }
    }
}