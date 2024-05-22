package com.example.soundsphere.ui.profile

import com.google.firebase.auth.FirebaseUser

data class ProfileState(
    val success: FirebaseUser? = null,
    val loading: Boolean = false,
    val error: String = ""
)