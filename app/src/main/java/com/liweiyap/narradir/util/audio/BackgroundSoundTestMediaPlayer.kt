package com.liweiyap.narradir.util.audio

import android.content.Context
import android.media.MediaPlayer

import com.liweiyap.narradir.R

class BackgroundSoundTestMediaPlayer(context: Context): IMediaPlayerController {
    private var mContext: Context?
    private var mMediaPlayer: MediaPlayer? = null
    private var mIsPlaying: Boolean = false

    init {
        mContext = context
    }

    fun destroy() {
        mContext = null
        free()
    }

    override fun play(res: String?, volume: Float, listener: MediaPlayer.OnCompletionListener?) {
        mMediaPlayer?.stop()

        if (res == null) {
            return
        }

        try {
            mMediaPlayer = create(res)
            if (mMediaPlayer == null) {
                return
            }

            mMediaPlayer!!.isLooping = true
            setVolume(volume)
            mMediaPlayer!!.setOnCompletionListener(listener)
            mMediaPlayer!!.start()
        }
        catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun resume() {
        if (mIsPlaying) {
            mMediaPlayer?.start()
        }
    }

    override fun pause() {
        mMediaPlayer?.let {
            mIsPlaying = it.isPlaying
        }
        mMediaPlayer?.pause()
    }

    override fun stop() {
        mMediaPlayer?.setOnCompletionListener(null)
        mMediaPlayer?.stop()
    }

    override fun free() {
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    override fun setVolume(volume: Float) {
        mMediaPlayer?.setVolume(volume, volume)
    }

    private fun create(res: String): MediaPlayer? {
        if (mContext == null) {
            return null
        }

        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
        return when (res) {
            mContext!!.getString(R.string.backgroundsound_cards) -> MediaPlayer.create(mContext, R.raw.backgroundcards)
            mContext!!.getString(R.string.backgroundsound_crickets) -> MediaPlayer.create(mContext, R.raw.backgroundcrickets)
            mContext!!.getString(R.string.backgroundsound_fireplace) -> MediaPlayer.create(mContext, R.raw.backgroundfireplace)
            mContext!!.getString(R.string.backgroundsound_rain) -> MediaPlayer.create(mContext, R.raw.backgroundrain)
            mContext!!.getString(R.string.backgroundsound_rainforest) -> MediaPlayer.create(mContext, R.raw.backgroundrainforest)
            mContext!!.getString(R.string.backgroundsound_rainstorm) -> MediaPlayer.create(mContext, R.raw.backgroundrainstorm)
            mContext!!.getString(R.string.backgroundsound_wolves) -> MediaPlayer.create(mContext, R.raw.backgroundwolves)
            else -> null
        }
    }
}