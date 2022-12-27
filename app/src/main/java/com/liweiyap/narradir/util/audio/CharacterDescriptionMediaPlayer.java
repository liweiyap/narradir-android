package com.liweiyap.narradir.util.audio;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;

public class CharacterDescriptionMediaPlayer implements MediaPlayerController
{
    public CharacterDescriptionMediaPlayer(final @NonNull Context context)
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
        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
        if (res.equals(mContext.getString(R.string.merlindescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.merlindescription);
        }
        else if (res.equals(mContext.getString(R.string.percivaldescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.percivaldescription);
        }
        else if (res.equals(mContext.getString(R.string.loyaldescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.loyaldescription);
        }
        else if (res.equals(mContext.getString(R.string.assassindescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.assassindescription);
        }
        else if (res.equals(mContext.getString(R.string.morganadescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.morganadescription);
        }
        else if (res.equals(mContext.getString(R.string.mordreddescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.mordreddescription);
        }
        else if (res.equals(mContext.getString(R.string.oberondescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.oberondescription);
        }
        else if (res.equals(mContext.getString(R.string.miniondescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.miniondescription);
        }
        else if (res.equals(mContext.getString(R.string.liberaldescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.liberaldescription);
        }
        else if (res.equals(mContext.getString(R.string.hitlerdescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.hitlerdescription);
        }
        else if (res.equals(mContext.getString(R.string.fascistdescription_key)))
        {
            return MediaPlayer.create(mContext, R.raw.fascistdescription);
        }

        return null;
    }

    private Context mContext;

    private MediaPlayer mMediaPlayer;
    private int mMediaPlayerCurrentLength;
}