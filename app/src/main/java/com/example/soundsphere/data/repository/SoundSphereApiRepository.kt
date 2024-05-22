package com.example.soundsphere.data.repository

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
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow


interface SoundSphereApiRepository {
    fun getSearchData(query: String): Flow<Resource<DataSearchDto>>
    fun getRecommendationData(): Flow<Resource<DataRecommendationDto>>
    fun getPlayListByIdData(playlistId: String): Flow<Resource<DataPlayListDto>>
    fun getNewReleaseData(): Flow<Resource<DataNewReleaseDto>>
    fun getAlbumsData(): Flow<Resource<DataAlbumsDto>>
    fun getAlbumsByIdData(id: String): Flow<Resource<Album>>
    fun getTrackByAlbumIdData(id: String): Flow<Resource<Tracks>>
    fun getTrackByIdData(id: String): Flow<Resource<DataTrackDto>>
    fun getFeaturedPlayListData(): Flow<Resource<DataFeaturePlaylistDto>>
    fun getTopArtistData(): Flow<Resource<DataTopArtistDto>>
    fun getBrowseCategoryData(): Flow<Resource<DataBrowseCategoryDto>>

}