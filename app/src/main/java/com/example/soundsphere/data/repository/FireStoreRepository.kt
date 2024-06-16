package com.example.soundsphere.data.repository

import com.example.soundsphere.data.model.Album
import com.example.soundsphere.data.model.Artist
import com.example.soundsphere.data.model.Playlist
import com.example.soundsphere.data.model.Song
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow


interface FireStoreRepository {
    fun savedLikedSong(song: Song, email: String) : Flow<Resource<Boolean>>
    fun savedLikedAlbum(album: Album, email: String) : Flow<Resource<Boolean>>
//    fun savedLikedPlayList(playListDto: PlayListDto, email: String) : Flow<Resource<Boolean>>
//    fun savedLikedArtist(trackList : String,email: String) : Flow<Resource<Boolean>>
    fun getLikedSongs(email: String) : Flow<Resource<List<Song>>>
    fun getLikedAlbums(email: String) : Flow<Resource<List<Album>>>
//    fun getLikedPlayList(email: String) : Flow<Resource<List<PlayListDto>>>
//    fun getLikedArtist(email: String) : Flow<Resource<List<Artist>>>

    fun getAllSongs() : Flow<Resource<List<Song>>>
    fun getSongsByName(name: String) : Flow<Resource<List<Song>>>
    fun getAllAlbums() : Flow<Resource<List<Album>>>
    fun getAlbumsByName(name: String) : Flow<Resource<List<Album>>>
    fun getAlbumById(idAlbum: String) : Flow<Resource<Album>>
    fun getSongsByAlbumId(id: String) : Flow<Resource<List<Song>>>
    fun getAllArtists() : Flow<Resource<List<Artist>>>
    fun getArtistByName(name: String) : Flow<Resource<List<Artist>>>
    fun getSongsByArtistId(id: String) : Flow<Resource<List<Song>>>
    fun getSongsByGenreId(id: String) : Flow<Resource<List<Song>>>
    fun createPlaylist(namePlaylist: String, email: String) : Flow<Resource<Boolean>>
    fun getAllPlaylists(email: String) : Flow<Resource<List<Playlist>>>
    fun getPlaylistByName(name: String,email: String) : Flow<Resource<Playlist>>
    fun deletePlaylist(idPlaylist: String, email: String) : Flow<Resource<Boolean>>
    fun addSongToPlaylist(namePlaylist: String, song: Song, email: String) : Flow<Resource<Boolean>>
}