package com.example.soundsphere.data.repository

import com.example.soundsphere.data.model.Artist
import com.example.soundsphere.data.model.Album
import com.example.soundsphere.data.model.Playlist
import com.example.soundsphere.data.model.Song
import com.example.soundsphere.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FireStoreRepositoryImpl @Inject constructor(
    fireStore: FirebaseFirestore
) : FireStoreRepository {

    private val favourites = fireStore.collection("favourites")
    private val songs = fireStore.collection("songs")
    private val albums = fireStore.collection("albums")
    private val artists = fireStore.collection("artists")
    private val playlists = fireStore.collection("playlists")

    override fun savedLikedSong(song: Song, email: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val userSongsCollection = favourites.document(email).collection("songs")
            val nameSong = song.title
            if (nameSong != "") {
                val songDocument = userSongsCollection.document(nameSong).get().await()
                if (songDocument.exists()) {
                    userSongsCollection.document(nameSong).delete().await()
                    emit(Resource.Success(false))
                } else {
                    userSongsCollection.document(nameSong).set(song).await()
                    emit(Resource.Success(true))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
//
    override fun savedLikedAlbum(album: Album, email: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val userAlbumsCollection = favourites.document(email).collection("albums")
            val albumId = album.id
            val albumDocument = userAlbumsCollection.document(albumId).get().await()
            if (albumDocument.exists()) {
                userAlbumsCollection.document(albumId).delete().await()
                emit(Resource.Success(false))
            } else {
                userAlbumsCollection.document(albumId).set(album).await()
                emit(Resource.Success(true))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getLikedSongs(email: String): Flow<Resource<List<Song>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val userTracksCollection = favourites.document(email).collection("songs")
                val trackDocuments = userTracksCollection.get().await()
                val tracks = trackDocuments.toObjects(Song::class.java).toList()
                emit(Resource.Success(tracks))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }
//
    override fun getLikedAlbums(email: String): Flow<Resource<List<Album>>> {

        return flow {
            emit(Resource.Loading())
            try {
                val userAlbumsCollection = favourites.document(email).collection("albums")
                val albumDocuments = userAlbumsCollection.get().await()
                val albums = albumDocuments.toObjects(Album::class.java).toList()
                emit(Resource.Success(albums))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }


    override fun getAllSongs(): Flow<Resource<List<Song>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val songDocuments = songs.get().await()
                val songs = songDocuments.toObjects(Song::class.java).toList()
                emit(Resource.Success(songs))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getSongsByName(name: String): Flow<Resource<List<Song>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val songDocument = songs.whereEqualTo("title", name).get().await()
                val songs = songDocument.toObjects<Song>().toList()
                emit(Resource.Success(songs))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getAllAlbums(): Flow<Resource<List<Album>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val albumDocuments = albums.get().await()
                val albums = albumDocuments.toObjects(com.example.soundsphere.data.model.Album::class.java).toList()
                emit(Resource.Success(albums))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getAlbumsByName(name: String): Flow<Resource<List<Album>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val albumDocument = albums.whereEqualTo("title", name).get().await()
                val albums = albumDocument.toObjects(Album::class.java).toList()
                emit(Resource.Success(albums))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getAlbumById(idAlbum: String): Flow<Resource<Album>> {
        return flow {
            emit(Resource.Loading())
            try {
                val albumDocument = albums.document(idAlbum).get().await()
                val album = albumDocument.toObject(Album::class.java)
                emit(Resource.Success(album!!))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }


    override fun getSongsByAlbumId(id: String): Flow<Resource<List<Song>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val songDocuments = songs.whereEqualTo("albumId", id).get().await()
                val songs = songDocuments.toObjects(Song::class.java).toList()
                emit(Resource.Success(songs))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getAllArtists(): Flow<Resource<List<Artist>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val artistDocuments = artists.get().await()
                val artists = artistDocuments.toObjects(Artist::class.java).toList()
                emit(Resource.Success(artists))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getArtistByName(name: String): Flow<Resource<List<Artist>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val artistDocument = artists.whereEqualTo("name", name).get().await()
                val artists = artistDocument.toObjects(Artist::class.java).toList()
                emit(Resource.Success(artists))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getSongsByArtistId(id: String): Flow<Resource<List<Song>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val songDocuments = songs.whereEqualTo("artist.artistId", id).get().await()
                val songs = songDocuments.toObjects(Song::class.java).toList()
                emit(Resource.Success(songs))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getSongsByGenreId(id: String): Flow<Resource<List<Song>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val songDocuments = songs.whereEqualTo("genreId", id).get().await()
                val songs = songDocuments.toObjects(Song::class.java).toList()
                emit(Resource.Success(songs))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun createPlaylist(namePlaylist: String, email: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                val userPlaylistsCollection = playlists.document(email).collection("playlists")
                val playlistDocument = userPlaylistsCollection.document(namePlaylist).get().await()
                if (playlistDocument.exists()) {
                    emit(Resource.Error("Playlist already exists"))
                } else {
                    userPlaylistsCollection.document(namePlaylist).set(Playlist(namePlaylist, email,namePlaylist,
                        emptyList()
                    )).await()
                    emit(Resource.Success(true))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getAllPlaylists(email: String): Flow<Resource<List<Playlist>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val userPlaylistsCollection = playlists.document(email).collection("playlists")
                val playlistDocuments = userPlaylistsCollection.get().await()
                val playlists = playlistDocuments.toObjects(Playlist::class.java).toList()
                emit(Resource.Success(playlists))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun getPlaylistByName(name: String, email: String): Flow<Resource<Playlist>> {
        return flow {
            emit(Resource.Loading())
            try {
                val userPlaylistsCollection = playlists.document(email).collection("playlists")
                val playlistDocument = userPlaylistsCollection.document(name).get().await()
                if (playlistDocument.exists()) {
                    val playlist = playlistDocument.toObject(Playlist::class.java)
                    emit(Resource.Success(playlist!!))
                } else {
                    emit(Resource.Error("Playlist not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }

    override fun deletePlaylist(idPlaylist: String, email: String): Flow<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun addSongToPlaylist(
        namePlaylist: String,
        song: Song,
        email: String
    ): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                val userPlaylistsCollection = playlists.document(email).collection("playlists")
                val playlistDocument = userPlaylistsCollection.document(namePlaylist).get().await()
                if (playlistDocument.exists()) {
                    val playlist = playlistDocument.toObject(Playlist::class.java)
                    val songs = playlist!!.songs.toMutableList()
                    if (songs.contains(song)) {
                        emit(Resource.Error("Song already exists in the playlist"))
                    }else{
                        songs.add(song)
                        userPlaylistsCollection.document(namePlaylist).update("songs", songs).await()
                        emit(Resource.Success(true))
                    }

                } else {
                    emit(Resource.Error("Playlist not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }


}