package com.liweiyap.narradir;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import com.liweiyap.narradir.utils.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverButton;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableTextView;

public class SettingsRoleTimerActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_roletimer);

        mPauseControlLayoutValueTextView = findViewById(R.id.updownControlLayoutValue);
        mPauseIncreaseButton = findViewById(R.id.upControlButton);
        mPauseDecreaseButton = findViewById(R.id.downControlButton);
        mGeneralBackButton = findViewById(R.id.generalBackButton);
        mMainButton = findViewById(R.id.mainButton);

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

        mPauseIncreaseButton.addOnClickObserver(this::increasePauseDuration);
        mPauseDecreaseButton.addOnClickObserver(this::decreasePauseDuration);

        // ----------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ----------------------------------------------------------------------

        mGeneralBackButton.addOnClickObserver(this::navigateBackwardsByOneStep);
        mMainButton.addOnClickObserver(this::navigateBackwardsByUndefinedSteps);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (mGeneralBackButton == null)
                {
                    return;
                }

                mGeneralBackButton.performClick();
            }
        });

        // ----------------------------------------------------------------------
        // initialise SoundPool for click sound
        // ----------------------------------------------------------------------

        mGeneralSoundPool = new SoundPool.Builder()
            .setMaxStreams(1)
            .build();
        mClickSoundId = mGeneralSoundPool.load(this, R.raw.clicksound, 1);
        addSoundToPlayOnButtonClick();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mGeneralSoundPool.release();
        mGeneralSoundPool = null;
    }

    private void addSoundToPlayOnButtonClick()
    {
        addSoundToPlayOnButtonClick(mGeneralBackButton);
        addSoundToPlayOnButtonClick(mMainButton);
        addSoundToPlayOnButtonClick(mPauseIncreaseButton);
        addSoundToPlayOnButtonClick(mPauseDecreaseButton);
    }

    private void addSoundToPlayOnButtonClick(ObserverListener observerListener)
    {
        if (observerListener == null)
        {
            return;
        }

        observerListener.addOnClickObserver(() -> mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 1, 0, 1f));
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
    private void navigateBackwardsByUndefinedSteps()
    {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        setResult(Constants.RESULT_OK_SETTINGS_UNDEFINEDSTEPS, intent);
        finish();
    }

    private SoundPool mGeneralSoundPool;
    private int mClickSoundId;

    private long mPauseDurationInMilliSecs = 5000;
    private final long mMaxPauseDurationInMilliSecs = 10000;
    private final long mMinPauseDurationInMilliSecs = 0;

    private CustomTypefaceableTextView mPauseControlLayoutValueTextView;
    private ObserverButton mPauseIncreaseButton;
    private ObserverButton mPauseDecreaseButton;

    private CustomTypefaceableObserverButton mGeneralBackButton;
    private CustomTypefaceableObserverButton mMainButton;
}