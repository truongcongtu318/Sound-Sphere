package com.example.soundsphere.ui.firestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.model.Track
import com.example.soundsphere.data.repository.FireStoreRepository
import com.example.soundsphere.data.repository.SavedTrack
import com.example.soundsphere.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class firestoreViewModel @Inject constructor(
    private val firestoreRepository: FireStoreRepository
) : ViewModel() {

    private val _savedTracks = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val savedTracks = _savedTracks.asStateFlow()

    private val _likedTracks = MutableStateFlow<Resource<List<Track>>>(Resource.Loading())
    val likedTracks  = _likedTracks.asStateFlow()

    fun savedLikedTrack(track: Track, email: String) {
        viewModelScope.launch {
            firestoreRepository.savedLikedTrack(track,email).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _savedTracks.value = Resource.Error(result.msg!!)
                    }

                    is Resource.Loading -> {
                        _savedTracks.value = Resource.Loading()
                    }

                    is Resource.Success -> {
                        _savedTracks.value = Resource.Success(result.data)
                    }
                }
            }
        }
    }

    fun getLikedTracks(email: String) {
        viewModelScope.launch {
            firestoreRepository.getLikedTracks(email).collect { result ->
                when(result) {
                    is Resource.Error -> {
                        _likedTracks.value = Resource.Error(result.msg!!)
                    }
                    is Resource.Loading -> {
                        _likedTracks.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _likedTracks.value = Resource.Success(result.data)
                    }
                }
            }
        }
    }


}