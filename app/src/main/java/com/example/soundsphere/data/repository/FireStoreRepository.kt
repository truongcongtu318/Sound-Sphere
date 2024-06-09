package com.example.soundsphere.data.repository

import com.example.soundsphere.data.model.Track
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow


interface FireStoreRepository {
    fun savedLikedTrack(track: Track,email: String) : Flow<Resource<Boolean>>
    fun getLikedTracks(email: String) : Flow<Resource<List<Track>>>

}