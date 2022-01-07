package com.liweiyap.narradir;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.liweiyap.narradir.ui.FullScreenPortraitActivity;
import com.liweiyap.narradir.ui.ObserverButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.SnackbarWrapper;
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.IntentHelper;
import com.liweiyap.narradir.util.SnackbarBuilderFlag;
import com.liweiyap.narradir.util.audio.BackgroundSoundTestMediaPlayer;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

import java.util.EnumSet;

public class SettingsBackgroundActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_background);

        mSnackbar = new SnackbarWrapper(this);

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

        mBackgroundSoundVolume = intent.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mBackgroundSoundName = IntentHelper.getStringExtra(intent, getString(R.string.background_sound_name_key), getString(R.string.backgroundsound_none));
        mDoHideBackgroundSoundHint = intent.getBooleanExtra(getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint);

        mBackgroundSoundTestMediaPlayer = new BackgroundSoundTestMediaPlayer(this);
        initBackgroundSound(mBackgroundSoundName);
        addBackgroundSoundSetters();

        displayVolume();

        // ----------------------------------------------------------------------
        // volume control
        // ----------------------------------------------------------------------

        volumeIncreaseButton.addOnClickObserver(this::increaseVolume);
        volumeDecreaseButton.addOnClickObserver(this::decreaseVolume);

        // ----------------------------------------------------------------------
        // initialise SoundPool for click sound
        // (important: should be initialised after volume control to prevent race condition with setting volume!!!)
        // (important: initialise before navigation bar)
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
            addSoundToPlayOnButtonClick(btn, false);
        }

        addSoundToPlayOnButtonClick(findViewById(R.id.generalBackButton), false);
        addSoundToPlayOnButtonClick(findViewById(R.id.mainButton), false);
        addSoundToPlayOnButtonClick(findViewById(R.id.upControlButton), true);
        addSoundToPlayOnButtonClick(findViewById(R.id.downControlButton), true);
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener observerListener, final boolean isVolumeControl)
    {
        if (observerListener == null)
        {
            return;
        }

        observerListener.addOnClickObserver(() -> {
            if (mBackgroundSoundTestMediaPlayer != null)
            {
                if (isVolumeControl)
                {
                    mBackgroundSoundTestMediaPlayer.setVolume(mBackgroundSoundVolume);
                }
                else
                {
                    stopBackgroundSound();
                }
            }

            if (mClickSoundGenerator != null)
            {
                mClickSoundGenerator.playClickSound();
            }
        });
    }

    private void initBackgroundSound(final @NonNull String backgroundSoundName)
    {
        // does not look elegant but it's safe because resource ID integers are non-constant from Gradle version >4.
        if (backgroundSoundName.equals(getString(R.string.backgroundsound_cards)))
        {
            findViewById(R.id.backgroundSoundCardsButton).performClick();
        }
        else if (backgroundSoundName.equals(getString(R.string.backgroundsound_crickets)))
        {
            findViewById(R.id.backgroundSoundCricketsButton).performClick();
        }
        else if (backgroundSoundName.equals(getString(R.string.backgroundsound_fireplace)))
        {
            findViewById(R.id.backgroundSoundFireplaceButton).performClick();
        }
        else if (backgroundSoundName.equals(getString(R.string.backgroundsound_rain)))
        {
            findViewById(R.id.backgroundSoundRainButton).performClick();
        }
        else if (backgroundSoundName.equals(getString(R.string.backgroundsound_rainforest)))
        {
            findViewById(R.id.backgroundSoundRainforestButton).performClick();
        }
        else if (backgroundSoundName.equals(getString(R.string.backgroundsound_rainstorm)))
        {
            findViewById(R.id.backgroundSoundRainstormButton).performClick();
        }
        else if (backgroundSoundName.equals(getString(R.string.backgroundsound_wolves)))
        {
            findViewById(R.id.backgroundSoundWolvesButton).performClick();
        }
        else
        {
            findViewById(R.id.backgroundSoundNoneButton).performClick();
        }
    }

    private void addBackgroundSoundSetters()
    {
        final CustomTypefaceableCheckableObserverButton backgroundSoundNoneButton = findViewById(R.id.backgroundSoundNoneButton);
        final CustomTypefaceableCheckableObserverButton backgroundSoundCardsButton = findViewById(R.id.backgroundSoundCardsButton);
        final CustomTypefaceableCheckableObserverButton backgroundSoundCricketsButton = findViewById(R.id.backgroundSoundCricketsButton);
        final CustomTypefaceableCheckableObserverButton backgroundSoundFireplaceButton = findViewById(R.id.backgroundSoundFireplaceButton);
        final CustomTypefaceableCheckableObserverButton backgroundSoundRainButton = findViewById(R.id.backgroundSoundRainButton);
        final CustomTypefaceableCheckableObserverButton backgroundSoundRainforestButton = findViewById(R.id.backgroundSoundRainforestButton);
        final CustomTypefaceableCheckableObserverButton backgroundSoundRainstormButton = findViewById(R.id.backgroundSoundRainstormButton);
        final CustomTypefaceableCheckableObserverButton backgroundSoundWolvesButton = findViewById(R.id.backgroundSoundWolvesButton);

        backgroundSoundNoneButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_none)));
        backgroundSoundCardsButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_cards)));
        backgroundSoundCricketsButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_crickets)));
        backgroundSoundFireplaceButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_fireplace)));
        backgroundSoundRainButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_rain)));
        backgroundSoundRainforestButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_rainforest)));
        backgroundSoundRainstormButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_rainstorm)));
        backgroundSoundWolvesButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_wolves)));

        backgroundSoundCardsButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_cards)));
        backgroundSoundCricketsButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_crickets)));
        backgroundSoundFireplaceButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_fireplace)));
        backgroundSoundRainButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_rain)));
        backgroundSoundRainforestButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_rainforest)));
        backgroundSoundRainstormButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_rainstorm)));
        backgroundSoundWolvesButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_wolves)));
        backgroundSoundNoneButton.addOnLongClickObserver(this::stopBackgroundSound);
    }

    private void selectBackgroundSound(final @NonNull String sound)
    {
        mBackgroundSoundName = sound;
    }

    private void playBackgroundSound(final @NonNull String sound)
    {
        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }

        mBackgroundSoundTestMediaPlayer.play(sound, mBackgroundSoundVolume, null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showSnackbar();
    }

    private void stopBackgroundSound()
    {
        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }

        mBackgroundSoundTestMediaPlayer.stop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    private void showSnackbar()
    {
        if (mDoHideBackgroundSoundHint)
        {
            return;
        }

        try
        {
            mSnackbar.show(
                findViewById(R.id.nonStartingActivityLayoutNavBar),
                getString(R.string.backgroundsound_mute_notification),
                BaseTransientBottomBar.LENGTH_SHORT,
                getString(R.string.acknowledge_button_text),
                () -> mDoHideBackgroundSoundHint = true,
                EnumSet.of(SnackbarBuilderFlag.SHOW_ABOVE_XY, SnackbarBuilderFlag.ACTION_DISMISSABLE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void decreaseVolume()
    {
        mBackgroundSoundVolume = Math.max(0f, mBackgroundSoundVolume - 0.1f);
        displayVolume();
    }

    private void navigateBackwardsByOneStep()
    {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.background_sound_name_key), mBackgroundSoundName);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint);
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
        intent.putExtra(getString(R.string.background_sound_name_key), mBackgroundSoundName);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint);
        setResult(Constants.RESULT_OK_SETTINGS_TWOSTEPS, intent);
        finish();
    }

    private ClickSoundGenerator mClickSoundGenerator;
    private SnackbarWrapper mSnackbar;

    private BackgroundSoundTestMediaPlayer mBackgroundSoundTestMediaPlayer;
    private float mBackgroundSoundVolume = 1f;
    private String mBackgroundSoundName;
    private boolean mDoHideBackgroundSoundHint = false;

    private CustomTypefaceableTextView mVolumeControlLayoutValueTextView;
}