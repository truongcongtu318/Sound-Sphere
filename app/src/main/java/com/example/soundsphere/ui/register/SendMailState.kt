package com.example.soundsphere.ui.register

import com.google.firebase.auth.AuthResult

data class SendMailState(
    val success: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)