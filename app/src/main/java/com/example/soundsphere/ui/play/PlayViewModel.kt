package com.example.soundsphere.ui.play

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.soundsphere.data.model.Track
import com.example.soundsphere.data.repository.SoundSphereApiRepository
import com.example.soundsphere.player.service.JetAudioServiceHandler
import com.example.soundsphere.player.service.JetAudioState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val track = Track(
    id = "",
    name = "",
    artist = "",
    preview_url = "".toUri().toString(),
    duration = 0,
    title = ""
)

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val serviceHandler: JetAudioServiceHandler,
    private val soundSphereRepository: SoundSphereApiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var duration by savedStateHandle.saveable {
        mutableStateOf(0L)
    }
    var progress by savedStateHandle.saveable {
        mutableStateOf(0f)
    }
    var progressString by savedStateHandle.saveable {
        mutableStateOf("00:00")
    }
    var isPlaying by savedStateHandle.saveable {
        mutableStateOf(false)
    }
    var currentSelectedTrack by savedStateHandle.saveable {
        mutableStateOf(track)
    }
    var trackList by savedStateHandle.saveable {
        mutableStateOf(listOf<Track>())
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()





    init {
        viewModelScope.launch {
            serviceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    is JetAudioState.Buffering -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.CurrentPlaying -> {
                        currentSelectedTrack = trackList[mediaState.mediaItemIndex]
                    }
                    JetAudioState.Initial -> _uiState.value = UiState.Initial
                    is JetAudioState.Playing -> isPlaying = mediaState.isPlaying
                    is JetAudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.Ready -> {
                        duration = mediaState.duration
                        _uiState.value = UiState.Ready
                    }
                }

            }

        }
    }


    private fun LoadTrackData(){
        viewModelScope.launch {
            val track = soundSphereRepository.getTrackByIdData(currentSelectedTrack.id)
            Log.d("123", track.toString())
        }
    }

    private fun setMediaItems(){
        trackList.map { track->
            MediaItem.Builder()
                .setUri(track.preview_url.toUri())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(track.artist)
                        .setDisplayTitle(track.title)
                        .setSubtitle(track.name)
                        .build()
                )
                .build()
        }.also {
            serviceHandler.setMediaItems(it)
        }
    }

    private fun calculateProgressValue(currentProgress: Long) {
        progress =
            if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f)
            else 0f
        progressString = formatDuration(currentProgress)
    }

    @SuppressLint("DefaultLocale")
    fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
        return String.format("%02d:%02d", minute, seconds)
    }
}


sealed class UIEvents {
    object PlayPause : UIEvents()
    data class SelectedAudioChange(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    object SeekToNext : UIEvents()
    object Backward : UIEvents()
    object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class UiState {
    object Initial : UiState()
    object Ready : UiState()
}