package com.liweiyap.narradir;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RawRes;

import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.SettingsLayout;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.TimeDisplay;
import com.liweiyap.narradir.util.audio.BackgroundSoundDictionary;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

import org.jetbrains.annotations.NotNull;

public class SettingsHomeActivity extends ActiveFullScreenPortraitActivity
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

        mPauseDurationInMilliSecs = intent.getLongExtra(getString(R.string.pause_duration_key), 5000);
        mBackgroundSoundRawResId = intent.getIntExtra(getString(R.string.background_sound_key), 0);
        mBackgroundSoundVolume = intent.getFloatExtra(getString(R.string.background_volume_key), 1f);
        mNarrationVolume = intent.getFloatExtra(getString(R.string.narration_volume_key), 1f);

        // ----------------------------------------------------------------------
        // set key and value of each individual SettingsLayout
        // ----------------------------------------------------------------------

        mNarrationSettingsLayout = findViewById(R.id.narrationSettingsLayout);
        mNarrationSettingsLayout.setKey(getString(R.string.settings_title_narration));
        mNarrationSettingsLayout.setValue("Vol " + Math.round(mNarrationVolume * 10));

        mBackgroundSettingsLayout = findViewById(R.id.backgroundSettingsLayout);
        mBackgroundSettingsLayout.setKey(getString(R.string.settings_title_background));
        mBackgroundSettingsLayout.setValue(BackgroundSoundDictionary.getNameStringFromResId(this, mBackgroundSoundRawResId) + ", Vol " + Math.round(mBackgroundSoundVolume * 10));

        mRoleTimerSettingsLayout = findViewById(R.id.roleTimerSettingsLayout);
        mRoleTimerSettingsLayout.setKey(getString(R.string.settings_title_roletimer));
        mRoleTimerSettingsLayout.setValue(TimeDisplay.fromMilliseconds(mPauseDurationInMilliSecs));

        // ----------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ----------------------------------------------------------------------

        CustomTypefaceableObserverButton backButton = findViewById(R.id.settingsHomeLayoutBackButton);
        backButton.addOnClickObserver(this::navigateBackwards);

        CustomTypefaceableObserverButton helpButton = findViewById(R.id.settingsHomeLayoutHelpButton);
        helpButton.addOnClickObserver(() -> navigateToHelpActivity(helpButton));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                backButton.performClick();
            }
        });

        // ----------------------------------------------------------------------
        // initialise SoundPool for click sound
        // ----------------------------------------------------------------------

        mClickSoundGenerator = new ClickSoundGenerator(this);
        addSoundToPlayOnButtonClick();

        // ----------------------------------------------------------------------
        // navigation from background sound selection layout itself
        // ----------------------------------------------------------------------

        mNarrationSettingsLayout.getEditButton().addOnClickObserver(() -> navigateToSettingsNarrationActivity(mNarrationSettingsLayout.getEditButton()));
        mBackgroundSettingsLayout.getEditButton().addOnClickObserver(() -> navigateToSettingsBackgroundActivity(mBackgroundSettingsLayout.getEditButton()));
        mRoleTimerSettingsLayout.getEditButton().addOnClickObserver(() -> navigateToSettingsRoleTimerActivity(mRoleTimerSettingsLayout.getEditButton()));

        // ----------------------------------------------------------------------
        // navigation to external web browser
        // ----------------------------------------------------------------------

        LinearLayout authorInfoLayout = findViewById(R.id.authorInfoLayout);
        authorInfoLayout.setOnClickListener(view -> navigateToAuthorWebsite());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mClickSoundGenerator == null)
        {
            return;
        }
        mClickSoundGenerator.freeResources();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_SETTINGS_NEW)
        {
            mPauseDurationInMilliSecs = data.getLongExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
            mBackgroundSoundRawResId = data.getIntExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
            mBackgroundSoundVolume = data.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
            mNarrationVolume = data.getFloatExtra(getString(R.string.narration_volume_key), mNarrationVolume);

            if (resultCode == Constants.RESULT_OK_SETTINGS_TWOSTEPS)
            {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
                intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
                intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
                intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
                setResult(Constants.RESULT_OK_SETTINGS_HOME, intent);
                finish();
            }
            else if (resultCode == Constants.RESULT_OK_SETTINGS_ONESTEP)
            {
                mNarrationSettingsLayout.setValue("Vol " + Math.round(mNarrationVolume * 10));
                mBackgroundSettingsLayout.setValue(BackgroundSoundDictionary.getNameStringFromResId(this, mBackgroundSoundRawResId) + ", Vol " + Math.round(mBackgroundSoundVolume * 10));
                mRoleTimerSettingsLayout.setValue(TimeDisplay.fromMilliseconds(mPauseDurationInMilliSecs));
            }
        }
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

        addSoundToPlayOnButtonClick(findViewById(R.id.settingsHomeLayoutBackButton));
        addSoundToPlayOnButtonClick(findViewById(R.id.settingsHomeLayoutHelpButton));
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener observerListener)
    {
        if ( (observerListener == null) || (mClickSoundGenerator == null) )
        {
            return;
        }

        observerListener.addOnClickObserver(() -> mClickSoundGenerator.playClickSound());
    }

    private void navigateToSettingsNarrationActivity(final @NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsNarrationActivity.class);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        startActivityForResult(intent, Constants.REQUEST_SETTINGS_NEW);
    }

    private void navigateToSettingsBackgroundActivity(final @NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsBackgroundActivity.class);
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        startActivityForResult(intent, Constants.REQUEST_SETTINGS_NEW);
    }

    private void navigateToSettingsRoleTimerActivity(final @NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsRoleTimerActivity.class);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        startActivityForResult(intent, Constants.REQUEST_SETTINGS_NEW);
    }

    private void navigateToHelpActivity(final @NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), HelpActivity.class);
        startActivityForResult(intent, Constants.REQUEST_SETTINGS_NEW);
    }

    private void navigateBackwards()
    {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        setResult(Constants.RESULT_OK_SETTINGS_HOME, intent);
        finish();
    }

    private void navigateToAuthorWebsite()
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://liweiyap.github.io"));
        startActivity(browserIntent);
    }

    private SettingsLayout mNarrationSettingsLayout;
    private SettingsLayout mBackgroundSettingsLayout;
    private SettingsLayout mRoleTimerSettingsLayout;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;

    private ClickSoundGenerator mClickSoundGenerator;
}