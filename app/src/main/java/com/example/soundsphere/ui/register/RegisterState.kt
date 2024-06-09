package com.example.soundsphere.ui.register

data class RegisterState(
    val success: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)