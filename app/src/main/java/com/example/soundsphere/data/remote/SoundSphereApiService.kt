package com.example.soundsphere.data.remote

import com.example.soundsphere.data.dto.albums.Album
import com.example.soundsphere.data.dto.albums.DataAlbumsDto
import com.example.soundsphere.data.dto.albums.Tracks
import com.example.soundsphere.data.dto.browse_category.DataBrowseCategoryDto
import com.example.soundsphere.data.dto.feature_playlist.DataFeaturePlaylistDto
import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto
import com.example.soundsphere.data.dto.playlist.DataPlayListDto
import com.example.soundsphere.data.dto.recomendation.DataRecommendationDto
import com.example.soundsphere.data.dto.search.DataSearchDto
import com.example.soundsphere.data.dto.top_artists.DataTopArtistDto
import com.example.soundsphere.data.dto.track.DataTrackDto
import com.example.soundsphere.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface SoundSphereApiService {
    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/playlists/{playlist_id}")
    suspend fun getPlaylistById(
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
        @Query("ids") ids: String = "382ObEPsp2rxGrnsizN5TX,1A2GTWGtFfWp7KSQTwWOyo,2noRn2Aes5aoNVsU6iWThc"
    ): Response<DataAlbumsDto>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/albums/{id}")
    suspend fun getAlbumsById(
        @Path("id") id: String
    ): Response<Album>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/albums/{id}/tracks")
    suspend fun getTrackByAlbumId(
        @Path("id") id: String
    ): Response<Tracks>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/browse/featured-playlists ")
    suspend fun getFeaturedPlaylists(
        @Query("limit") limit: Int = 15
    ): Response<DataFeaturePlaylistDto>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/tracks/{id}")
    suspend fun getTracksById(
        @Path("id") id: String
    ): Response<DataTrackDto>


    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/artists")
    suspend fun getTopArtists(
        @Query("ids") ids: String = "2CIMQHirSU0MQqyYHq0eOx,57dN52uHvrHOxijzpIgu3E,1vCWHaC5f2uS3yhpwWbIA6,66CXWjxzNUsdJxJ2JdwvnR,6qqNVTkY8uBg9cP3Jd7DAH,246dkjvS1zLTtiykXe5h60,3TVXtAsR1Inumwj472S9r4,3YQKmKGau1PzlVlkL1iodx,0du5cEVh5yTK9QJze8zA0C,1Xyo4u8uXC1ZmMpatF05PJ,1mcTU81TzQhprhouKaTkpq,1Xylc3o4UrD53lo9CvFvVg,1RyvyyTE3xzB2ZywiAwp0i,6fWVd57NKTalqvmjRd2t8Z,3Nrfpe0tUJi4K4DXYWgMUX,1uNFoZAHBGtllmzznpCI3s,5cj0lLjcoR7YOSnhnX0Po5,0X2BH1fck6amBIoJhDVmmJ,7dGJo4pcD2V6oG8kP0tJRR,5K4W6rqBFWDnAN6FQUkS6x"
    ): Response<DataTopArtistDto>

    @Headers("Authorization: Bearer ${Constants.AUTHORIZATION_HEADER}")
    @GET("v1/browse/categories")
    suspend fun getCategoriesBrowse(
        @Query("limit") limit: Int = 30
    ): Response<DataBrowseCategoryDto>
}

