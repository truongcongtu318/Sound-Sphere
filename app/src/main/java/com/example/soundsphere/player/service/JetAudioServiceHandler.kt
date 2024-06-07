package com.example.soundsphere.player.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JetAudioServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer
) : Player.Listener {
    private val _audioState = MutableStateFlow<JetAudioState>(JetAudioState.Initial)
    val audioState: StateFlow<JetAudioState> = _audioState.asStateFlow()

    private var job: Job? = null

    init {
        exoPlayer.addListener(this)
    }

    fun addMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItems(mediaItems: List<MediaItem>, startIndex: Int) {
        exoPlayer.setMediaItems(mediaItems, startIndex, 0)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun clearExoPlayer() {
        exoPlayer.clearMediaItems()
        exoPlayer.release()
    }

    suspend fun onPlayerEvent(
        playerEvent: PlayerEvent, selectedAudioIndex: Int = -1, seekPosition: Long = 0
    ) {
        when (playerEvent) {
            PlayerEvent.SeekToPrevious -> {
                if (exoPlayer.hasPreviousMediaItem()) {
                    exoPlayer.seekToPreviousMediaItem()
                }
            }

            PlayerEvent.Forward -> {
                exoPlayer.seekForward()
            }

            PlayerEvent.PlayPause -> {
                playOrPause()
                exoPlayer.playWhenReady = !exoPlayer.playWhenReady
            }

            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition.toLong())
            PlayerEvent.SeekToNext -> {
                if (exoPlayer.hasNextMediaItem()) {
                    exoPlayer.seekToNextMediaItem()
                }
            }

            PlayerEvent.SelectedAudioChange -> {
                when (selectedAudioIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        playOrPause()
                    }

                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
                        _audioState.value = JetAudioState.Playing(true)
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }

            else -> {}
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value =
                JetAudioState.Buffering(exoPlayer.currentPosition)

            ExoPlayer.STATE_READY -> _audioState.value = JetAudioState.Ready(exoPlayer.duration)
            Player.STATE_ENDED -> {
                TODO()
            }

            Player.STATE_IDLE -> {
                TODO()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = JetAudioState.Playing(isPlaying)
        _audioState.value = JetAudioState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()

        }
    }

    private suspend fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            startProgressUpdate()
        } else {
            exoPlayer.play()
            _audioState.value = JetAudioState.Playing(true)
        }
        startProgressUpdate()
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _audioState.value = JetAudioState.Progress(exoPlayer.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value = JetAudioState.Playing(false)
    }
}


sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object SelectedAudioChange : PlayerEvent()
    object SeekToPrevious : PlayerEvent()
    object SeekToNext : PlayerEvent()
    object Forward : PlayerEvent()
    object SeekTo : PlayerEvent()
    object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

sealed class JetAudioState {
    object Initial : JetAudioState()
    data class Ready(val duration: Long) : JetAudioState()
    data class Progress(val progress: Long) : JetAudioState()
    data class Buffering(val progress: Long) : JetAudioState()
    data class Playing(val isPlaying: Boolean) : JetAudioState()
    data class CurrentPlaying(val mediaItemIndex: Int) : JetAudioState()
}