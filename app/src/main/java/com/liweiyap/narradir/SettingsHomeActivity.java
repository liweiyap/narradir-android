package com.liweiyap.narradir;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.SettingsLayout;
import com.liweiyap.narradir.ui.TextViewCompatAutosizeHelper;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.IntentHelper;
import com.liweiyap.narradir.util.LifecycleActivityResultObserverListener;
import com.liweiyap.narradir.util.TimeDisplay;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

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
        mBackgroundSoundVolume = intent.getFloatExtra(getString(R.string.background_volume_key), 1f);
        mNarrationVolume = intent.getFloatExtra(getString(R.string.narration_volume_key), 1f);
        mBackgroundSoundName = IntentHelper.getStringExtra(intent, getString(R.string.background_sound_name_key), getString(R.string.backgroundsound_none));

        // ----------------------------------------------------------------------
        // set key and value of each individual SettingsLayout
        // ----------------------------------------------------------------------

        mNarrationSettingsLayout = findViewById(R.id.narrationSettingsLayout);
        mNarrationSettingsLayout.setKey(getString(R.string.settings_title_narration));
        mNarrationSettingsLayout.setValue("Vol " + Math.round(mNarrationVolume * 10));

        mBackgroundSettingsLayout = findViewById(R.id.backgroundSettingsLayout);
        mBackgroundSettingsLayout.setKey(getString(R.string.settings_title_background));
        mBackgroundSettingsLayout.setValue(mBackgroundSoundName + ", Vol " + Math.round(mBackgroundSoundVolume * 10));

        mRoleTimerSettingsLayout = findViewById(R.id.roleTimerSettingsLayout);
        mRoleTimerSettingsLayout.setKey(getString(R.string.settings_title_roletimer));
        mRoleTimerSettingsLayout.setValue(TimeDisplay.fromMilliseconds(mPauseDurationInMilliSecs));

        // ----------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ----------------------------------------------------------------------

        CustomTypefaceableObserverButton privacyButton = findViewById(R.id.privacyButton);
        privacyButton.addOnClickObserver(() -> navigateToPrivacyActivity(privacyButton));

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

        mSettingsIndividualActivityResultObserverListener = new LifecycleActivityResultObserverListener(
            getActivityResultRegistry(),
            getString(R.string.settingshome_to_settingsindividual_key),
            result -> {
                Intent data = result.getData();
                if (data == null)
                {
                    return;
                }

                mPauseDurationInMilliSecs = data.getLongExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
                mBackgroundSoundVolume = data.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
                mNarrationVolume = data.getFloatExtra(getString(R.string.narration_volume_key), mNarrationVolume);

                // Do not use `getString(R.string.backgroundsound_none)`, or value will be accidentally overwritten when navigating backwards not from SettingsBackgroundActivity.
                // We have already made use of IntentHelper earlier in `onCreate()` to make sure that the value is non-null.
                mBackgroundSoundName = IntentHelper.getStringExtra(data, getString(R.string.background_sound_name_key), mBackgroundSoundName);

                if (result.getResultCode() == Constants.RESULT_OK_SETTINGS_TWOSTEPS)
                {
                    Intent newIntent = new Intent();
                    newIntent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
                    newIntent.putExtra(getString(R.string.background_sound_name_key), mBackgroundSoundName);
                    newIntent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
                    newIntent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
                    setResult(Constants.RESULT_OK_SETTINGS_HOME, newIntent);
                    finish();
                }
                else if (result.getResultCode() == Constants.RESULT_OK_SETTINGS_ONESTEP)
                {
                    mNarrationSettingsLayout.setValue("Vol " + Math.round(mNarrationVolume * 10));
                    mBackgroundSettingsLayout.setValue(mBackgroundSoundName + ", Vol " + Math.round(mBackgroundSoundVolume * 10));
                    mRoleTimerSettingsLayout.setValue(TimeDisplay.fromMilliseconds(mPauseDurationInMilliSecs));
                }
            });
        getLifecycle().addObserver(mSettingsIndividualActivityResultObserverListener);

        mNarrationSettingsLayout.getEditButton().addOnClickObserver(() -> navigateToSettingsNarrationActivity(mNarrationSettingsLayout.getEditButton()));
        mBackgroundSettingsLayout.getEditButton().addOnClickObserver(() -> navigateToSettingsBackgroundActivity(mBackgroundSettingsLayout.getEditButton()));
        mRoleTimerSettingsLayout.getEditButton().addOnClickObserver(() -> navigateToSettingsRoleTimerActivity(mRoleTimerSettingsLayout.getEditButton()));

        // ----------------------------------------------------------------------
        // navigation to external web browser
        // ----------------------------------------------------------------------

        LinearLayout authorInfoLayout = findViewById(R.id.authorInfoLayout);
        authorInfoLayout.setOnClickListener(view -> navigateToAuthorWebsite());

        // -----------------------------------------------------------------------------------------
        // auto-sizing TextViews
        // -----------------------------------------------------------------------------------------

        TextViewCompatAutosizeHelper.minimiseAutoSizeTextSizeRange(findViewById(R.id.authorWebsiteTextView));
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

        addSoundToPlayOnButtonClick(findViewById(R.id.privacyButton));
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

    private void navigateToSettingsNarrationActivity(final @NonNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsNarrationActivity.class);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        mSettingsIndividualActivityResultObserverListener.launch(intent);
    }

    private void navigateToSettingsBackgroundActivity(final @NonNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsBackgroundActivity.class);
        intent.putExtra(getString(R.string.background_sound_name_key), mBackgroundSoundName);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mSettingsIndividualActivityResultObserverListener.launch(intent);
    }

    private void navigateToSettingsRoleTimerActivity(final @NonNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsRoleTimerActivity.class);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mSettingsIndividualActivityResultObserverListener.launch(intent);
    }

    private void navigateToPrivacyActivity(final @NonNull View view)
    {
        Intent intent = new Intent(view.getContext(), PrivacyActivity.class);
        mSettingsIndividualActivityResultObserverListener.launch(intent);
    }

    private void navigateToHelpActivity(final @NonNull View view)
    {
        Intent intent = new Intent(view.getContext(), HelpActivity.class);
        mSettingsIndividualActivityResultObserverListener.launch(intent);
    }

    private void navigateBackwards()
    {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.background_sound_name_key), mBackgroundSoundName);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        setResult(Constants.RESULT_OK_SETTINGS_HOME, intent);
        finish();
    }

    private void navigateToAuthorWebsite()
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://liweiyap.github.io"));
        startActivity(browserIntent);
    }

    private SettingsLayout mNarrationSettingsLayout;
    private SettingsLayout mBackgroundSettingsLayout;
    private SettingsLayout mRoleTimerSettingsLayout;

    private long mPauseDurationInMilliSecs = 5000;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;
    private String mBackgroundSoundName;

    private ClickSoundGenerator mClickSoundGenerator;
    private LifecycleActivityResultObserverListener mSettingsIndividualActivityResultObserverListener;
}