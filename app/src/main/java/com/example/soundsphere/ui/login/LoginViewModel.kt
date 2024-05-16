package com.example.soundsphere.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.AuthRepository
import com.example.soundsphere.utils.Resource
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _googleState = mutableStateOf(GoogleLoginState())
    val googleState : State<GoogleLoginState> = _googleState

    private val _facebookState = mutableStateOf(FacebookLoginState())
    val facebookState : State<FacebookLoginState> = _facebookState

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn



    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            auth.currentUser?.let {
                _isLoggedIn.value = true
            } ?: run {
                _isLoggedIn.value = false
            }
        }
    }
    fun loginWithGoogle(credential: AuthCredential) = viewModelScope.launch {
        authRepository.loginWithGoogle(credential).collect{result ->
            when(result){
                is Resource.Error -> {
                    _googleState.value = GoogleLoginState(error = result.msg.toString())
                }
                is Resource.Loading -> {
                    _googleState.value = GoogleLoginState(loading = true)
                }
                is Resource.Success -> {
                    _googleState.value = GoogleLoginState(success = result.data)
                }
            }
        }
    }

    fun loginWithFacebook(token: AccessToken) = viewModelScope.launch {
        authRepository.loginWithFacebook(token).collect{result ->
            when(result){
                is Resource.Error -> {
                    _facebookState.value = FacebookLoginState(error = result.msg.toString())
                }
                is Resource.Loading -> {
                    _facebookState.value = FacebookLoginState(loading = true)
                }
                is Resource.Success -> {
                    _facebookState.value = FacebookLoginState(success = result.data)
                }
            }
        }
    }


}