package com.liweiyap.narradir;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class BackgroundSoundTestMediaPlayer
{
    public BackgroundSoundTestMediaPlayer(@NonNull Context context)
    {
        mContext = context;
    }

    public void play(@RawRes int resId, float volume)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
        }

        try
        {
            mMediaPlayer = MediaPlayer.create(mContext, resId);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setVolume(volume, volume);
            mMediaPlayer.start();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }

    public void resume()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        if (mIsPlaying)
        {
            mMediaPlayer.start();
        }
    }

    public void pause()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mIsPlaying = mMediaPlayer.isPlaying();
        mMediaPlayer.pause();
    }

    public void stop()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mMediaPlayer.stop();
    }

    public void free()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    private final Context mContext;

    private MediaPlayer mMediaPlayer;
    private boolean mIsPlaying;
}