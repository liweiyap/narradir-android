package com.liweiyap.narradir;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.ui.ObserverButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

public class SettingsRoleTimerActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_roletimer);

        mPauseControlLayoutValueTextView = findViewById(R.id.updownControlLayoutValue);
        ObserverButton pauseIncreaseButton = findViewById(R.id.upControlButton);
        ObserverButton pauseDecreaseButton = findViewById(R.id.downControlButton);
        CustomTypefaceableObserverButton generalBackButton = findViewById(R.id.generalBackButton);
        CustomTypefaceableObserverButton mainButton = findViewById(R.id.mainButton);

        CustomTypefaceableTextView settingsTitle = findViewById(R.id.settingsTitleTextView);
        settingsTitle.setText(R.string.settings_title_roletimer);

        CustomTypefaceableTextView timeControlLayoutLabelTextView = findViewById(R.id.updownControlLayoutLabel);
        timeControlLayoutLabelTextView.setText(R.string.time_control_layout_label);

        // ----------------------------------------------------------------------
        // receive data from previous Activity
        // ----------------------------------------------------------------------

        Intent intent = getIntent();
        mPauseDurationInMilliSecs = intent.getLongExtra(getString(R.string.pause_duration_key), 5000);
        displayPauseDuration();

        // ----------------------------------------------------------------------
        // pause duration control
        // ----------------------------------------------------------------------

        pauseIncreaseButton.addOnClickObserver(this::increasePauseDuration);
        pauseDecreaseButton.addOnClickObserver(this::decreasePauseDuration);

        // ----------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ----------------------------------------------------------------------

        generalBackButton.addOnClickObserver(this::navigateBackwardsByOneStep);
        mainButton.addOnClickObserver(this::navigateBackwardsByTwoSteps);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                generalBackButton.performClick();
            }
        });

        // ----------------------------------------------------------------------
        // initialise SoundPool for click sound
        // ----------------------------------------------------------------------

        mClickSoundGenerator = new ClickSoundGenerator(this);
        addSoundToPlayOnButtonClick();
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
        addSoundToPlayOnButtonClick(findViewById(R.id.generalBackButton));
        addSoundToPlayOnButtonClick(findViewById(R.id.mainButton));
        addSoundToPlayOnButtonClick(findViewById(R.id.upControlButton));
        addSoundToPlayOnButtonClick(findViewById(R.id.downControlButton));
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener observerListener)
    {
        if ( (observerListener == null) || (mClickSoundGenerator == null) )
        {
            return;
        }

        observerListener.addOnClickObserver(() -> mClickSoundGenerator.playClickSound());
    }

    private void displayPauseDuration()
    {
        if (mPauseControlLayoutValueTextView == null)
        {
            return;
        }

        mPauseControlLayoutValueTextView.setText(String.valueOf(mPauseDurationInMilliSecs/1000));
    }

    private void increasePauseDuration()
    {
        mPauseDurationInMilliSecs = Math.min(mMaxPauseDurationInMilliSecs, mPauseDurationInMilliSecs + 1000);
        displayPauseDuration();
    }

    private void decreasePauseDuration()
    {
        mPauseDurationInMilliSecs = Math.max(mMinPauseDurationInMilliSecs, mPauseDurationInMilliSecs - 1000);
        displayPauseDuration();
    }

    private void navigateBackwardsByOneStep()
    {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        setResult(Constants.RESULT_OK_SETTINGS_ONESTEP, intent);
        finish();
    }

    /**
     * Taken from: https://stackoverflow.com/a/14292451/12367873; https://stackoverflow.com/a/14148838/12367873
     *
     * Alternatively, check out:
     *  - `finishActivity(requestCode)` (https://stackoverflow.com/a/51308125/12367873)
     *  - `intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)` (https://stackoverflow.com/a/21571381/12367873)
     *  - `intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)` (https://stackoverflow.com/a/10085298/12367873)
     */
    private void navigateBackwardsByTwoSteps()
    {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        setResult(Constants.RESULT_OK_SETTINGS_TWOSTEPS, intent);
        finish();
    }

    private ClickSoundGenerator mClickSoundGenerator;

    private long mPauseDurationInMilliSecs = 5000;
    private final long mMaxPauseDurationInMilliSecs = 10000;
    private final long mMinPauseDurationInMilliSecs = 0;

    private CustomTypefaceableTextView mPauseControlLayoutValueTextView;
}