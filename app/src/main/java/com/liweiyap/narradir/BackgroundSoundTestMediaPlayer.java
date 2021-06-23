package com.liweiyap.narradir;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class BackgroundSoundTestMediaPlayer implements MediaPlayerController
{
    public BackgroundSoundTestMediaPlayer(final @NonNull Context context)
    {
        mContext = context;
    }

    @Override
    public void play(final @RawRes int resId, final float volume)
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

    @Override
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

    @Override
    public void pause()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mIsPlaying = mMediaPlayer.isPlaying();
        mMediaPlayer.pause();
    }

    @Override
    public void stop()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mMediaPlayer.stop();
    }

    @Override
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