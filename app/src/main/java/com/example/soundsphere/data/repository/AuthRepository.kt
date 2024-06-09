package com.example.soundsphere.data.repository

import com.example.soundsphere.utils.Resource
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getProfile() : Flow<Resource<FirebaseUser>>
    fun loginWithGoogle(credential: AuthCredential) : Flow<Resource<AuthResult>>
    fun loginWithFacebook(token : AccessToken) : Flow<Resource<AuthResult>>
    fun registerWithEmailPassword(email: String, password: String) : Flow<Resource<AuthResult>>
    fun loginWithEmailPassword(email: String, password: String) : Flow<Resource<AuthResult>>
    fun deleteAccount() : Flow<Resource<Void>>
    fun sendEmailVerification() : Flow<Resource<Boolean>>
    fun checkEmailVerified(): Flow<Boolean>
    fun isUserAuthenticated(): Flow<Boolean>
}