package com.example.soundsphere.di

import com.example.soundsphere.data.remote.SoundSphereApiService
import com.example.soundsphere.data.repository.SoundSphereApiRepository
import com.example.soundsphere.data.repository.SoundSphereApiRepositoryImpl
import com.example.soundsphere.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

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
    fun provideSoundSphereRepository(apiService: SoundSphereApiService): SoundSphereApiRepository {
        return SoundSphereApiRepositoryImpl(apiService)
    }

    @Provides
    fun provideSoundSphereApiService(retrofit: Retrofit): SoundSphereApiService {
        return retrofit.create(SoundSphereApiService::class.java)
    }
}