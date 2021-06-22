package com.liweiyap.narradir;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class CharacterDescriptionMediaPlayer
{
    public CharacterDescriptionMediaPlayer(@NonNull Context context)
    {
        mContext = context;
    }

    public void play(@RawRes int descriptionId)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
        }

        try
        {
            // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
            mMediaPlayer = MediaPlayer.create(mContext, descriptionId);
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

        mMediaPlayer.seekTo(mMediaPlayerCurrentLength);
        mMediaPlayer.start();
    }

    public void pause()
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mMediaPlayer.pause();
        mMediaPlayerCurrentLength = mMediaPlayer.getCurrentPosition();
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
    private int mMediaPlayerCurrentLength;
}