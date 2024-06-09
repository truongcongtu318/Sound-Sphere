package com.example.soundsphere.utils

sealed class Resource<T>(
    var data: T? = null,
    val msg: String? = null,
    var track: com.example.soundsphere.data.model.Track? = null
) {

    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(msg: String) : Resource<T>(msg = msg)
    class Loading<T> : Resource<T>()
}