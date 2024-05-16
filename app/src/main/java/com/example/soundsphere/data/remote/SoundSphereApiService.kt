package com.example.soundsphere.data.remote

import com.example.soundsphere.data.dto.albums.DataAlbumsDto
import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto
import com.example.soundsphere.data.dto.search.DataSearchDto
import com.example.soundsphere.data.dto.playlist.DataPlayListDto
import com.example.soundsphere.data.dto.recomendation.DataRecommendationDto
import com.example.soundsphere.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface SoundSphereApiService {
    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Path("playlist_id") playlistId: String
    ): Response<DataPlayListDto>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/search")
    suspend fun getSearchData(
        @Query("q") query: String,
        @Query("type") type: String = "track,album,artist,playlist",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 5
    ): Response<DataSearchDto>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/recommendations")
    suspend fun getRecommendations(
        @Query("seed_artists") seedArtists: String? = "5dfZ5uSmzR7VQK0udbAVpf,1LEtM3AleYg1xabW6CRkpi",
        @Query("seed_genres") seedGenres: String? = "vietnamese",
        @Query("seed_tracks") seedTracks: String? = null,
        @Query("limit") limit: Int = 25
    ): Response<DataRecommendationDto>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(): Response<DataNewReleaseDto>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/albums")
    suspend fun getAlbums(
        @Query("ids") ids : String = "382ObEPsp2rxGrnsizN5TX,1A2GTWGtFfWp7KSQTwWOyo,2noRn2Aes5aoNVsU6iWThc"
    ) : Response<DataAlbumsDto>
}

