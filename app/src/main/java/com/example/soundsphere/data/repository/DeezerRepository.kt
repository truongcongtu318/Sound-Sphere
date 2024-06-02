package com.example.soundsphere.data.repository

import com.example.soundsphere.data.dtodeezer.album.AlbumDto
import com.example.soundsphere.data.dtodeezer.albumtracks.AlbumTrackDto
import com.example.soundsphere.data.dtodeezer.artist_top.ArtistTopDto
import com.example.soundsphere.data.dtodeezer.chart.ChartDto
import com.example.soundsphere.data.dtodeezer.genre.GenreDto
import com.example.soundsphere.data.dtodeezer.playlist.PlayListDto
import com.example.soundsphere.data.dtodeezer.playlisttracks.PlayListTrackDto
import com.example.soundsphere.data.dtodeezer.search.searchAlbum.SearchAlbumDto
import com.example.soundsphere.data.dtodeezer.search.searchArtist.SearchArtistDto
import com.example.soundsphere.data.dtodeezer.search.searchPlayList.SearchPlayListDto
import com.example.soundsphere.data.dtodeezer.search.searchTrack.SearchTrackDto
import com.example.soundsphere.data.dtodeezer.track.TrackDto
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DeezerRepository {
    fun getCharts() : Flow<Resource<ChartDto>>
    fun getAlbumTracks(url: String) : Flow<Resource<AlbumTrackDto>>
    fun getPlayListTracks(url: String) : Flow<Resource<PlayListTrackDto>>
    fun getAlbum(id: String) : Flow<Resource<AlbumDto>>
    fun getTracks(id: String) : Flow<Resource<TrackDto>>
    fun getPlayList(id: String) : Flow<Resource<PlayListDto>>
    fun getGenres() : Flow<Resource<GenreDto>>
    fun getArtistTop(url: String): Flow<Resource<ArtistTopDto>>
    fun getSearchAlbum(q: String): Flow<Resource<SearchAlbumDto>>
    fun getSearchTrack(q: String): Flow<Resource<SearchTrackDto>>
    fun getSearchPlayList(q: String): Flow<Resource<SearchPlayListDto>>
    fun getSearchArtist(q: String): Flow<Resource<SearchArtistDto>>
}