package com.example.soundsphere.data.repository

import com.example.soundsphere.data.dto.albums.DataAlbumsDto
import com.example.soundsphere.data.dto.new_release.DataNewReleaseDto
import com.example.soundsphere.data.dto.playlist.DataPlayListDto
import com.example.soundsphere.data.dto.recomendation.DataRecommendationDto
import com.example.soundsphere.data.dto.search.DataSearchDto
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow


interface SoundSphereApiRepository {
    fun getSearchData(query: String) : Flow<Resource<DataSearchDto>>
    fun getRecommendationData() : Flow<Resource<DataRecommendationDto>>
    fun getPlayListData(playlistId: String) : Flow<Resource<DataPlayListDto>>
    fun getNewReleaseData() : Flow<Resource<DataNewReleaseDto>>
    fun getAlbumsData() : Flow<Resource<DataAlbumsDto>>
}