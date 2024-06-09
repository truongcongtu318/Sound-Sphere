package com.example.soundsphere.ui.auth

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.AuthRepository
import com.example.soundsphere.player.service.JetAudioServiceHandler
import com.example.soundsphere.player.service.JetAudioState
import com.example.soundsphere.ui.login.FacebookLoginState
import com.example.soundsphere.ui.login.GoogleLoginState
import com.example.soundsphere.ui.login.LoginState
import com.example.soundsphere.ui.play.UIEvents
import com.example.soundsphere.ui.register.RegisterState
import com.example.soundsphere.ui.register.SendMailState
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth,
    private val serviceHandler: JetAudioServiceHandler,
) : ViewModel() {

    private val _googleState = mutableStateOf(GoogleLoginState())
    val googleState: State<GoogleLoginState> = _googleState

    private val _facebookState = MutableStateFlow(FacebookLoginState())
    val facebookState: StateFlow<FacebookLoginState> = _facebookState.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registerState = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> = _registerState

    private val _sendVerificationState = mutableStateOf<Resource<Boolean>>(Resource.Loading())
    val sendVerificationState: State<Resource<Boolean>> = _sendVerificationState

    private val _isUserAuthenticated = MutableStateFlow(false)
    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated

    private val _isEmailVerified = MutableStateFlow(false)
    val isEmailVerified: StateFlow<Boolean> = _isEmailVerified

    init {
        checkUserAuthentication()
    }

    fun checkUserAuthentication() {
        viewModelScope.launch {
            authRepository.isUserAuthenticated().collect {
                _isUserAuthenticated.value = it
            }
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            authRepository.checkEmailVerified().collect {
                _isEmailVerified.value = it
            }
        }
    }

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


    fun registerWithEmailPassword(email: String, password: String) = viewModelScope.launch {
        authRepository.registerWithEmailPassword(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _registerState.value = RegisterState(error = result.msg.toString())
                }

                is Resource.Loading -> {
                    _registerState.value = RegisterState(loading = true)
                }

                is Resource.Success -> {
                    _registerState.value = RegisterState(success = true)
                    sendEmailVerification()
                }
            }
        }
    }

    fun sendEmailVerification() = viewModelScope.launch {
        authRepository.sendEmailVerification().collect { result ->
            _sendVerificationState.value = result
        }
    }

    fun logout() {
        auth.signOut()
//        serviceHandler.clearExoPlayer()
        _isUserAuthenticated.value = false
        _isEmailVerified.value = false
        _loginState.value = LoginState()
        _googleState.value = GoogleLoginState()
        _facebookState.value = FacebookLoginState()
    }
}