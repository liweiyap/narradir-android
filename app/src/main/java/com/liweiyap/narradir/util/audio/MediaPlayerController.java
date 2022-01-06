package com.liweiyap.narradir.util.audio;

public interface MediaPlayerController
{
    void play(final String res, final float volume);
    void resume();
    void pause();
    void stop();
    void free();
    void setVolume(final float volume);
}