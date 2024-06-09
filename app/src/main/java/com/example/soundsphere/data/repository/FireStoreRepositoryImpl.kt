package com.example.soundsphere.data.repository

import android.util.Log
import com.example.soundsphere.data.model.Track
import com.example.soundsphere.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class SavedTrack(
    val track: Track? = null,
    val mailUser: String = "",
)

class FireStoreRepositoryImpl @Inject constructor(
    fireStore: FirebaseFirestore
) : FireStoreRepository {

    private val favourites = fireStore.collection("favourites")
    private var savedTrack = SavedTrack()
    override fun savedLikedTrack(track: Track,email: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val userTracksCollection = favourites.document(email).collection("tracks")
            val trackId = track.id
            if (trackId != null) {
                val trackDocument = userTracksCollection.document(trackId).get().await()
                if (trackDocument.exists()) {
                    userTracksCollection.document(trackId).delete().await()
                    emit(Resource.Success(false))
                } else {
                    userTracksCollection.document(trackId).set(track).await()
                    emit(Resource.Success(true))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getLikedTracks(email: String): Flow<Resource<List<Track>>> {

        return flow {
            emit(Resource.Loading())
            try {
                val userTracksCollection = favourites.document(email).collection("tracks")
                val trackDocuments = userTracksCollection.get().await()
                val tracks = trackDocuments.toObjects(Track::class.java).toList()
                emit(Resource.Success(tracks))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }


}