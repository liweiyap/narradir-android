package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RawRes;

import com.liweiyap.narradir.utils.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverButton;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableTextView;

public class SettingsBackgroundActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_background);

        mVolumeControlLayoutValueTextView = findViewById(R.id.updownControlLayoutValue);
        mVolumeIncreaseButton = findViewById(R.id.upControlButton);
        mVolumeDecreaseButton = findViewById(R.id.downControlButton);
        mGeneralBackButton = findViewById(R.id.generalBackButton);
        mMainButton = findViewById(R.id.mainButton);

        CustomTypefaceableTextView settingsTitle = findViewById(R.id.settingsTitleTextView);
        settingsTitle.setText(R.string.settings_title_background);

        CustomTypefaceableTextView volumeControlLayoutLabelTextView = findViewById(R.id.updownControlLayoutLabel);
        volumeControlLayoutLabelTextView.setText(R.string.volume_control_layout_label);

        addSingleTargetSelectionToBackgroundSoundSelectionLayout();

        // ----------------------------------------------------------------------
        // receive data from previous Activity
        // ----------------------------------------------------------------------

        Intent intent = getIntent();

        mBackgroundSoundRawResId = intent.getIntExtra("BACKGROUND_SOUND", 0);
        mBackgroundSoundVolume = intent.getFloatExtra("BACKGROUND_VOLUME", 1f);

        selectBackgroundSound(mBackgroundSoundRawResId);
        addBackgroundSoundSetters();

        displayVolume();

        // ----------------------------------------------------------------------
        // volume control
        // ----------------------------------------------------------------------

        mVolumeIncreaseButton.addOnClickObserver(this::increaseVolume);
        mVolumeDecreaseButton.addOnClickObserver(this::decreaseVolume);

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
    protected void onResume()
    {
        super.onResume();

        if (mGeneralMediaPlayer == null)
        {
            return;
        }

        if (mIsPlaying)
        {
            mGeneralMediaPlayer.start();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mGeneralMediaPlayer == null)
        {
            return;
        }

        mIsPlaying = mGeneralMediaPlayer.isPlaying();
        mGeneralMediaPlayer.pause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.release();
            mGeneralMediaPlayer = null;
        }

        mGeneralSoundPool.release();
        mGeneralSoundPool = null;
    }

    private void addSingleTargetSelectionToBackgroundSoundSelectionLayout()
    {
        LinearLayout backgroundSoundSelectionLayout = findViewById(R.id.backgroundSoundSelectionLayout);
        for (int childIdx = 0; childIdx < backgroundSoundSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) backgroundSoundSelectionLayout.getChildAt(childIdx);
            int i = childIdx;
            btn.addOnClickObserver(() -> {
                btn.check();
                for (int j = 0; j < backgroundSoundSelectionLayout.getChildCount(); ++j)
                {
                    if (i != j)
                    {
                        CustomTypefaceableCheckableObserverButton tmp = (CustomTypefaceableCheckableObserverButton) backgroundSoundSelectionLayout.getChildAt(j);
                        tmp.uncheck();
                    }
                }
            });
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

        addSoundToPlayOnButtonClick(mGeneralBackButton);
        addSoundToPlayOnButtonClick(mMainButton);
        addSoundToPlayOnButtonClick(mVolumeIncreaseButton);
        addSoundToPlayOnButtonClick(mVolumeDecreaseButton);
    }

    private void addSoundToPlayOnButtonClick(ObserverListener observerListener)
    {
        if (observerListener == null)
        {
            return;
        }

        observerListener.addOnClickObserver(() -> {
            if (mGeneralMediaPlayer != null)
            {
                mGeneralMediaPlayer.stop();
            }

            mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 1, 0, 1f);
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void selectBackgroundSound(@RawRes int resId)
    {
        CustomTypefaceableCheckableObserverButton btn;

        switch (resId)
        {
            case R.raw.backgroundcards:
                btn = findViewById(R.id.backgroundSoundCardsButton);
                if (btn != null)
                {
                    btn.performClick();
                }
                return;
            case R.raw.backgroundcrickets:
                btn = findViewById(R.id.backgroundSoundCricketsButton);
                if (btn != null)
                {
                    btn.performClick();
                }
                return;
            case R.raw.backgroundfireplace:
                btn = findViewById(R.id.backgroundSoundFireplaceButton);
                if (btn != null)
                {
                    btn.performClick();
                }
                return;
            case R.raw.backgroundrain:
                btn = findViewById(R.id.backgroundSoundRainButton);
                if (btn != null)
                {
                    btn.performClick();
                }
                return;
            case R.raw.backgroundrainforest:
                btn = findViewById(R.id.backgroundSoundRainforestButton);
                if (btn != null)
                {
                    btn.performClick();
                }
                return;
            case R.raw.backgroundrainstorm:
                btn = findViewById(R.id.backgroundSoundRainstormButton);
                if (btn != null)
                {
                    btn.performClick();
                }
                return;
            case R.raw.backgroundwolves:
                btn = findViewById(R.id.backgroundSoundWolvesButton);
                if (btn != null)
                {
                    btn.performClick();
                }
                return;
        }

        btn = findViewById(R.id.backgroundSoundNoneButton);
        if (btn != null)
        {
            btn.performClick();
        }
    }

    private void addBackgroundSoundSetters()
    {
        CustomTypefaceableCheckableObserverButton btn;

        btn = findViewById(R.id.backgroundSoundNoneButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = 0);
        btn.addOnLongClickObserver(() -> {
            if (mGeneralMediaPlayer != null)
            {
                mGeneralMediaPlayer.stop();
            }
        });

        btn = findViewById(R.id.backgroundSoundCardsButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = R.raw.backgroundcards);
        btn.addOnLongClickObserver(() -> playBackgroundSound(R.raw.backgroundcards));

        btn = findViewById(R.id.backgroundSoundCricketsButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = R.raw.backgroundcrickets);
        btn.addOnLongClickObserver(() -> playBackgroundSound(R.raw.backgroundcrickets));

        btn = findViewById(R.id.backgroundSoundFireplaceButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = R.raw.backgroundfireplace);
        btn.addOnLongClickObserver(() -> playBackgroundSound(R.raw.backgroundfireplace));

        btn = findViewById(R.id.backgroundSoundRainButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = R.raw.backgroundrain);
        btn.addOnLongClickObserver(() -> playBackgroundSound(R.raw.backgroundrain));

        btn = findViewById(R.id.backgroundSoundRainforestButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = R.raw.backgroundrainforest);
        btn.addOnLongClickObserver(() -> playBackgroundSound(R.raw.backgroundrainforest));

        btn = findViewById(R.id.backgroundSoundRainstormButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = R.raw.backgroundrainstorm);
        btn.addOnLongClickObserver(() -> playBackgroundSound(R.raw.backgroundrainstorm));

        btn = findViewById(R.id.backgroundSoundWolvesButton);
        btn.addOnClickObserver(() -> mBackgroundSoundRawResId = R.raw.backgroundwolves);
        btn.addOnLongClickObserver(() -> playBackgroundSound(R.raw.backgroundwolves));
    }

    private void playBackgroundSound(@RawRes int soundId)
    {
        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.stop();
        }

        try
        {
            mGeneralMediaPlayer = MediaPlayer.create(this, soundId);
            mGeneralMediaPlayer.setLooping(true);
            mGeneralMediaPlayer.setVolume(mBackgroundSoundVolume, mBackgroundSoundVolume);
            mGeneralMediaPlayer.start();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
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
        intent.putExtra("BACKGROUND_SOUND", mBackgroundSoundRawResId);
        intent.putExtra("BACKGROUND_VOLUME", mBackgroundSoundVolume);
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
        intent.putExtra("BACKGROUND_SOUND", mBackgroundSoundRawResId);
        intent.putExtra("BACKGROUND_VOLUME", mBackgroundSoundVolume);
        setResult(Constants.RESULT_OK_SETTINGS_UNDEFINEDSTEPS, intent);
        finish();
    }

    private SoundPool mGeneralSoundPool;
    private int mClickSoundId;

    private MediaPlayer mGeneralMediaPlayer;
    private boolean mIsPlaying;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private float mBackgroundSoundVolume = 1f;

    private CustomTypefaceableTextView mVolumeControlLayoutValueTextView;
    private ObserverButton mVolumeIncreaseButton;
    private ObserverButton mVolumeDecreaseButton;

    private CustomTypefaceableObserverButton mGeneralBackButton;
    private CustomTypefaceableObserverButton mMainButton;
}