package com.example.soundsphere.data.remote

import com.example.soundsphere.data.entities.Song
import com.example.soundsphere.utils.Constants1
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SoundSphereDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(Constants1.SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song>{
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception){
            emptyList()
        }
    }
}