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
import com.example.soundsphere.data.dtodeezer.albumtracks.Data
import com.example.soundsphere.data.model.Track
import com.example.soundsphere.data.repository.DeezerRepository
import com.example.soundsphere.player.service.JetAudioServiceHandler
import com.example.soundsphere.player.service.JetAudioState
import com.example.soundsphere.player.service.PlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private var track: Data? = null

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val serviceHandler: JetAudioServiceHandler,
    private val repository: DeezerRepository,
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
        mutableStateOf(Data())
    }
    var trackList by savedStateHandle.saveable {
        mutableStateOf(listOf<Data>())
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


    internal fun loadTrackList(url: String) {
        viewModelScope.launch {
            repository.getAlbumTracks(url).collectLatest { albumTracks ->
                trackList = albumTracks.data?.data ?: listOf()
                setMediaItems()
            }
        }
    }

    internal fun setCurrentTrackSelected(track: Data) {
        this.currentSelectedTrack = track
        serviceHandler.addMediaItem(
            MediaItem.Builder().setUri(track.preview?.toUri()).setMediaMetadata(
                MediaMetadata.Builder().setAlbumArtist(track.artist?.name)
                    .setDisplayTitle(track.title).setSubtitle(track.title).build()
            ).build()
        )
    }

    private fun setMediaItems() {
        trackList.map { track ->
            MediaItem.Builder().setUri(track.preview?.toUri()).setMediaMetadata(
                MediaMetadata.Builder().setAlbumArtist(track.artist?.name)
                    .setDisplayTitle(track.title).setSubtitle(track.title).build()
            ).build()
        }.also {
            val indexCurrentSelected =
                trackList.indexOf(trackList.find { it.id == currentSelectedTrack.id })
            val startIndex = if (indexCurrentSelected < 0) {
                0
            } else {
                indexCurrentSelected
            }
            serviceHandler.setMediaItems(it, startIndex)
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

    fun onUiEvent(event: UIEvents) = viewModelScope.launch {
        when (event) {
            UIEvents.Backward -> serviceHandler.onPlayerEvent(PlayerEvent.SeekToPrevious)
            UIEvents.Forward -> serviceHandler.onPlayerEvent(PlayerEvent.Forward)
            UIEvents.PlayPause -> serviceHandler.onPlayerEvent(PlayerEvent.PlayPause)
            is UIEvents.SeekTo -> {
                serviceHandler.onPlayerEvent(
                    PlayerEvent.SeekTo, seekPosition = ((duration * event.position) / 100f).toLong()
                )
            }

            UIEvents.SeekToNext -> serviceHandler.onPlayerEvent(PlayerEvent.SeekToNext)
            is UIEvents.SelectedAudioChange -> {
                serviceHandler.onPlayerEvent(
                    PlayerEvent.SelectedAudioChange, selectedAudioIndex = event.index
                )
            }

            is UIEvents.UpdateProgress -> {
                serviceHandler.onPlayerEvent(
                    PlayerEvent.UpdateProgress(
                        event.newProgress
                    ),
                )
                progress = event.newProgress
            }
        }


    }
    override fun onCleared() {
        viewModelScope.launch {
            serviceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
        super.onCleared()
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