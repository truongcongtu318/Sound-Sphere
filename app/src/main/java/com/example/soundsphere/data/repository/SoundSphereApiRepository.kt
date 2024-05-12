package com.example.soundsphere.data.repository

import com.example.soundsphere.data.model.TopSongs
import com.example.soundsphere.utils.Resource
import kotlinx.coroutines.flow.Flow


interface SoundSphereApiRepository {
    fun getTopChartSongs() : Flow<Resource<TopSongs>>
}