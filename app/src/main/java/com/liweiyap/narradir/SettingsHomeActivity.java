package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.RawRes;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverListener;
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

        mNarrationSettingsLayout = findViewById(R.id.narrationSettingsLayout);
        mNarrationSettingsLayout.setKey("NARRATION");
        mNarrationSettingsLayout.setValue("Vol " + (int) (mNarrationVolume * 10));

        mBackgroundSettingsLayout = findViewById(R.id.backgroundSettingsLayout);
        mBackgroundSettingsLayout.setKey("BACKGROUND");
        String backgroundSettingsLayoutValue = getBackgroundSoundName(mBackgroundSoundRawResId);
        mBackgroundSettingsLayout.setValue(
            (backgroundSettingsLayoutValue != null) ?
                (backgroundSettingsLayoutValue + ", Vol " + (int) (mBackgroundSoundVolume * 10)) :
                ("None, Vol " + (int) (mBackgroundSoundVolume * 10))
        );

        mRoleTimerSettingsLayout = findViewById(R.id.roleTimerSettingsLayout);
        mRoleTimerSettingsLayout.setKey("ROLE TIMER");
        mRoleTimerSettingsLayout.setValue(getTimeFromPauseDuration(mPauseDurationInMilliSecs));

        // ----------------------------------------------------------------------
        // initialise SoundPool for background noise and click sound
        // ----------------------------------------------------------------------

        mGeneralSoundPool = new SoundPool.Builder()
            .setMaxStreams(1)
            .build();
        mClickSoundId = mGeneralSoundPool.load(this, R.raw.clicksound, 1);
        addSoundToPlayOnButtonClick();
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

    private void addSoundToPlayOnButtonClick()
    {
        addSoundToPlayOnButtonClick(mNarrationSettingsLayout);
        addSoundToPlayOnButtonClick(mBackgroundSettingsLayout);
        addSoundToPlayOnButtonClick(mRoleTimerSettingsLayout);
    }

    private void addSoundToPlayOnButtonClick(ObserverListener observerListener)
    {
        if (observerListener == null)
        {
            return;
        }

        observerListener.addOnClickObserver(() -> mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 1, 0, 1f));
    }

    private SettingsLayout mNarrationSettingsLayout;
    private SettingsLayout mBackgroundSettingsLayout;
    private SettingsLayout mRoleTimerSettingsLayout;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;

    private SoundPool mGeneralSoundPool;
    private int mClickSoundId;
}