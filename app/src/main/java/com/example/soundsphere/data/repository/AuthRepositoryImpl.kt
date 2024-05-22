package com.example.soundsphere.data.repository

import com.example.soundsphere.utils.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private var firebaseAuth: FirebaseAuth
) : AuthRepository{
    override fun getProfile(): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.currentUser
            emit(Resource.Success(result!!))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun loginWithGoogle(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Success(result))

        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }



}