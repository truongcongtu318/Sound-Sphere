package com.example.soundsphere.data.repository

import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getProfile() : Flow<Resource<FirebaseUser>>
    fun loginWithGoogle(credential: AuthCredential) : Flow<Resource<AuthResult>>

}