package com.liweiyap.narradir.util.audio

import android.media.MediaPlayer.OnCompletionListener

interface IMediaPlayerController {
    fun play(res: String?, volume: Float, listener: OnCompletionListener?)
    fun resume()
    fun pause()
    fun stop()
    fun free()
    fun setVolume(volume: Float)
}