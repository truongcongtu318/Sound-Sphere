package com.example.soundsphere.ui.download_music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadMusicViewModel @Inject constructor(
    private val downloader: MusicDownloader
) : ViewModel() {

    fun downloadMusic(url: String, fileName: String) {
        viewModelScope.launch {
            downloader.downloadMusic(url, "$fileName.mp3")
        }
    }
}
