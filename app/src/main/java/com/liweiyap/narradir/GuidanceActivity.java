package com.liweiyap.narradir;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import com.liweiyap.narradir.ui.FullScreenPortraitActivity;
import com.liweiyap.narradir.ui.HtmlHelper;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

public class GuidanceActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // ----------------------------------------------------------------------
        // receive data from previous Activity
        // ----------------------------------------------------------------------

        Intent intent = getIntent();

        int activityType = intent.getIntExtra(getString(R.string.guidance_activity_type_key), Constants.GUIDANCE_NULL);
        if (activityType == Constants.GUIDANCE_NULL)
        {
            throw new RuntimeException(
                "GuidanceActivity::onCreate(): " +
                    "Programming error: incorrect activityType (" + activityType + ")");
        }
        else if (activityType == Constants.GUIDANCE_HELP)
        {
            setContentView(R.layout.activity_guidance_help);
        }
        else if (activityType == Constants.GUIDANCE_PRIVACY)
        {
            setContentView(R.layout.activity_guidance_privacy);
        }

        HtmlHelper.setText(findViewById(R.id.pvcy_subheading0_subsubheading0_list0), getString(R.string.privacy_subheading0_subsubheading0_list0));
        HtmlHelper.setText(findViewById(R.id.pvcy_subheading2_subsubheading0_list0), getString(R.string.privacy_subheading2_subsubheading0_list0));
        HtmlHelper.setText(findViewById(R.id.pvcy_subheading3_subsubheading0_list0), getString(R.string.privacy_subheading3_subsubheading0_list0));
        HtmlHelper.setText(findViewById(R.id.pvcy_subheading5_para0), getString(R.string.privacy_subheading5_para0));

        CustomTypefaceableObserverButton generalBackButton = findViewById(R.id.generalBackButton);
        CustomTypefaceableObserverButton mainButton = findViewById(R.id.mainButton);

        // ----------------------------------------------------------------------
        // initialise SoundPool for click sound
        // (important: initialise before navigation)
        // ----------------------------------------------------------------------

        mClickSoundGenerator = new ClickSoundGenerator(this);
        addSoundToPlayOnButtonClick();

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
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener observerListener)
    {
        if ( (observerListener == null) || (mClickSoundGenerator == null) )
        {
            return;
        }

        observerListener.addOnClickObserver(() -> mClickSoundGenerator.playClickSound());
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
    private void navigateBackwardsByTwoSteps()
    {
        Intent intent = new Intent();
        setResult(Constants.RESULT_OK_SETTINGS_TWOSTEPS, intent);
        finish();
    }

    private ClickSoundGenerator mClickSoundGenerator;
}