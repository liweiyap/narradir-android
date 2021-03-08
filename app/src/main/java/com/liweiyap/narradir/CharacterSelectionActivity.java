package com.liweiyap.narradir;

import android.media.MediaPlayer;
import android.os.Bundle;

public class CharacterSelectionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.testwavenet);
        mp.start();
//        mp.release();
    }
}