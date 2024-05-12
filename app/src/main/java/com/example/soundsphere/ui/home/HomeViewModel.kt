package com.example.soundsphere.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.model.TopSongs
import com.example.soundsphere.data.repository.SoundSphereApiRepository
import com.example.soundsphere.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SoundSphereApiRepository
) : ViewModel() {
    private val _homeState = mutableStateOf(HomeState())
    val homeState: State<HomeState> = _homeState

    fun fetchTopChartSongs() = viewModelScope.launch {
        repository.getTopChartSongs().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _homeState.value = HomeState(isError = result.msg ?: "An error occurred")
                }
                is Resource.Loading -> {
                    _homeState.value = HomeState(isLoading = true)
                }
                is Resource.Success -> {
                    _homeState.value = HomeState(isSuccess = result.data.toString())
                }
            }
        }
    }
}