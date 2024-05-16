package com.example.soundsphere.ui.login

import com.google.firebase.auth.AuthResult

data class FacebookLoginState (
    val success: AuthResult? = null,
    val loading: Boolean = false,
    val error: String = ""
)