package com.example.soundsphere.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class SavedTrack(
    val trackId: String = "",
    val mailUser: String = "",
)

class FireStoreRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : FireStoreRepository {

    private val favourites = fireStore.collection("favourites")
    var savedTrack = SavedTrack()
    override suspend fun savedLikedTrack(
        savedTrack: SavedTrack,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

    }

    override suspend fun getLikedTracks(
        onSuccess: (List<SavedTrack>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        favourites.get()
            .addOnCompleteListener { result ->
                val savedTrack = result.result.mapNotNull { it.toObject<SavedTrack>() }
                onSuccess(savedTrack)
            }
            .addOnFailureListener {
                onFailure(it)
            }

    }

}