package com.example.soundsphere.data.repository

interface FireStoreRepository {
    suspend fun savedLikedTrack(savedTrack: SavedTrack, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getLikedTracks(onSuccess: (List<SavedTrack>) -> Unit, onFailure: (Exception) -> Unit)

}