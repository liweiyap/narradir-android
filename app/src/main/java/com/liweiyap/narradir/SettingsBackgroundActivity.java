package com.liweiyap.narradir;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RawRes;

import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.ui.ObserverButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.audio.BackgroundSoundDictionary;
import com.liweiyap.narradir.util.audio.BackgroundSoundTestMediaPlayer;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

public class SettingsBackgroundActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_background);

        mVolumeControlLayoutValueTextView = findViewById(R.id.updownControlLayoutValue);
        ObserverButton volumeIncreaseButton = findViewById(R.id.upControlButton);
        ObserverButton volumeDecreaseButton = findViewById(R.id.downControlButton);
        CustomTypefaceableObserverButton generalBackButton = findViewById(R.id.generalBackButton);
        CustomTypefaceableObserverButton mainButton = findViewById(R.id.mainButton);

        CustomTypefaceableTextView settingsTitle = findViewById(R.id.settingsTitleTextView);
        settingsTitle.setText(R.string.settings_title_background);

        CustomTypefaceableTextView volumeControlLayoutLabelTextView = findViewById(R.id.updownControlLayoutLabel);
        volumeControlLayoutLabelTextView.setText(R.string.volume_control_layout_label);

        ViewGroupSingleTargetSelector.addSingleTargetSelection(findViewById(R.id.backgroundSoundSelectionLayout));

        // ----------------------------------------------------------------------
        // receive data from previous Activity
        // ----------------------------------------------------------------------

        Intent intent = getIntent();

        mBackgroundSoundRawResId = intent.getIntExtra(getString(R.string.background_sound_key), 0);
        mBackgroundSoundVolume = intent.getFloatExtra(getString(R.string.background_volume_key), 1f);

        mBackgroundSoundTestMediaPlayer = new BackgroundSoundTestMediaPlayer(this);
        selectBackgroundSound(mBackgroundSoundRawResId);
        addBackgroundSoundSetters();

        displayVolume();

        // ----------------------------------------------------------------------
        // volume control
        // ----------------------------------------------------------------------

        volumeIncreaseButton.addOnClickObserver(this::increaseVolume);
        volumeDecreaseButton.addOnClickObserver(this::decreaseVolume);

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
    protected void onResume()
    {
        super.onResume();

        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }
        mBackgroundSoundTestMediaPlayer.resume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }
        mBackgroundSoundTestMediaPlayer.pause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mBackgroundSoundTestMediaPlayer != null)
        {
            mBackgroundSoundTestMediaPlayer.free();
        }

        if (mClickSoundGenerator != null)
        {
            mClickSoundGenerator.freeResources();
        }
    }

    private void addSoundToPlayOnButtonClick()
    {
        LinearLayout backgroundSoundSelectionLayout = findViewById(R.id.backgroundSoundSelectionLayout);
        for (int childIdx = 0; childIdx < backgroundSoundSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) backgroundSoundSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        addSoundToPlayOnButtonClick(findViewById(R.id.generalBackButton));
        addSoundToPlayOnButtonClick(findViewById(R.id.mainButton));
        addSoundToPlayOnButtonClick(findViewById(R.id.upControlButton));
        addSoundToPlayOnButtonClick(findViewById(R.id.downControlButton));
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener observerListener)
    {
        if (observerListener == null)
        {
            return;
        }

        observerListener.addOnClickObserver(() -> {
            if (mBackgroundSoundTestMediaPlayer != null)
            {
                mBackgroundSoundTestMediaPlayer.stop();
            }

            if (mClickSoundGenerator != null)
            {
                mClickSoundGenerator.playClickSound();
            }
        });
    }

    private void selectBackgroundSound(final @RawRes int resId)
    {
        final int selectorButtonId = BackgroundSoundDictionary.getSelectorButtonIdFromSoundResId(resId);
        final CustomTypefaceableCheckableObserverButton selectorButton = findViewById(selectorButtonId);

        if (selectorButton == null)
        {
            return;
        }

        selectorButton.performClick();
    }

    private void addBackgroundSoundSetters()
    {
        CustomTypefaceableCheckableObserverButton btn = findViewById(R.id.backgroundSoundNoneButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = 0);
        btn.addOnLongClickObserver(() -> {
            if (mBackgroundSoundTestMediaPlayer != null)
            {
                mBackgroundSoundTestMediaPlayer.stop();
            }
        });

        addBackgroundSoundSetter(R.raw.backgroundcards);
        addBackgroundSoundSetter(R.raw.backgroundcrickets);
        addBackgroundSoundSetter(R.raw.backgroundfireplace);
        addBackgroundSoundSetter(R.raw.backgroundrain);
        addBackgroundSoundSetter(R.raw.backgroundrainforest);
        addBackgroundSoundSetter(R.raw.backgroundrainstorm);
        addBackgroundSoundSetter(R.raw.backgroundwolves);
    }

    private void addBackgroundSoundSetter(final @RawRes int resId)
    {
        final int selectorButtonId = BackgroundSoundDictionary.getSelectorButtonIdFromSoundResId(resId);
        final CustomTypefaceableCheckableObserverButton selectorButton = findViewById(selectorButtonId);

        if ( (selectorButton == null) || (selectorButtonId == R.id.backgroundSoundNoneButton) )
        {
            return;
        }

        selectorButton.addOnClickObserver(() -> mBackgroundSoundRawResId = resId);
        selectorButton.addOnLongClickObserver(() -> playBackgroundSound(resId));
    }

    private void playBackgroundSound(final @RawRes int soundId)
    {
        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }

        mBackgroundSoundTestMediaPlayer.play(soundId, mBackgroundSoundVolume);
    }

    private void displayVolume()
    {
        if (mVolumeControlLayoutValueTextView == null)
        {
            return;
        }

        mVolumeControlLayoutValueTextView.setText(String.valueOf(Math.round(mBackgroundSoundVolume * 10)));
    }

    private void increaseVolume()
    {
        mBackgroundSoundVolume = Math.min(1f, mBackgroundSoundVolume + 0.1f);
        displayVolume();
    }

    private void decreaseVolume()
    {
        mBackgroundSoundVolume = Math.max(0f, mBackgroundSoundVolume - 0.1f);
        displayVolume();
    }

    private void navigateBackwardsByOneStep()
    {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
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
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        setResult(Constants.RESULT_OK_SETTINGS_TWOSTEPS, intent);
        finish();
    }

    private ClickSoundGenerator mClickSoundGenerator;

    private BackgroundSoundTestMediaPlayer mBackgroundSoundTestMediaPlayer;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private float mBackgroundSoundVolume = 1f;

    private CustomTypefaceableTextView mVolumeControlLayoutValueTextView;
}