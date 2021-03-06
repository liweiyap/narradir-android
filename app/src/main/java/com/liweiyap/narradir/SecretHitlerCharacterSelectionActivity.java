package com.liweiyap.narradir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RawRes;

import com.liweiyap.narradir.secrethitler.SecretHitlerControlGroup;
import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.ui.ObserverImageButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.LifecycleActivityResultObserverListener;
import com.liweiyap.narradir.util.PlayerNumberDictionary;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SecretHitlerCharacterSelectionActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection_secrethitler);

        // -----------------------------------------------------------------------------------------
        // character image button array, player number selection layout, character selection layouts
        // -----------------------------------------------------------------------------------------

        mSecretHitlerControlGroup = new SecretHitlerControlGroup(
            this,
            findViewById(R.id.playerNumberSelectionLayout),
            findViewById(R.id.p5Button), findViewById(R.id.p6Button), findViewById(R.id.p7Button),
            findViewById(R.id.p8Button), findViewById(R.id.p9Button), findViewById(R.id.p10Button),
            findViewById(R.id.liberal0Button), findViewById(R.id.liberal1Button), findViewById(R.id.liberal2Button), findViewById(R.id.liberal3Button),
            findViewById(R.id.liberal4Button), findViewById(R.id.liberal5Button),
            findViewById(R.id.hitlerButton), findViewById(R.id.fascist0Button), findViewById(R.id.fascist1Button), findViewById(R.id.fascist2Button));

        loadPreferences();

        // -----------------------------------------------------------------------------------------
        // sounds
        // -----------------------------------------------------------------------------------------

        mClickSoundGenerator = new ClickSoundGenerator(this);
        addSoundToPlayOnButtonClick();

        // -----------------------------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // -----------------------------------------------------------------------------------------

        mSettingsHomeActivityResultObserverListener = new LifecycleActivityResultObserverListener(
            getActivityResultRegistry(),
            getString(R.string.secrethitler_to_settingshome_key),
            result -> {
                if (result.getResultCode() != Constants.RESULT_OK_SETTINGS_HOME)
                {
                    return;
                }

                Intent data = result.getData();
                if (data == null)
                {
                    return;
                }

                mBackgroundSoundRawResId = data.getIntExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
                mBackgroundSoundVolume = data.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
                mPauseDurationInMilliSecs = data.getLongExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
                mNarrationVolume = data.getFloatExtra(getString(R.string.narration_volume_key), mNarrationVolume);
            });
        getLifecycle().addObserver(mSettingsHomeActivityResultObserverListener);

        CustomTypefaceableObserverButton gameSwitcherButton = findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        gameSwitcherButton.setText(getString(R.string.game_switcher_button_avalon));
        gameSwitcherButton.addOnClickObserver(() -> navigateToAvalonCharacterSelectionActivity(gameSwitcherButton));

        CustomTypefaceableObserverButton playButton = findViewById(R.id.characterSelectionLayoutPlayButton);
        playButton.addOnClickObserver(() -> navigateToPlayIntroductionActivity(playButton));

        ObserverImageButton settingsButton = findViewById(R.id.characterSelectionLayoutSettingsButton);
        settingsButton.addOnClickObserver(() -> navigateToSettingsHomeActivity(settingsButton));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (mSecretHitlerControlGroup == null)
        {
            return;
        }
        mSecretHitlerControlGroup.resumeCharacterDescriptionMediaPlayer();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        savePreferences();  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873

        if (mSecretHitlerControlGroup == null)
        {
            return;
        }
        mSecretHitlerControlGroup.pauseCharacterDescriptionMediaPlayer();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mSecretHitlerControlGroup != null)
        {
            mSecretHitlerControlGroup.freeCharacterDescriptionMediaPlayer();
        }

        if (mClickSoundGenerator != null)
        {
            mClickSoundGenerator.freeResources();
        }
    }

    private void addSoundToPlayOnButtonClick()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        CustomTypefaceableObserverButton gameSwitcherButton = findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        addSoundToPlayOnButtonClick(gameSwitcherButton);

        CustomTypefaceableObserverButton playButton = findViewById(R.id.characterSelectionLayoutPlayButton);
        addSoundToPlayOnButtonClick(playButton);

        ObserverImageButton settingsButton = findViewById(R.id.characterSelectionLayoutSettingsButton);
        addSoundToPlayOnButtonClick(settingsButton);
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            if (mSecretHitlerControlGroup != null)
            {
                mSecretHitlerControlGroup.stopCharacterDescriptionMediaPlayer();
            }

            if (mClickSoundGenerator != null)
            {
                mClickSoundGenerator.playClickSound();
            }
        });
    }

    private void navigateToAvalonCharacterSelectionActivity(final @NotNull View view)
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putInt(getString(R.string.last_selected_game_key), Constants.GAME_AVALON);
        sharedPrefEditor.apply();

        Intent intent = new Intent(view.getContext(), AvalonCharacterSelectionActivity.class);
        finish();
        view.getContext().startActivity(intent);
    }

    private void navigateToPlayIntroductionActivity(final @NotNull View view)
    {
        if (mSecretHitlerControlGroup == null)
        {
            throw new RuntimeException("SecretHitlerCharacterSelectionActivity::navigateToPlayIntroductionActivity(): mSecretHitlerControlGroup is NULL");
        }

        ArrayList<Integer> introSegmentArrayList = new ArrayList<>();

        if (mSecretHitlerControlGroup.getExpectedGoodTotal() + mSecretHitlerControlGroup.getExpectedEvilTotal() < 7)
        {
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment0small);
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment1small);
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment2small);
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment3small);
        }
        else
        {
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment0large);
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment1large);
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment2large);
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment3large);
            introSegmentArrayList.add(R.raw.secrethitlerintrosegment4large);
        }

        Intent intent = new Intent(view.getContext(), PlayIntroductionActivity.class);
        intent.putIntegerArrayListExtra(getString(R.string.intro_segments_key), introSegmentArrayList);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        intent.putExtra(getString(R.string.is_started_from_avalon_key), false);
        view.getContext().startActivity(intent);
    }

    private void navigateToSettingsHomeActivity(final @NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsHomeActivity.class);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        mSettingsHomeActivityResultObserverListener.launch(intent);
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private void savePreferences()
    {
        if (mSecretHitlerControlGroup == null)
        {
            throw new RuntimeException("SecretHitlerCharacterSelectionActivity::savePreferences(): mSecretHitlerControlGroup is NULL");
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        sharedPrefEditor.putInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        sharedPrefEditor.putFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        sharedPrefEditor.putFloat(getString(R.string.narration_volume_key), mNarrationVolume);
        sharedPrefEditor.putInt(getString(R.string.good_player_number_secrethitler_key), mSecretHitlerControlGroup.getExpectedGoodTotal());
        sharedPrefEditor.putInt(getString(R.string.evil_player_number_secrethitler_key), mSecretHitlerControlGroup.getExpectedEvilTotal());
        sharedPrefEditor.apply();
    }

    private void loadPreferences()
    {
        if (mSecretHitlerControlGroup == null)
        {
            throw new RuntimeException("SecretHitlerCharacterSelectionActivity::loadPreferences(): mSecretHitlerControlGroup is NULL");
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        mPauseDurationInMilliSecs = sharedPref.getLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mBackgroundSoundRawResId = sharedPref.getInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        mBackgroundSoundVolume = sharedPref.getFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mNarrationVolume = sharedPref.getFloat(getString(R.string.narration_volume_key), mNarrationVolume);
        int expectedGoodTotal = sharedPref.getInt(getString(R.string.good_player_number_secrethitler_key), mSecretHitlerControlGroup.getExpectedGoodTotal());
        int expectedEvilTotal = sharedPref.getInt(getString(R.string.evil_player_number_secrethitler_key), mSecretHitlerControlGroup.getExpectedEvilTotal());

        final int playerNumberButtonId = PlayerNumberDictionary.getSelectorButtonIdFromPlayerNumber(expectedGoodTotal + expectedEvilTotal);
        if (playerNumberButtonId == 0)
        {
            throw new RuntimeException(
                "SecretHitlerCharacterSelectionActivity::loadPreferences(): " +
                    "Invalid no of players: " + (expectedGoodTotal + expectedEvilTotal));
        }
        findViewById(playerNumberButtonId).performClick();
    }

    private SecretHitlerControlGroup mSecretHitlerControlGroup;
    private ClickSoundGenerator mClickSoundGenerator;
    private LifecycleActivityResultObserverListener mSettingsHomeActivityResultObserverListener;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;
}