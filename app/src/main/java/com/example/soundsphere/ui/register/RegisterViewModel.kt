package com.example.soundsphere.ui.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundsphere.data.repository.AuthRepository
import com.example.soundsphere.ui.login.FacebookLoginState
import com.example.soundsphere.ui.login.GoogleLoginState
import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _registerState = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> = _registerState

    private val _sendVerificationState = mutableStateOf(SendMailState())
    val sendVerificationState: State<SendMailState> = _sendVerificationState

    private val _isEmailVerified = MutableStateFlow(false)
    val isEmailVerified: StateFlow<Boolean> get() = _isEmailVerified

    private val _isUserAuthenticated = MutableStateFlow(false)
    val isUserAuthenticated: StateFlow<Boolean> get() = _isUserAuthenticated



    fun checkEmailVerified() {
        viewModelScope.launch {
            authRepository.checkEmailVerified().collect { isVerified ->
                _isEmailVerified.value = isVerified
            }
        }
    }

    private fun checkUserAuthentication() {
        viewModelScope.launch {
            authRepository.isUserAuthenticated().collect { isAuthenticated ->
                _isUserAuthenticated.value = isAuthenticated
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
                }
            }
        }
    }

    fun sendEmailVerification() = viewModelScope.launch {
        authRepository.sendEmailVerification().collect { result ->
            when (result) {
                is Resource.Error -> {
                    _sendVerificationState.value = SendMailState(error = result.msg.toString())
                }

                is Resource.Loading -> {
                    _sendVerificationState.value = SendMailState(loading = true)
                }

                is Resource.Success -> {
                    _sendVerificationState.value = SendMailState(success = true)
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _isEmailVerified.value = false
        _isUserAuthenticated.value = false
    }

}