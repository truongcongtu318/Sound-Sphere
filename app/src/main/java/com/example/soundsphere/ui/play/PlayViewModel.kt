package com.example.soundsphere.ui.play

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.soundsphere.data.model.Song
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


@SuppressLint("LogNotTimber")
@HiltViewModel
class PlayViewModel @androidx.annotation.OptIn(UnstableApi::class)
@Inject constructor(
    private val serviceHandler: JetAudioServiceHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var duration by savedStateHandle.saveable {
        mutableStateOf(0L)
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    var progress by savedStateHandle.saveable {
        mutableStateOf(0f)
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    var progressString by savedStateHandle.saveable {
        mutableStateOf("00:00")
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    var isPlaying by savedStateHandle.saveable {
        mutableStateOf(false)
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    var currentSelectedTrack by savedStateHandle.saveable {
        mutableStateOf(Song())
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    var trackList by savedStateHandle.saveable {
        mutableStateOf(listOf<Song>())
    }


    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()



    init {
        observeAudioState()
    }


    @androidx.annotation.OptIn(UnstableApi::class)
    private fun observeAudioState() {
        viewModelScope.launch {
            serviceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    is JetAudioState.Buffering -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.CurrentPlaying -> {
                        if (trackList.isNotEmpty() && mediaState.mediaItemIndex in trackList.indices) {
                            if (mediaState.mediaItemIndex !=0) {
                                currentSelectedTrack = trackList[mediaState.mediaItemIndex]
                            }
                        } else {
                            Log.w(
                                "PlayViewModel",
                                "trackList is empty or mediaItemIndex is out of bounds, cannot set currentSelectedTrack."
                            )
                        }
                    }

                    JetAudioState.Initial -> _uiState.value = UiState.Initial
                    is JetAudioState.Playing -> isPlaying = mediaState.isPlaying
                    is JetAudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.Ready -> {
                        duration = mediaState.duration
                        _uiState.value = UiState.Ready
                    }

                    else -> {
                    }

                }
            }
        }
    }

    internal fun setCurrentTrackSelected(song: Song) {
        this.currentSelectedTrack = song
        serviceHandler.addMediaItem(
            MediaItem.Builder().setUri(song.url.toUri()).setMediaMetadata(
                MediaMetadata.Builder().setAlbumArtist(song.artist?.name)
                    .setDisplayTitle(song.title)
                    .build()
            ).build()
        )
        setMediaItems()
    }



    @androidx.annotation.OptIn(UnstableApi::class)
    private fun setMediaItems() {
        trackList.map { song ->
            val imageUri = song.picture
            MediaItem.Builder().setUri(song.url.toUri()).setMediaMetadata(
                MediaMetadata.Builder()
                    .setAlbumArtist(song.artist?.name)
                    .setDisplayTitle(song.title)
                    .setSubtitle(song.title)
                    .setArtworkUri(imageUri.toUri())
                    .build()
            ).build()
        }.also { it ->
            val indexCurrentSelected =
                trackList.indexOf(trackList.find {
                    it.title == currentSelectedTrack.title
                })
            Log.d("PlayViewModel", "setMediaItems: $currentSelectedTrack")
            val startIndex = if (indexCurrentSelected < 0) {
                0
            } else {
                indexCurrentSelected
            }
            currentSelectedTrack = trackList[startIndex]
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
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


    fun onUiEvent(event: UIEvents) = viewModelScope.launch {
        when (event) {
            UIEvents.Backward -> serviceHandler.onPlayerEvent(PlayerEvent.SeekToPrevious)
            UIEvents.Forward -> {
                serviceHandler.onPlayerEvent(PlayerEvent.Forward)
            }

            UIEvents.PlayPause -> {
                serviceHandler.onPlayerEvent(PlayerEvent.PlayPause)
                isPlaying = !isPlaying
            }

            is UIEvents.SeekTo -> {
                serviceHandler.onPlayerEvent(
                    PlayerEvent.SeekTo, seekPosition = ((duration * event.position) / 100f).toLong()
                )
            }

            UIEvents.SeekToNext -> {
                serviceHandler.onPlayerEvent(PlayerEvent.SeekToNext)
            }

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

