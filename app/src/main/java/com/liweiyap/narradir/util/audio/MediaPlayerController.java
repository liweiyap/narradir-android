package com.liweiyap.narradir.util.audio;

import android.media.MediaPlayer;

public interface MediaPlayerController
{
    void play(final String res, final float volume, final MediaPlayer.OnCompletionListener listener);
    void resume();
    void pause();
    void stop();
    void free();
    void setVolume(final float volume);
}