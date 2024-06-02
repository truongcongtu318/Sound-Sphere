package com.example.soundsphere.ui.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.FireStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val fireStoreRepository: FireStoreRepository
): ViewModel() {
    private val _libraryState = MutableStateFlow(SavedTrackState())
    val libraryState : StateFlow<SavedTrackState> = _libraryState

    init {
        viewModelScope.launch {
            getSavedTracks()
        }
    }

    private suspend fun getSavedTracks() {
        fireStoreRepository.getLikedTracks(
            onSuccess = {
                _libraryState.value = SavedTrackState(isSuccessful = it)
                Log.d("TAG", "getSavedTracks: $it")
            },
            onFailure = {
                _libraryState.value = SavedTrackState(isError = it.message)
            }
        )
    }
}