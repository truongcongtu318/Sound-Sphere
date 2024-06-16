package com.example.soundsphere.ui.firestore

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.soundsphere.data.model.Album
import com.example.soundsphere.data.model.Artist
import com.example.soundsphere.data.model.Playlist
import com.example.soundsphere.data.model.Song
import com.example.soundsphere.data.repository.FireStoreRepository
import com.example.soundsphere.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FireStoreViewModel @Inject constructor(
    private val firestoreRepository: FireStoreRepository
) : ViewModel() {

    private val _savedTracks = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val savedTracks = _savedTracks.asStateFlow()

    private val _savedAlbums = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val savedAlbums = _savedAlbums.asStateFlow()

    private val _savedPlaylist = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val savedPlaylist = _savedPlaylist.asStateFlow()

    private val _savedArtist = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val savedArtist = _savedArtist.asStateFlow()

    private val _likedTracks = MutableStateFlow<Resource<List<Song>>>(Resource.Loading())
    val likedTracks  = _likedTracks.asStateFlow()

    private val _likedAlbums = MutableStateFlow<Resource<List<Album>>>(Resource.Loading())
    val likedAlbums  = _likedAlbums.asStateFlow()


    private val _allSongs = MutableStateFlow<Resource<List<Song>>>(Resource.Loading())
    val allSongs  = _allSongs.asStateFlow()

    private val _songsByAlbumId = MutableStateFlow<Resource<List<Song>>>(Resource.Loading())
    val songsByAlbumId  = _songsByAlbumId.asStateFlow()

    private val _allAlbums = MutableStateFlow<Resource<List<Album>>>(Resource.Loading())
    val allAlbums = _allAlbums.asStateFlow()

    private val _albumById = MutableStateFlow<Resource<Album>>(Resource.Loading())
    val albumById = _albumById.asStateFlow()

    private val _allArtists = MutableStateFlow<Resource<List<Artist>>>(Resource.Loading())
    val allArtists = _allArtists.asStateFlow()

    private val _songsByArtistId = MutableStateFlow<Resource<List<Song>>>(Resource.Loading())
    val songsByArtistId = _songsByArtistId.asStateFlow()

    private val _songsByName = MutableStateFlow<Resource<List<Song>>>(Resource.Loading())
    val songsByName = _songsByName.asStateFlow()

    private val _albumsByName = MutableStateFlow<Resource<List<Album>>>(Resource.Loading())
    val albumsByName = _albumsByName.asStateFlow()

    private val _artistsByName = MutableStateFlow<Resource<List<Artist>>>(Resource.Loading())
    val artistsByName = _artistsByName.asStateFlow()

    private val _songsByGenreId = MutableStateFlow<Resource<List<Song>>>(Resource.Loading())
    val songsByGenreId = _songsByGenreId.asStateFlow()

    private val _createPlaylist = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val createPlaylist = _createPlaylist.asStateFlow()

    private val _addSongToPlaylist = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val addSongToPlaylist = _addSongToPlaylist.asStateFlow()

    private val _allPlaylists = MutableStateFlow<Resource<List<Playlist>>>(Resource.Loading())
    val allPlaylists = _allPlaylists.asStateFlow()

    private val _playlistByName = MutableStateFlow<Resource<Playlist>>(Resource.Loading())
    val playlistByName = _playlistByName.asStateFlow()


    fun savedLikedSong(song: Song, email: String) {
        viewModelScope.launch {
            firestoreRepository.savedLikedSong(song,email).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _savedTracks.value = Resource.Error(result.msg!!)
                    }

                    is Resource.Loading -> {
                        _savedTracks.value = Resource.Loading()
                    }

                    is Resource.Success -> {
                        _savedTracks.value = Resource.Success(result.data)
                    }
                }
            }
        }
    }

    fun savedLikedAlbum(album: Album, email: String) {
        viewModelScope.launch {
            firestoreRepository.savedLikedAlbum(album,email).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _savedAlbums.value = Resource.Error(result.msg!!)
                    }
                    is Resource.Loading -> {
                        _savedAlbums.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _savedAlbums.value = Resource.Success(result.data)
                    }
                }
            }
        }
    }

    fun getLikedSongs(email: String) {
        viewModelScope.launch {
            firestoreRepository.getLikedSongs(email).collect { result ->
                when(result) {
                    is Resource.Error -> {
                        _likedTracks.value = Resource.Error(result.msg!!)
                    }
                    is Resource.Loading -> {
                        _likedTracks.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _likedTracks.value = Resource.Success(result.data)
                    }
                }
            }
        }
    }

    fun getLikedAlbums(email: String) {
        viewModelScope.launch {
            firestoreRepository.getLikedAlbums(email).collect { result ->
                when(result) {
                    is Resource.Error -> {
                        _likedAlbums.value = Resource.Error(result.msg!!)
                    }
                    is Resource.Loading -> {
                        _likedAlbums.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _likedAlbums.value = Resource.Success(result.data)
                    }
                }
            }
        }
    }
    @OptIn(UnstableApi::class)
    fun getAllSongs() {
        viewModelScope.launch {
            firestoreRepository.getAllSongs().collect { result ->
                when (result) {
                    is Resource.Error -> _allSongs.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _allSongs.value = Resource.Loading()
                    is Resource.Success -> _allSongs.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getAllAlbums() {
        viewModelScope.launch {
            firestoreRepository.getAllAlbums().collect { result ->
                when (result) {
                    is Resource.Error -> _allAlbums.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _allAlbums.value = Resource.Loading()
                    is Resource.Success -> _allAlbums.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getAlbumById(id: String) {
        viewModelScope.launch {
            firestoreRepository.getAlbumById(id).collect { result ->
                when (result) {
                    is Resource.Error -> _albumById.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _albumById.value = Resource.Loading()
                    is Resource.Success -> _albumById.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getSongsByAlbumId(id: String) {
        viewModelScope.launch {
            firestoreRepository.getSongsByAlbumId(id).collect { result ->
                when (result) {
                    is Resource.Error -> _songsByAlbumId.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _songsByAlbumId.value = Resource.Loading()
                    is Resource.Success -> _songsByAlbumId.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getAllArtists() {
        viewModelScope.launch {
            firestoreRepository.getAllArtists().collect { result ->
                when (result) {
                    is Resource.Error -> _allArtists.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _allArtists.value = Resource.Loading()
                    is Resource.Success -> _allArtists.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getSongsByArtistId(id: String) {
        viewModelScope.launch {
            firestoreRepository.getSongsByArtistId(id).collect { result ->
                when (result) {
                    is Resource.Error -> _songsByArtistId.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _songsByArtistId.value = Resource.Loading()
                    is Resource.Success -> _songsByArtistId.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getSongsByName(name: String) {
        viewModelScope.launch {
            firestoreRepository.getSongsByName(name).collect { result ->
                when (result) {
                    is Resource.Error -> _songsByName.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _songsByName.value = Resource.Loading()
                    is Resource.Success -> _songsByName.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getAlbumsByName(name: String) {
        viewModelScope.launch {
            firestoreRepository.getAlbumsByName(name).collect { result ->
                when (result) {
                    is Resource.Error -> _albumsByName.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _albumsByName.value = Resource.Loading()
                    is Resource.Success -> _albumsByName.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getArtistsByName(name: String) {
        viewModelScope.launch {
            firestoreRepository.getArtistByName(name).collect { result ->
                when (result) {
                    is Resource.Error -> _artistsByName.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _artistsByName.value = Resource.Loading()
                    is Resource.Success -> _artistsByName.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getSongsByGenreId(id: String) {
        viewModelScope.launch {
            firestoreRepository.getSongsByGenreId(id).collect { result ->
                when (result) {
                    is Resource.Error -> _songsByGenreId.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _songsByGenreId.value = Resource.Loading()
                    is Resource.Success -> _songsByGenreId.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun createPlaylist(namePlaylist: String, email: String) {
        viewModelScope.launch {
            firestoreRepository.createPlaylist(namePlaylist, email).collect { result ->
                when (result) {
                    is Resource.Error -> _createPlaylist.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _createPlaylist.value = Resource.Loading()
                    is Resource.Success -> _createPlaylist.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun addSongToPlaylist(namePlayList: String, song: Song, email: String) {
        viewModelScope.launch {
            firestoreRepository.addSongToPlaylist(namePlayList, song, email).collect { result ->
                when (result) {
                    is Resource.Error -> _addSongToPlaylist.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _addSongToPlaylist.value = Resource.Loading()
                    is Resource.Success -> _addSongToPlaylist.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getAllPlaylists(email: String) {
        viewModelScope.launch {
            firestoreRepository.getAllPlaylists(email).collect { result ->
                when (result) {
                    is Resource.Error -> _allPlaylists.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _allPlaylists.value = Resource.Loading()
                    is Resource.Success -> _allPlaylists.value = Resource.Success(result.data)
                }
            }
        }
    }

    fun getPlayListByName(name: String, email: String) {
        viewModelScope.launch {
            firestoreRepository.getPlaylistByName(name, email).collect { result ->
                when (result) {
                    is Resource.Error -> _playlistByName.value = Resource.Error(result.msg!!)
                    is Resource.Loading -> _playlistByName.value = Resource.Loading()
                    is Resource.Success -> _playlistByName.value = Resource.Success(result.data)
                }
            }
        }
    }
}