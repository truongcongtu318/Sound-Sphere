package com.example.soundsphere.ui.login

import com.google.firebase.auth.AuthResult

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: AuthResult? = null,
    val isError: String? = ""
)