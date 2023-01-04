package com.liweiyap.narradir.util.audio

import android.media.MediaPlayer

interface IMediaPlayerController {
    fun play(res: String?, volume: Float, listener: MediaPlayer.OnCompletionListener?)
    fun resume()
    fun pause()
    fun stop()
    fun free()
    fun setVolume(volume: Float)
}