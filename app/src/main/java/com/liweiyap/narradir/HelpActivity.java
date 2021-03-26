package com.liweiyap.narradir;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;

public class HelpActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mGeneralBackButton = findViewById(R.id.generalBackButton);
        mMainButton = findViewById(R.id.mainButton);

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

    private void addSoundToPlayOnButtonClick()
    {
        addSoundToPlayOnButtonClick(mGeneralBackButton);
        addSoundToPlayOnButtonClick(mMainButton);
    }

    private void addSoundToPlayOnButtonClick(ObserverListener observerListener)
    {
        if (observerListener == null)
        {
            return;
        }

        observerListener.addOnClickObserver(() -> mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 1, 0, 1f));
    }

    private void navigateBackwardsByOneStep()
    {
        Intent intent = new Intent();
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
        setResult(Constants.RESULT_OK_SETTINGS_UNDEFINEDSTEPS, intent);
        finish();
    }

    private SoundPool mGeneralSoundPool;
    private int mClickSoundId;

    private CustomTypefaceableObserverButton mGeneralBackButton;
    private CustomTypefaceableObserverButton mMainButton;
}