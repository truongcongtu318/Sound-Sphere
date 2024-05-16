package com.example.soundsphere.ui.home
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.SoundSphereApiRepository
import com.example.soundsphere.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SoundSphereApiRepository
) : ViewModel() {
    private val _newReleaseState = MutableStateFlow(NewReleaseState())
    val newReleaseState: StateFlow<NewReleaseState> = _newReleaseState

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState

    private val _recommendationState = MutableStateFlow(RecommendationState())
    val recommendationState: StateFlow<RecommendationState> = _recommendationState

    private val _playlistState = MutableStateFlow(PlayListState())
    val playlistState: StateFlow<PlayListState> = _playlistState

    private val _albumsState = MutableStateFlow(AlbumsState())
    val albumsState: StateFlow<AlbumsState> = _albumsState

    init {
        viewModelScope.launch {
            getRecommendationData()
            getNewReleasesData()
            getAlbumsData()
            getSearchData("Sơn Tùng MTP")
        }
    }

    private suspend fun getSearchData(query: String) {
        repository.getSearchData(query).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _searchState.value = SearchState(isError = result.msg)
                }

                is Resource.Loading -> {
                    _searchState.value = SearchState(isLoading = true)
                }

                is Resource.Success -> {
                    _searchState.value = SearchState(isSuccessDataPlayList = result.data)
                }
            }

        }
    }


    private suspend fun getRecommendationData() {
        repository.getRecommendationData().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _recommendationState.value = RecommendationState(isError = result.msg)
                    Log.d("Recommendation", "getRecommendationData: ${result.msg}")
                }

                is Resource.Loading -> {
                    _recommendationState.value = RecommendationState(isLoading = true)
                }

                is Resource.Success -> {
                    _recommendationState.value =
                        RecommendationState(isSuccessDataRecommendation = result.data)
                }
            }
        }
    }


    private suspend fun getNewReleasesData() {
        repository.getNewReleaseData().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _newReleaseState.value = NewReleaseState(isError = result.msg)
                }

                is Resource.Loading -> {
                    _newReleaseState.value = NewReleaseState(isLoading = true)

                }

                is Resource.Success -> {
                    _newReleaseState.value =
                        NewReleaseState(isSuccessDataNewRelease = result.data)
                }
            }
        }
    }

    private suspend fun getPlayListData(playlistId: String) {
        repository.getPlayListData(playlistId).collect { result ->
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
            }
        }
    }

    private suspend fun getAlbumsData() {
        repository.getAlbumsData().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _albumsState.value = AlbumsState(isError = result.msg)
                }

                is Resource.Loading -> {
                    _albumsState.value = AlbumsState(isLoading = true)
                }

                is Resource.Success -> {
                    _albumsState.value = AlbumsState(isSuccessDataAlbums = result.data)
                }
            }
        }
    }


}
