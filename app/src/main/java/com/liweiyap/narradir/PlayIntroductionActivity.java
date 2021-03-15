package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;

public class PlayIntroductionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_introduction);

        CustomTypefaceableObserverButton stopButton = findViewById(R.id.playIntroLayoutStopButton);
        stopButton.addOnClickObserver(this::finish);
    }
}
