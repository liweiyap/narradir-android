package com.liweiyap.narradir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RawRes;

import com.liweiyap.narradir.avalon.AvalonCharacterName;
import com.liweiyap.narradir.avalon.AvalonControlGroup;
import com.liweiyap.narradir.ui.CheckableObserverImageButton;
import com.liweiyap.narradir.ui.ObserverImageButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.util.Constants;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AvalonCharacterSelectionActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection_avalon);

        // -----------------------------------------------------------------------------------------
        // Avalon is the default game but another game might have been the last selected.
        // If last selected game is not the default game, then switch game.
        // -----------------------------------------------------------------------------------------

        CustomTypefaceableObserverButton gameSwitcherButton = findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        gameSwitcherButton.setText(getString(R.string.game_switcher_button_secrethitler));
        gameSwitcherButton.addOnClickObserver(() -> navigateToSecretHitlerCharacterSelectionActivity(gameSwitcherButton));

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        int lastSelectedGame = sharedPref.getInt(getString(R.string.last_selected_game_key), Constants.GAME_AVALON);
        if (lastSelectedGame != Constants.GAME_AVALON)
        {
            gameSwitcherButton.performClick();
        }

        // -----------------------------------------------------------------------------------------
        // character image button array, player number selection layout, character selection layouts
        // -----------------------------------------------------------------------------------------

        mAvalonControlGroup = new AvalonControlGroup(
            this,
            findViewById(R.id.playerNumberSelectionLayout),
            findViewById(R.id.p5Button), findViewById(R.id.p6Button), findViewById(R.id.p7Button),
            findViewById(R.id.p8Button), findViewById(R.id.p9Button), findViewById(R.id.p10Button),
            findViewById(R.id.merlinButton), findViewById(R.id.percivalButton), findViewById(R.id.loyal0Button), findViewById(R.id.loyal1Button),
            findViewById(R.id.loyal2Button), findViewById(R.id.loyal3Button), findViewById(R.id.loyal4Button), findViewById(R.id.loyal5Button),
            findViewById(R.id.assassinButton), findViewById(R.id.morganaButton), findViewById(R.id.mordredButton), findViewById(R.id.oberonButton),
            findViewById(R.id.minion0Button), findViewById(R.id.minion1Button), findViewById(R.id.minion2Button), findViewById(R.id.minion3Button));

        loadPreferences();

        // -----------------------------------------------------------------------------------------
        // sounds
        // -----------------------------------------------------------------------------------------

        mClickSoundGenerator = new ClickSoundGenerator(this);
        addSoundToPlayOnButtonClick();

        // -----------------------------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // -----------------------------------------------------------------------------------------

        CustomTypefaceableObserverButton playButton = findViewById(R.id.characterSelectionLayoutPlayButton);
        playButton.addOnClickObserver(() -> navigateToPlayIntroductionActivity(playButton));

        ObserverImageButton settingsButton = findViewById(R.id.characterSelectionLayoutSettingsButton);
        settingsButton.addOnClickObserver(() -> navigateToSettingsHomeActivity(settingsButton));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (mAvalonControlGroup == null)
        {
            return;
        }
        mAvalonControlGroup.resumeCharacterDescriptionMediaPlayer();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        savePreferences();  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873

        if (mAvalonControlGroup == null)
        {
            return;
        }
        mAvalonControlGroup.pauseCharacterDescriptionMediaPlayer();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mAvalonControlGroup != null)
        {
            mAvalonControlGroup.freeCharacterDescriptionMediaPlayer();
        }

        if (mClickSoundGenerator != null)
        {
            mClickSoundGenerator.freeResources();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if ( (requestCode == Constants.REQUEST_SETTINGS_HOME) && (resultCode == Constants.RESULT_OK_SETTINGS_HOME) )
//        {
//            mBackgroundSoundRawResId = data.getIntExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
//            mBackgroundSoundVolume = data.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
//            mPauseDurationInMilliSecs = data.getLongExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
//            mNarrationVolume = data.getFloatExtra(getString(R.string.narration_volume_key), mNarrationVolume);
//        }
//    }

    private void addSoundToPlayOnButtonClick()
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionActivity::addSoundToPlayOnButtonClick(): mAvalonControlGroup is NULL");
        }

        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.ASSASSIN));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON));

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
            if (mAvalonControlGroup != null)
            {
                mAvalonControlGroup.stopCharacterDescriptionMediaPlayer();
            }

            if (mClickSoundGenerator != null)
            {
                mClickSoundGenerator.playClickSound();
            }
        });
    }

    public CheckableObserverImageButton[] getCharacterImageButtonArray()
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionActivity::getCharacterImageButtonArray(): mAvalonControlGroup is NULL");
        }

        return mAvalonControlGroup.getCharacterImageButtonArray();
    }

    private void navigateToSecretHitlerCharacterSelectionActivity(final @NotNull View view)
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putInt(getString(R.string.last_selected_game_key), Constants.GAME_SECRETHITLER);
        sharedPrefEditor.apply();

        Intent intent = new Intent(view.getContext(), SecretHitlerCharacterSelectionActivity.class);
        finish();
        view.getContext().startActivity(intent);
    }

    private void navigateToPlayIntroductionActivity(final @NotNull View view)
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionActivity::navigateToPlayIntroductionActivity(): mAvalonControlGroup is NULL");
        }

        ArrayList<Integer> introSegmentArrayList = new ArrayList<>();

        introSegmentArrayList.add(R.raw.avalonintrosegment0);

        introSegmentArrayList.add(mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).isChecked() ?
            R.raw.avalonintrosegment1withoberon :
            R.raw.avalonintrosegment1nooberon);

        introSegmentArrayList.add(R.raw.avalonintrosegment2);

        if (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked())
        {
            introSegmentArrayList.add(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).isChecked() ?
                R.raw.avalonintrosegment3withmordred :
                R.raw.avalonintrosegment3nomordred);

            introSegmentArrayList.add(R.raw.avalonintrosegment4);

            if (mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).isChecked())
            {
                introSegmentArrayList.add(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).isChecked() ?
                    R.raw.avalonintrosegment5withpercivalwithmorgana :
                    R.raw.avalonintrosegment5withpercivalnomorgana);

                introSegmentArrayList.add(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).isChecked() ?
                    R.raw.avalonintrosegment6withpercivalwithmorgana :
                    R.raw.avalonintrosegment6withpercivalnomorgana);

                introSegmentArrayList.add(R.raw.avalonintrosegment7);
            }
            else
            {
                introSegmentArrayList.add(R.raw.avalonintrosegment5nopercival);
            }
        }
        else
        {
            introSegmentArrayList.add(R.raw.avalonintrosegment3nomerlin);
        }

        Intent intent = new Intent(view.getContext(), PlayIntroductionActivity.class);
        intent.putIntegerArrayListExtra(getString(R.string.intro_segments_key), introSegmentArrayList);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        intent.putExtra(getString(R.string.is_started_from_avalon_key), true);
        view.getContext().startActivity(intent);
    }

    private void navigateToSettingsHomeActivity(final @NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsHomeActivity.class);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
//        startActivityForResult(intent, Constants.REQUEST_SETTINGS_HOME);
        mSettingsHomeActivityResultLauncher.launch(intent);
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private void savePreferences()
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionActivity::savePreferences(): mAvalonControlGroup is NULL");
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        sharedPrefEditor.putInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        sharedPrefEditor.putFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        sharedPrefEditor.putFloat(getString(R.string.narration_volume_key), mNarrationVolume);
        sharedPrefEditor.putInt(getString(R.string.good_player_number_avalon_key), mAvalonControlGroup.getExpectedGoodTotal());
        sharedPrefEditor.putInt(getString(R.string.evil_player_number_avalon_key), mAvalonControlGroup.getExpectedEvilTotal());

        if (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked() != mAvalonControlGroup.getCharacter(AvalonCharacterName.ASSASSIN).isChecked())
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionActivity::savePreferences(): " +
                    "Checked state of Assassin should be identical to that of Merlin");
        }

        sharedPrefEditor.putBoolean(getString(R.string.is_merlin_checked_key), mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_percival_checked_key), mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_morgana_checked_key), mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_mordred_checked_key), mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_oberon_checked_key), mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).isChecked());
        sharedPrefEditor.apply();
    }

    private void loadPreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        mPauseDurationInMilliSecs = sharedPref.getLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mBackgroundSoundRawResId = sharedPref.getInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        mBackgroundSoundVolume = sharedPref.getFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mNarrationVolume = sharedPref.getFloat(getString(R.string.narration_volume_key), mNarrationVolume);

        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionActivity::loadPreferences(): mAvalonControlGroup is NULL");
        }

        if (mAvalonControlGroup.getCharacterImageButtonArray() == null)
        {
            return;
        }

        int expectedGoodTotal = sharedPref.getInt(getString(R.string.good_player_number_avalon_key), mAvalonControlGroup.getExpectedGoodTotal());
        int expectedEvilTotal = sharedPref.getInt(getString(R.string.evil_player_number_avalon_key), mAvalonControlGroup.getExpectedEvilTotal());
        boolean isMerlinChecked = sharedPref.getBoolean(getString(R.string.is_merlin_checked_key), true);
        boolean isPercivalChecked = sharedPref.getBoolean(getString(R.string.is_percival_checked_key), false);
        boolean isMorganaChecked = sharedPref.getBoolean(getString(R.string.is_morgana_checked_key), false);
        boolean isMordredChecked = sharedPref.getBoolean(getString(R.string.is_mordred_checked_key), false);
        boolean isOberonChecked = sharedPref.getBoolean(getString(R.string.is_oberon_checked_key), false);

        switch (expectedGoodTotal + expectedEvilTotal)
        {
            case 5:
                CustomTypefaceableCheckableObserverButton p5Button = findViewById(R.id.p5Button);
                p5Button.performClick();
                break;
            case 6:
                CustomTypefaceableCheckableObserverButton p6Button = findViewById(R.id.p6Button);
                p6Button.performClick();
                break;
            case 7:
                CustomTypefaceableCheckableObserverButton p7Button = findViewById(R.id.p7Button);
                p7Button.performClick();
                break;
            case 8:
                CustomTypefaceableCheckableObserverButton p8Button = findViewById(R.id.p8Button);
                p8Button.performClick();
                break;
            case 9:
                CustomTypefaceableCheckableObserverButton p9Button = findViewById(R.id.p9Button);
                p9Button.performClick();
                break;
            case 10:
                CustomTypefaceableCheckableObserverButton p10Button = findViewById(R.id.p10Button);
                p10Button.performClick();
                break;
            default:
                throw new RuntimeException(
                    "AvalonCharacterSelectionActivity::loadPreferences(): " +
                        "Invalid no of players: " + (expectedGoodTotal + expectedEvilTotal));
        }

        // default: Merlin is checked
        // saved preferences: if Merlin is not checked
        if ( (!isMerlinChecked) && (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).performClick();
        }

        // default: Percival is not checked
        // saved preferences: if Percival is checked
        if ( (isPercivalChecked) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).performClick();
        }

        // default: Morgana is not checked
        // saved preferences: if Morgana is checked
        if ( (isMorganaChecked) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).performClick();
        }

        // default: Mordred is not checked
        // saved preferences: if Mordred is checked
        if ( (isMordredChecked) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).performClick();
        }

        // default: Oberon is not checked
        // saved preferences: if Oberon is checked
        if ( (isOberonChecked) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).performClick();
        }

        mAvalonControlGroup.checkPlayerComposition("AvalonCharacterSelectionActivity::loadPreferences()");

        if (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked() != mAvalonControlGroup.getCharacter(AvalonCharacterName.ASSASSIN).isChecked())
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionActivity::loadPreferences(): " +
                    "Checked state of Assassin should be identical to that of Merlin");
        }
    }

    private AvalonControlGroup mAvalonControlGroup;
    private ClickSoundGenerator mClickSoundGenerator;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;

    private final ActivityResultLauncher<Intent> mSettingsHomeActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
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
}