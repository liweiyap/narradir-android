package com.liweiyap.narradir.util.audio;

import android.content.Context;
import android.media.SoundPool;

import androidx.annotation.NonNull;

import com.liweiyap.narradir.R;

public class ClickSoundGenerator
{
    public ClickSoundGenerator(final @NonNull Context context)
    {
        allocateResources(context);
    }

    public void allocateResources(final @NonNull Context context)
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