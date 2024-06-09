package com.example.soundsphere.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.AuthRepository
import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState : StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        viewModelScope.launch {
            getProfile()
        }
    }


    private fun getProfile() = viewModelScope.launch {
        authRepository.getProfile().collect{result->
            when(result){
                is Resource.Error -> {
                    _profileState.value = ProfileState(
                        error = result.msg ?: "An unknown error occurred"
                    )
                }
                is Resource.Loading -> {
                    _profileState.value = ProfileState(loading = true)
                }
                is Resource.Success -> {
                    _profileState.value = ProfileState(success = result.data)
                }


            }
        }
    }

}