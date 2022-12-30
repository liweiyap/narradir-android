package com.liweiyap.narradir.util.audio

import android.content.Context
import android.media.MediaPlayer

import com.liweiyap.narradir.R

class CharacterDescriptionMediaPlayer(context: Context): IMediaPlayerController {
    private var mContext: Context?
    private var mMediaPlayer: MediaPlayer? = null
    private var mMediaPlayerCurrentLength: Int = 0

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

            setVolume(volume)
            mMediaPlayer!!.setOnCompletionListener(listener)
            mMediaPlayer!!.start()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun resume() {
        mMediaPlayer?.seekTo(mMediaPlayerCurrentLength)
        mMediaPlayer?.start()
    }

    override fun pause() {
        mMediaPlayer?.pause()
        mMediaPlayer?.let {
            mMediaPlayerCurrentLength = it.currentPosition
        }
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
            mContext!!.getString(R.string.merlindescription_key) -> MediaPlayer.create(mContext, R.raw.merlindescription)
            mContext!!.getString(R.string.percivaldescription_key) -> MediaPlayer.create(mContext, R.raw.percivaldescription)
            mContext!!.getString(R.string.loyaldescription_key) -> MediaPlayer.create(mContext, R.raw.loyaldescription)
            mContext!!.getString(R.string.assassindescription_key) -> MediaPlayer.create(mContext, R.raw.assassindescription)
            mContext!!.getString(R.string.morganadescription_key) -> MediaPlayer.create(mContext, R.raw.morganadescription)
            mContext!!.getString(R.string.mordreddescription_key) -> MediaPlayer.create(mContext, R.raw.mordreddescription)
            mContext!!.getString(R.string.oberondescription_key) -> MediaPlayer.create(mContext, R.raw.oberondescription)
            mContext!!.getString(R.string.miniondescription_key) -> MediaPlayer.create(mContext, R.raw.miniondescription)
            mContext!!.getString(R.string.liberaldescription_key) -> MediaPlayer.create(mContext, R.raw.liberaldescription)
            mContext!!.getString(R.string.hitlerdescription_key) -> MediaPlayer.create(mContext, R.raw.hitlerdescription)
            mContext!!.getString(R.string.fascistdescription_key) -> MediaPlayer.create(mContext, R.raw.fascistdescription)
            else -> null
        }
    }
}