package com.example.soundsphere.ui.search

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
class SearchViewModel @Inject constructor(
    private val repository: SoundSphereApiRepository
) : ViewModel() {

    private val _topArtistState = MutableStateFlow(TopArtistState())
    val topArtistState: StateFlow<TopArtistState> = _topArtistState
    private val _browseCategoryState = MutableStateFlow(BrowseCategoryState())
    val browseCategoryState: StateFlow<BrowseCategoryState> = _browseCategoryState

    init {
        if (_topArtistState.value.isSuccessDataTopArtist == null) {
            viewModelScope.launch {
                getTopArtistData()
            }
        }
        if (_browseCategoryState.value.isSuccessDataBrowseCategory == null) {
            viewModelScope.launch {
                getBrowseCategoryData()
            }
        }

    }

    private suspend fun getTopArtistData() {
        repository.getTopArtistData().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _topArtistState.value = TopArtistState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _topArtistState.value = TopArtistState(isLoading = true)
                }

                is Resource.Success -> {
                    _topArtistState.value = TopArtistState(isSuccessDataTopArtist = result.data)
                }

                is Resource.Track -> TODO()
            }
        }

    }

    private suspend fun getBrowseCategoryData() {
        repository.getBrowseCategoryData().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _browseCategoryState.value = BrowseCategoryState(
                        isError = result.msg ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _browseCategoryState.value = BrowseCategoryState(isLoading = true)
                }

                is Resource.Success -> {
                    _browseCategoryState.value =
                        BrowseCategoryState(isSuccessDataBrowseCategory = result.data)
                }

                is Resource.Track -> TODO()
            }
        }
    }
}