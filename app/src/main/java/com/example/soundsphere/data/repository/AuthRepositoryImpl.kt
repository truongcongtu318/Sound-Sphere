package com.example.soundsphere.data.repository

import android.util.Log
import com.example.soundsphere.utils.Resource
import com.facebook.AccessToken
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private var firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun getProfile(): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.currentUser
            result?.reload()?.await()
            emit(Resource.Success(result))
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

    override fun loginWithFacebook(token: AccessToken): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val credential = FacebookAuthProvider.getCredential(token.token)
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun registerWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                emit(Resource.Success(result))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun loginWithEmailPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun deleteAccount(): Flow<Resource<Void>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.currentUser!!.delete().await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun sendEmailVerification(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.sendEmailVerification().await()
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("No user is logged in"))
            }
        }.catch { e ->
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override fun checkEmailVerified(): Flow<Boolean> = flow {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            emit(user.isEmailVerified)
        } else {
            emit(false)
        }
    }.catch { e ->
        emit(false)
    }

    override fun isUserAuthenticated(): Flow<Boolean> = flow {
        val user = FirebaseAuth.getInstance().currentUser
        emit(user != null)
    }.catch { e ->
        emit(false)
    }
}