package com.example.soundsphere.data.repository

import com.example.soundsphere.utils.Resource
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginWithGoogle(credential: AuthCredential) : Flow<Resource<AuthResult>>
    fun loginWithFacebook(token: AccessToken) : Flow<Resource<AuthResult>>
}