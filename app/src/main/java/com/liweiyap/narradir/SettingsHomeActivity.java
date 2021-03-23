package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RawRes;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.SettingsLayout;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SettingsHomeActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_home);

        // ----------------------------------------------------------------------
        // receive data from previous Activity
        // ----------------------------------------------------------------------

        Intent intent = getIntent();

        mPauseDurationInMilliSecs = intent.getLongExtra("PAUSE_DURATION", 5000);
        mBackgroundSoundRawResId = intent.getIntExtra("BACKGROUND_SOUND", 0);
        mBackgroundSoundVolume = intent.getFloatExtra("BACKGROUND_VOLUME", 1f);
        mNarrationVolume = intent.getFloatExtra("NARRATION_VOLUME", 1f);

        // ----------------------------------------------------------------------
        // set key and value of each individual SettingsLayout
        // ----------------------------------------------------------------------

        SettingsLayout narrationSettingsLayout = findViewById(R.id.narrationSettingsLayout);
        narrationSettingsLayout.setKey("NARRATION");
        narrationSettingsLayout.setValue("Vol " + (int) (mNarrationVolume * 10));

        SettingsLayout backgroundSettingsLayout = findViewById(R.id.backgroundSettingsLayout);
        backgroundSettingsLayout.setKey("BACKGROUND");
        String backgroundSettingsLayoutValue = getBackgroundSoundName(mBackgroundSoundRawResId);
        backgroundSettingsLayout.setValue(
            (backgroundSettingsLayoutValue != null) ?
                (backgroundSettingsLayoutValue + ", Vol " + (int) (mBackgroundSoundVolume * 10)) :
                ("None, Vol " + (int) (mBackgroundSoundVolume * 10))
        );

        SettingsLayout roleTimerSettingsLayout = findViewById(R.id.roleTimerSettingsLayout);
        roleTimerSettingsLayout.setKey("ROLE TIMER");
        roleTimerSettingsLayout.setValue(getTimeFromPauseDuration(mPauseDurationInMilliSecs));
    }

    @SuppressLint("NonConstantResourceId")
    private String getBackgroundSoundName(@RawRes int resId)
    {
        switch (resId)
        {
            case R.raw.backgroundcards:
                return "Cards";
            case R.raw.backgroundcrickets:
                return "Crickets";
            case R.raw.backgroundfireplace:
                return "Fireplace";
            case R.raw.backgroundrain:
                return "Rain";
            case R.raw.backgroundrainforest:
                return "Rainforest";
            case R.raw.backgroundrainstorm:
                return "Rainstorm";
            case R.raw.backgroundwolves:
                return "Wolves";
        }

        return null;
    }

    private String getTimeFromPauseDuration(long msec)
    {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(msec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
            TimeUnit.MILLISECONDS.toSeconds(msec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec))
        );
    }

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;
}