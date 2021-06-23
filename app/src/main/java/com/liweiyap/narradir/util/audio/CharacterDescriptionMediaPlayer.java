package com.liweiyap.narradir.util.audio;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class CharacterDescriptionMediaPlayer implements MediaPlayerController
{
    public CharacterDescriptionMediaPlayer(final @NonNull Context context)
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
            // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
            mMediaPlayer = MediaPlayer.create(mContext, resId);
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

        mMediaPlayer.seekTo(mMediaPlayerCurrentLength);
        mMediaPlayer.start();
    }

    @Override
    public void pause()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mMediaPlayer.pause();
        mMediaPlayerCurrentLength = mMediaPlayer.getCurrentPosition();
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
    private int mMediaPlayerCurrentLength;
}