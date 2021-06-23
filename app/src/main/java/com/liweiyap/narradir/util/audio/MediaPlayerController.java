package com.liweiyap.narradir.util.audio;

import androidx.annotation.RawRes;

public interface MediaPlayerController
{
    void play(final @RawRes int resId, final float volume);
    void resume();
    void pause();
    void stop();
    void free();
}