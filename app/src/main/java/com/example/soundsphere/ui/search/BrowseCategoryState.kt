package com.example.soundsphere.ui.search

import com.example.soundsphere.data.dto.browse_category.DataBrowseCategoryDto

data class BrowseCategoryState (
    val isLoading: Boolean = false,
    val isSuccessDataBrowseCategory: DataBrowseCategoryDto? = null,
    val isError: String = ""
)