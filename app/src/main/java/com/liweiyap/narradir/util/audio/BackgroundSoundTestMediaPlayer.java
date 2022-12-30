package com.liweiyap.narradir.util.audio;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;

public class BackgroundSoundTestMediaPlayer implements IMediaPlayerController
{
    public BackgroundSoundTestMediaPlayer(final @NonNull Context context)
    {
        mContext = context;
    }

    public void destroy()
    {
        mContext = null;
        free();
    }

    @Override
    public void play(final String res, final float volume, final MediaPlayer.OnCompletionListener listener)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
        }

        if (res == null)
        {
            return;
        }

        try
        {
            mMediaPlayer = create(res);
            if (mMediaPlayer == null)
            {
                return;
            }

            mMediaPlayer.setLooping(true);
            setVolume(volume);
            mMediaPlayer.setOnCompletionListener(listener);
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

        mMediaPlayer.setOnCompletionListener(null);
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

    @Override
    public void setVolume(final float volume)
    {
        if (mMediaPlayer == null)
        {
            return;
        }

        mMediaPlayer.setVolume(volume, volume);
    }

    private @Nullable MediaPlayer create(final @NonNull String res)
    {
        if (mContext == null)
        {
            return null;
        }

        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
        if (res.equals(mContext.getString(R.string.backgroundsound_cards)))
        {
            return MediaPlayer.create(mContext, R.raw.backgroundcards);
        }
        else if (res.equals(mContext.getString(R.string.backgroundsound_crickets)))
        {
            return MediaPlayer.create(mContext, R.raw.backgroundcrickets);
        }
        else if (res.equals(mContext.getString(R.string.backgroundsound_fireplace)))
        {
            return MediaPlayer.create(mContext, R.raw.backgroundfireplace);
        }
        else if (res.equals(mContext.getString(R.string.backgroundsound_rain)))
        {
            return MediaPlayer.create(mContext, R.raw.backgroundrain);
        }
        else if (res.equals(mContext.getString(R.string.backgroundsound_rainforest)))
        {
            return MediaPlayer.create(mContext, R.raw.backgroundrainforest);
        }
        else if (res.equals(mContext.getString(R.string.backgroundsound_rainstorm)))
        {
            return MediaPlayer.create(mContext, R.raw.backgroundrainstorm);
        }
        else if (res.equals(mContext.getString(R.string.backgroundsound_wolves)))
        {
            return MediaPlayer.create(mContext, R.raw.backgroundwolves);
        }

        return null;
    }

    private Context mContext;

    private MediaPlayer mMediaPlayer;
    private boolean mIsPlaying;
}