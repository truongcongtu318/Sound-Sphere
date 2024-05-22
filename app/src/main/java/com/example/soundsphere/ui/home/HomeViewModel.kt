package com.example.soundsphere.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.DeezerRepository
import com.example.soundsphere.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DeezerRepository
) : ViewModel() {
    private val _newReleaseState = MutableStateFlow(NewReleaseState())
    val newReleaseState: StateFlow<NewReleaseState> = _newReleaseState

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState

    private val _recommendationState = MutableStateFlow(RecommendationState())
    val recommendationState: StateFlow<RecommendationState> = _recommendationState

    private val _albumsState = MutableStateFlow(AlbumsState())
    val albumsState: StateFlow<AlbumsState> = _albumsState

    private val _playlistFeatureState = MutableStateFlow(FeaturePlayListState())
    val playlistFeatureState: StateFlow<FeaturePlayListState> = _playlistFeatureState

    private val _albumByIdState = MutableStateFlow(AlbumByIdState())
    val albumByIdState: StateFlow<AlbumByIdState> = _albumByIdState

    private val _trackByAlbumIdState = MutableStateFlow(TracksByAlbumIdState())
    val trackByAlbumIdState: StateFlow<TracksByAlbumIdState> = _trackByAlbumIdState

    private val _chartState = MutableStateFlow(ChartState())
    val chartState: StateFlow<ChartState> = _chartState

    init {
        if (_albumsState.value.isSuccessDataAlbums == null
            || _recommendationState.value.isSuccessDataRecommendation == null
            || _newReleaseState.value.isSuccessDataNewRelease == null
            || _playlistFeatureState.value.isSuccessDataPlayListFeature == null
        ) {
            viewModelScope.launch {
//                getRecommendationData()
//                getNewReleasesData()
//                getAlbumsData()
//                getFeaturePlayListData()
            }
        }
    }

    private suspend fun getCharts() {
        repository.getCharts().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _chartState.value = ChartState(isError = result.msg)
                }
                is Resource.Loading -> {
                    _chartState.value = ChartState(isLoading = true)
                }
                is Resource.Success -> {
                    _chartState.value = ChartState(isSuccessful = result.data)
                }
                is Resource.Track -> TODO()
            }
        }
    }
}

//    private suspend fun getSearchData(query: String) {
//        repository.getSearchData(query).collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _searchState.value = SearchState(isError = result.msg)
//                }
//
//                is Resource.Loading -> {
//                    _searchState.value = SearchState(isLoading = true)
//                }
//
//                is Resource.Success -> {
//                    _searchState.value = SearchState(isSuccessDataPlayList = result.data)
//                }
//
//                is Resource.Track -> TODO()
//            }
//
//        }
//    }
//
//
//    private suspend fun getRecommendationData() {
//        repository.getRecommendationData().collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _recommendationState.value = RecommendationState(isError = result.msg)
//                    Log.d("Recommendation", "getRecommendationData: ${result.msg}")
//                }
//
//                is Resource.Loading -> {
//                    _recommendationState.value = RecommendationState(isLoading = true)
//                }
//
//                is Resource.Success -> {
//                    _recommendationState.value =
//                        RecommendationState(isSuccessDataRecommendation = result.data)
//                }
//
//                is Resource.Track -> TODO()
//            }
//        }
//    }
//
//
//    private suspend fun getNewReleasesData() {
//        repository.getNewReleaseData().collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _newReleaseState.value = NewReleaseState(isError = result.msg)
//                }
//
//                is Resource.Loading -> {
//                    _newReleaseState.value = NewReleaseState(isLoading = true)
//
//                }
//
//                is Resource.Success -> {
//                    _newReleaseState.value =
//                        NewReleaseState(isSuccessDataNewRelease = result.data)
//                }
//
//                is Resource.Track -> TODO()
//            }
//        }
//    }
//
//    private suspend fun getFeaturePlayListData() {
//        repository.getFeaturedPlayListData().collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _playlistFeatureState.value = FeaturePlayListState(isError = result.msg)
//                }
//
//                is Resource.Loading -> {
//                    _playlistFeatureState.value = FeaturePlayListState(isLoading = true)
//
//                }
//
//                is Resource.Success -> {
//                    _playlistFeatureState.value =
//                        FeaturePlayListState(isSuccessDataPlayListFeature = result.data)
//                }
//
//                is Resource.Track -> TODO()
//            }
//        }
//    }
//
//    private suspend fun getAlbumsData() {
//        repository.getAlbumsData().collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _albumsState.value = AlbumsState(isError = result.msg)
//                }
//
//                is Resource.Loading -> {
//                    _albumsState.value = AlbumsState(isLoading = true)
//                }
//
//                is Resource.Success -> {
//                    _albumsState.value = AlbumsState(isSuccessDataAlbums = result.data)
//                }
//
//                is Resource.Track -> TODO()
//            }
//        }
//    }
//
//    suspend fun getAlbumsByIdData(id: String) {
//        repository.getAlbumsByIdData(id).collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _albumByIdState.value = AlbumByIdState(isError = result.msg)
//                }
//
//                is Resource.Loading -> {
//                    _albumByIdState.value = AlbumByIdState(isLoading = true)
//                }
//
//                is Resource.Success -> {
//                    _albumByIdState.value = AlbumByIdState(isSuccessDataAlbumById = result.data)
//                }
//
//                is Resource.Track -> TODO()
//            }
//        }
//    }
//
//    suspend fun getTrackByAlbumId(id: String) {
//        repository.getTrackByAlbumIdData(id).collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _trackByAlbumIdState.value = TracksByAlbumIdState(isError = result.msg)
//                }
//                is Resource.Loading -> {
//                    _trackByAlbumIdState.value = TracksByAlbumIdState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    _trackByAlbumIdState.value =
//                        TracksByAlbumIdState(isSuccessDataTrackByAlbumId = result.data)
//                }
//
//                is Resource.Track -> TODO()
//            }
//        }
//
//    }
//}

