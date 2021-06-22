package com.liweiyap.narradir;

import android.content.Context;
import android.media.SoundPool;

import androidx.annotation.NonNull;

public class ClickSoundGenerator
{
    public ClickSoundGenerator(@NonNull Context context)
    {
        allocateResources(context);
    }

    public void allocateResources(@NonNull Context context)
    {
        mSoundPool = new SoundPool.Builder()
            .setMaxStreams(1)
            .build();
        mClickSoundId = mSoundPool.load(context, R.raw.clicksound, 1);
    }

    public void playClickSound()
    {
        if (mSoundPool == null)
        {
            return;
        }

        mSoundPool.play(mClickSoundId, 1f, 1f, 1, 0, 1f);
    }

    public void freeResources()
    {
        if (mSoundPool == null)
        {
            return;
        }
        mSoundPool.release();
        mSoundPool = null;
    }

    private SoundPool mSoundPool;
    private int mClickSoundId;
}