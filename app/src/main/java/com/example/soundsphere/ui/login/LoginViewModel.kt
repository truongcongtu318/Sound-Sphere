package com.example.soundsphere.ui.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.AuthRepository
import com.example.soundsphere.utils.Resource
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _googleState = mutableStateOf(GoogleLoginState())
    val googleState: State<GoogleLoginState> = _googleState

    private val _facebookState = mutableStateOf(FacebookLoginState())
    val facebookState: State<FacebookLoginState> = _facebookState

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()


    fun loginWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        authRepository.loginWithEmailPassword(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _loginState.value = LoginState(isError = result.msg.toString())
                }

                is Resource.Loading -> {
                    _loginState.value = LoginState(isLoading = true)
                }

                is Resource.Success -> {
                    _loginState.value = LoginState(isSuccess = result.data)
                }
            }
        }
    }

    fun loginWithGoogle(credential: AuthCredential) = viewModelScope.launch {
        authRepository.loginWithGoogle(credential).collect { result ->
            when (result) {
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

    @SuppressLint("LogNotTimber")
    fun loginWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            authRepository.loginWithFacebook(token).collect { result ->
                when (result) {
                    is Resource.Error -> _facebookState.value =
                        FacebookLoginState(error = result.msg.toString())

                    is Resource.Loading -> _facebookState.value =
                        FacebookLoginState(loading = true)

                    is Resource.Success -> _facebookState.value =
                        FacebookLoginState(success = result.data)
                }
            }
        }
    }


}