package com.liweiyap.narradir;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;

import java.util.ArrayList;

public class PlayIntroductionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_introduction);

        Intent intent = getIntent();
        ArrayList<Integer> introSegmentArrayList = intent.getIntegerArrayListExtra("INTRO_SEGMENTS");
        if ( (introSegmentArrayList.size() != 4) &&
             !((introSegmentArrayList.size() == 3) && (introSegmentArrayList.get(2) == R.raw.introsegment2nopercival)) )
        {
            throw new RuntimeException(
                "PlayIntroductionActivity::onCreate(): " +
                    "Invalid input from intent");
        }

        long pauseDurationInMilliSecs = intent.getLongExtra("PAUSE_DURATION", 5000);

        MediaPlayer test = MediaPlayer.create(this, introSegmentArrayList.get(0));
        test.start();

        CustomTypefaceableObserverButton stopButton = findViewById(R.id.playIntroLayoutStopButton);
        stopButton.addOnClickObserver(this::finish);
    }
}
