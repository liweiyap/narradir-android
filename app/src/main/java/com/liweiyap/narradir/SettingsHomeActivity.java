package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RawRes;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.SettingsLayout;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;

import org.jetbrains.annotations.NotNull;

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
        mNarrationSettingsLayout.setKey(getString(R.string.settings_title_narration));
        mNarrationSettingsLayout.setValue("Vol " + (int) (mNarrationVolume * 10));

        mBackgroundSettingsLayout = findViewById(R.id.backgroundSettingsLayout);
        mBackgroundSettingsLayout.setKey(getString(R.string.settings_title_background));
        mBackgroundSettingsLayout.setValue(getBackgroundSoundName(mBackgroundSoundRawResId) + ", Vol " + (int) (mBackgroundSoundVolume * 10));

        mRoleTimerSettingsLayout = findViewById(R.id.roleTimerSettingsLayout);
        mRoleTimerSettingsLayout.setKey(getString(R.string.settings_title_roletimer));
        mRoleTimerSettingsLayout.setValue(getTimeFromPauseDuration(mPauseDurationInMilliSecs));

        // ----------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ----------------------------------------------------------------------

        mBackButton = findViewById(R.id.settingsHomeLayoutBackButton);
        mBackButton.addOnClickObserver(this::navigateBackwards);

        mHelpButton = findViewById(R.id.settingsHomeLayoutHelpButton);

        // ----------------------------------------------------------------------
        // initialise SoundPool for click sound
        // ----------------------------------------------------------------------

        mGeneralSoundPool = new SoundPool.Builder()
            .setMaxStreams(1)
            .build();
        mClickSoundId = mGeneralSoundPool.load(this, R.raw.clicksound, 1);
        addSoundToPlayOnButtonClick();

        // ----------------------------------------------------------------------
        // navigation from background sound selection layout itself
        // ----------------------------------------------------------------------

        mBackgroundSettingsLayout.getEditButton().addOnClickObserver(() -> navigateToSettingsBackgroundActivity(mBackgroundSettingsLayout.getEditButton()));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mGeneralSoundPool.release();
        mGeneralSoundPool = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_NEWBACKGROUNDSETTING)
        {
            mBackgroundSoundRawResId = data.getIntExtra("BACKGROUND_SOUND", mBackgroundSoundRawResId);
            mBackgroundSoundVolume = data.getFloatExtra("BACKGROUND_VOLUME", mBackgroundSoundVolume);

            if (resultCode == Constants.RESULT_OK_SETTINGS_UNDEFINEDSTEPS)
            {
                Intent intent = new Intent();
                intent.putExtra("BACKGROUND_SOUND", mBackgroundSoundRawResId);
                intent.putExtra("BACKGROUND_VOLUME", mBackgroundSoundVolume);
                setResult(Constants.RESULT_OK_SETTINGS_HOME, intent);
                finish();
            }
            else if (resultCode == Constants.RESULT_OK_SETTINGS_ONESTEP)
            {
                mBackgroundSettingsLayout.setValue(getBackgroundSoundName(mBackgroundSoundRawResId) + ", Vol " + (int) (mBackgroundSoundVolume * 10));
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private String getBackgroundSoundName(@RawRes int resId)
    {
        switch (resId)
        {
            case R.raw.backgroundcards:
                return getString(R.string.backgroundsound_cards);
            case R.raw.backgroundcrickets:
                return getString(R.string.backgroundsound_crickets);
            case R.raw.backgroundfireplace:
                return getString(R.string.backgroundsound_fireplace);
            case R.raw.backgroundrain:
                return getString(R.string.backgroundsound_rain);
            case R.raw.backgroundrainforest:
                return getString(R.string.backgroundsound_rainforest);
            case R.raw.backgroundrainstorm:
                return getString(R.string.backgroundsound_rainstorm);
            case R.raw.backgroundwolves:
                return getString(R.string.backgroundsound_wolves);
        }

        return getString(R.string.backgroundsound_none);
    }

    private @NotNull String getTimeFromPauseDuration(long msec)
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
        if (mNarrationSettingsLayout != null)
        {
            addSoundToPlayOnButtonClick(mNarrationSettingsLayout.getEditButton());
        }

        if (mBackgroundSettingsLayout != null)
        {
            addSoundToPlayOnButtonClick(mBackgroundSettingsLayout.getEditButton());
        }

        if (mRoleTimerSettingsLayout != null)
        {
            addSoundToPlayOnButtonClick(mRoleTimerSettingsLayout.getEditButton());
        }

        addSoundToPlayOnButtonClick(mBackButton);
        addSoundToPlayOnButtonClick(mHelpButton);
    }

    private void addSoundToPlayOnButtonClick(ObserverListener observerListener)
    {
        if (observerListener == null)
        {
            return;
        }

        observerListener.addOnClickObserver(() -> mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 1, 0, 1f));
    }

    private void navigateToSettingsBackgroundActivity(@NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsBackgroundActivity.class);
        intent.putExtra("BACKGROUND_SOUND", mBackgroundSoundRawResId);
        intent.putExtra("BACKGROUND_VOLUME", mBackgroundSoundVolume);
        startActivityForResult(intent, Constants.REQUEST_NEWBACKGROUNDSETTING);
    }

    private void navigateBackwards()
    {
        Intent intent = new Intent();
        intent.putExtra("BACKGROUND_SOUND", mBackgroundSoundRawResId);
        intent.putExtra("BACKGROUND_VOLUME", mBackgroundSoundVolume);
        intent.putExtra("PAUSE_DURATION", mPauseDurationInMilliSecs);
        intent.putExtra("NARRATION_VOLUME", mNarrationVolume);
        setResult(Constants.RESULT_OK_SETTINGS_HOME, intent);
        finish();
    }

    private SettingsLayout mNarrationSettingsLayout;
    private SettingsLayout mBackgroundSettingsLayout;
    private SettingsLayout mRoleTimerSettingsLayout;

    private CustomTypefaceableObserverButton mBackButton;
    private CustomTypefaceableObserverButton mHelpButton;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;

    private SoundPool mGeneralSoundPool;
    private int mClickSoundId;
}