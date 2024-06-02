package com.example.soundsphere.di

import com.example.soundsphere.data.remote.DeezerApiService
import com.example.soundsphere.data.repository.AuthRepository
import com.example.soundsphere.data.repository.AuthRepositoryImpl
import com.example.soundsphere.data.repository.DeezerRepository
import com.example.soundsphere.data.repository.DeezerRepositoryImpl
import com.example.soundsphere.data.repository.FireStoreRepository
import com.example.soundsphere.data.repository.FireStoreRepositoryImpl
import com.example.soundsphere.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .build()
//    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideSoundSphereRepository(apiService: DeezerApiService): DeezerRepository {
        return DeezerRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideSoundSphereApiService(retrofit: Retrofit): DeezerApiService {
        return retrofit.create(DeezerApiService::class.java)
    }


    @Singleton
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesFireStore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFireStoreRepositoryImp(firebaseFireStore: FirebaseFirestore): FireStoreRepository {
        return FireStoreRepositoryImpl(firebaseFireStore)
    }




}

