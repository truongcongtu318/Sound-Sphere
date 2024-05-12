package com.example.soundsphere.ui.home

data class HomeState (
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)