package com.liweiyap.narradir.util.audio

import android.content.Context
import android.media.SoundPool

import com.liweiyap.narradir.R

class ClickSoundGenerator(context: Context) {
    init {
        allocateResources(context)
    }

    fun allocateResources(context: Context) {
        mSoundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
        mSoundPool?.let {
            mClickSoundId = it.load(context, R.raw.clicksound, 1)
        }
    }

    fun playClickSound() {
        mSoundPool?.play(mClickSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun freeResources() {
        mSoundPool?.release()
        mSoundPool = null
    }

    private var mSoundPool: SoundPool? = null
    private var mClickSoundId = 0
}