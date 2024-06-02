package com.example.soundsphere.ui.search

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
class SearchViewModel @Inject constructor(
    private val repository: DeezerRepository
) : ViewModel() {

    private val _genresState = MutableStateFlow(GenresState())
    val genresState: StateFlow<GenresState> = _genresState

    private val _searchAlbumState = MutableStateFlow(SearchAlbumState())
    val searchAlbumState: StateFlow<SearchAlbumState> = _searchAlbumState

    private val _searchArtistState = MutableStateFlow(SearchArtistState())
    val searchArtistState: StateFlow<SearchArtistState> = _searchArtistState

    private val _searchTrackState = MutableStateFlow(SearchTrackState())
    val searchTrackState: StateFlow<SearchTrackState> = _searchTrackState

    private val _searchPlayListState = MutableStateFlow(SearchPlayListState())
    val searchPlayListState: StateFlow<SearchPlayListState> = _searchPlayListState


    init {
        viewModelScope.launch {
            getGenres()
        }
    }

    suspend fun getSearchAlbum(q: String) {
        repository.getSearchAlbum(q).collect{result ->
            when(result){
                is Resource.Error -> {
                    _searchAlbumState.value = SearchAlbumState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _searchAlbumState.value = SearchAlbumState(isLoading = true)
                }
                is Resource.Success -> {
                    _searchAlbumState.value = SearchAlbumState(isSuccessful = result.data)
                }
                is Resource.Track -> TODO()
            }
        }
    }


    suspend fun getSearchArtist(q: String) {
        repository.getSearchArtist(q).collect{result ->
            when(result){
                is Resource.Error -> {
                    _searchArtistState.value = SearchArtistState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _searchArtistState.value = SearchArtistState(isLoading = true)
                }
                is Resource.Success -> {
                    _searchArtistState.value = SearchArtistState(isSuccessful = result.data)
                }
                is Resource.Track -> TODO()
            }
        }
    }


    suspend fun getSearchTrack(q: String) {
        repository.getSearchTrack(q).collect{result ->
            when(result){
                is Resource.Error -> {
                    _searchTrackState.value = SearchTrackState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _searchTrackState.value = SearchTrackState(isLoading = true)
                }
                is Resource.Success -> {
                    _searchTrackState.value = SearchTrackState(isSuccessful = result.data)
                }
                is Resource.Track -> TODO()
            }
        }
    }


    suspend fun getSearchPlayList(q: String) {
        repository.getSearchPlayList(q).collect{result ->
            when(result){
                is Resource.Error -> {
                    _searchPlayListState.value = SearchPlayListState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _searchPlayListState.value = SearchPlayListState(isLoading = true)
                }
                is Resource.Success -> {
                    _searchPlayListState.value = SearchPlayListState(isSuccessful = result.data)
                }
                is Resource.Track -> TODO()
            }
        }
    }

    private suspend fun getGenres() {
        repository.getGenres().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _genresState.value =
                        GenresState(isError = result.msg ?: "An unexpected error occured")
                }

                is Resource.Loading -> {
                    _genresState.value = GenresState(isLoading = true)
                }

                is Resource.Success -> {
                    _genresState.value = GenresState(isSuccessful = result.data)
                }

                is Resource.Track -> TODO()
            }

        }
    }
}
