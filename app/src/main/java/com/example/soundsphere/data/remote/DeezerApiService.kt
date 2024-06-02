package com.example.soundsphere.data.remote

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
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface DeezerApiService {
    @GET
    suspend fun getCharts(
        @Url url: String = "https://api.deezer.com/chart/0"
    ) : Response<ChartDto>

    @GET
    suspend fun getAlbumTracks(
        @Url url: String
    ) : Response<AlbumTrackDto>
    @GET
    suspend fun getPlayListTracks(
        @Url url: String
    ) : Response<PlayListTrackDto>

    @GET("album/{id}")
    suspend fun getAlbum(
        @Path("id") id: String
    ) : Response<AlbumDto>

    @GET("track/{id}")
    suspend fun getTrack(
        @Path("id") id: String
    ): Response<TrackDto>

    @GET("playlist/{id}")
    suspend fun getPlayList(
        @Path("id") id: String
    ): Response<PlayListDto>

    @GET("genre")
    suspend fun getGenres(): Response<GenreDto>

    @GET
    suspend fun getArtistTop(
        @Url url: String
    ): Response<ArtistTopDto>

    @GET("search/album")
    suspend fun getSearchAlbum(
        @Query("q") q: String
    ): Response<SearchAlbumDto>
    @GET("search/track")
    suspend fun getSearchTrack(
        @Query("q") q: String
    ): Response<SearchTrackDto>

    @GET("search/artist")
    suspend fun getSearchArtist(
        @Query("q") q: String
    ): Response<SearchArtistDto>
    @GET("search/playlist")
    suspend fun getSearchPlayList(
        @Query("q") q: String
    ): Response<SearchPlayListDto>

}