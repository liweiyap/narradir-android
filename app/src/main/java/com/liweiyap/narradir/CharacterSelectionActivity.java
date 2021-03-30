package com.liweiyap.narradir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RawRes;
import androidx.core.util.Pair;

import com.liweiyap.narradir.utils.CheckableObserverImageButton;
import com.liweiyap.narradir.utils.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverImageButton;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CharacterSelectionActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        initialiseCharacterImageButtonArray();

        // ------------------------------------------------------------
        // player number selection layout
        // ------------------------------------------------------------

        addSingleTargetSelectionToPlayerNumberSelectionLayout();
        adaptAvailableCharactersAccordingToPlayerNumber();

        // ------------------------------------------------------------
        // character selection layouts
        // ------------------------------------------------------------

        /* set up */

        addSelectionRules();

        CustomTypefaceableObserverButton gameSwitcherButton = findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        gameSwitcherButton.setText(getString(R.string.game_switcher_button_secrethitler));
        gameSwitcherButton.addOnClickObserver(() -> navigateToSecretHitlerCharacterSelectionActivity(gameSwitcherButton));  // set this before loadPreferences() because Avalon might not have been the last selected game

        loadPreferences();

        /* click sound */

        mGeneralSoundPool = new SoundPool.Builder()
            .setMaxStreams(1)
            .build();
        mClickSoundId = mGeneralSoundPool.load(this, R.raw.clicksound, 1);
        addSoundToPlayOnButtonClick();

        /* general MediaPlayer for character descriptions */

        addCharacterDescriptions();

        // ------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ------------------------------------------------------------

        CustomTypefaceableObserverButton playButton = findViewById(R.id.characterSelectionLayoutPlayButton);
        playButton.addOnClickObserver(() -> navigateToPlayIntroductionActivity(playButton));

        ObserverImageButton settingsButton = findViewById(R.id.characterSelectionLayoutSettingsButton);
        settingsButton.addOnClickObserver(() -> navigateToSettingsHomeActivity(settingsButton));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (mGeneralMediaPlayer == null)
        {
            return;
        }

        mGeneralMediaPlayer.seekTo(mGeneralMediaPlayerCurrentLength);
        mGeneralMediaPlayer.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        savePreferences();  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873

        if (mGeneralMediaPlayer == null)
        {
            return;
        }
        mGeneralMediaPlayer.pause();
        mGeneralMediaPlayerCurrentLength = mGeneralMediaPlayer.getCurrentPosition();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if ( (requestCode == Constants.REQUEST_SETTINGS_HOME) && (resultCode == Constants.RESULT_OK_SETTINGS_HOME) )
        {
            mBackgroundSoundRawResId = data.getIntExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
            mBackgroundSoundVolume = data.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
            mPauseDurationInMilliSecs = data.getLongExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
            mNarrationVolume = data.getFloatExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        }
    }

    private void initialiseCharacterImageButtonArray()
    {
        mCharacterImageButtonArray = new CheckableObserverImageButton[CharacterName.getNumberOfCharacters()];
        mCharacterImageButtonArray[CharacterName.MERLIN] = findViewById(R.id.merlinButton);
        mCharacterImageButtonArray[CharacterName.PERCIVAL] = findViewById(R.id.percivalButton);
        mCharacterImageButtonArray[CharacterName.LOYAL0] = findViewById(R.id.loyal0Button);
        mCharacterImageButtonArray[CharacterName.LOYAL1] = findViewById(R.id.loyal1Button);
        mCharacterImageButtonArray[CharacterName.LOYAL2] = findViewById(R.id.loyal2Button);
        mCharacterImageButtonArray[CharacterName.LOYAL3] = findViewById(R.id.loyal3Button);
        mCharacterImageButtonArray[CharacterName.LOYAL4] = findViewById(R.id.loyal4Button);
        mCharacterImageButtonArray[CharacterName.LOYAL5] = findViewById(R.id.loyal5Button);
        mCharacterImageButtonArray[CharacterName.ASSASSIN] = findViewById(R.id.assassinButton);
        mCharacterImageButtonArray[CharacterName.MORGANA] = findViewById(R.id.morganaButton);
        mCharacterImageButtonArray[CharacterName.MORDRED] = findViewById(R.id.mordredButton);
        mCharacterImageButtonArray[CharacterName.OBERON] = findViewById(R.id.oberonButton);
        mCharacterImageButtonArray[CharacterName.MINION0] = findViewById(R.id.minion0Button);
        mCharacterImageButtonArray[CharacterName.MINION1] = findViewById(R.id.minion1Button);
        mCharacterImageButtonArray[CharacterName.MINION2] = findViewById(R.id.minion2Button);
        mCharacterImageButtonArray[CharacterName.MINION3] = findViewById(R.id.minion3Button);
    }

    private void addSingleTargetSelectionToPlayerNumberSelectionLayout()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            int i = childIdx;
            btn.addOnClickObserver(() -> {
                btn.check();
                for (int j = 0; j < playerNumberSelectionLayout.getChildCount(); ++j)
                {
                    if (i != j)
                    {
                        CustomTypefaceableCheckableObserverButton tmp = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(j);
                        tmp.uncheck();
                    }
                }
            });
        }
    }

    private void adaptAvailableCharactersAccordingToPlayerNumber()
    {
        CustomTypefaceableCheckableObserverButton p5Button = findViewById(R.id.p5Button);
        CustomTypefaceableCheckableObserverButton p6Button = findViewById(R.id.p6Button);
        CustomTypefaceableCheckableObserverButton p7Button = findViewById(R.id.p7Button);
        CustomTypefaceableCheckableObserverButton p8Button = findViewById(R.id.p8Button);
        CustomTypefaceableCheckableObserverButton p9Button = findViewById(R.id.p9Button);
        CustomTypefaceableCheckableObserverButton p10Button = findViewById(R.id.p10Button);

        p5Button.addOnClickObserver(() -> {
            int playerNumberChange = 5 - (mExpectedGoodTotal + mExpectedEvilTotal);  // new - old

            if (playerNumberChange > 0)  // new > old (increase)
            {
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
                playerNumberSelectionLayoutChecker(5);
            }
            else if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(5);
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            }
        });

        p6Button.addOnClickObserver(() -> {
            int playerNumberChange = 6 - (mExpectedGoodTotal + mExpectedEvilTotal);  // new - old

            if (playerNumberChange > 0)  // new > old (increase)
            {
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
                playerNumberSelectionLayoutChecker(6);
            }
            else if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(6);
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            }
        });

        p7Button.addOnClickObserver(() -> {
            int playerNumberChange = 7 - (mExpectedGoodTotal + mExpectedEvilTotal);  // new - old

            if (playerNumberChange > 0)  // new > old (increase)
            {
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
                playerNumberSelectionLayoutChecker(7);
            }
            else if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(7);
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            }
        });

        p8Button.addOnClickObserver(() -> {
            int playerNumberChange = 8 - (mExpectedGoodTotal + mExpectedEvilTotal);  // new - old

            if (playerNumberChange > 0)  // new > old (increase)
            {
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
                playerNumberSelectionLayoutChecker(8);
            }
            else if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(8);
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            }
        });

        p9Button.addOnClickObserver(() -> {
            int playerNumberChange = 9 - (mExpectedGoodTotal + mExpectedEvilTotal);  // new - old

            if (playerNumberChange > 0)  // new > old (increase)
            {
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
                playerNumberSelectionLayoutChecker(9);
            }
            else if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(9);
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            }
        });

        p10Button.addOnClickObserver(() -> {
            int playerNumberChange = 10 - (mExpectedGoodTotal + mExpectedEvilTotal);  // new - old

            if (playerNumberChange > 0)  // new > old (increase)
            {
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.VISIBLE);
                playerNumberSelectionLayoutChecker(10);
            }
            else if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(10);
                mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
                mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.VISIBLE);
            }
        });
    }

    private void playerNumberSelectionLayoutChecker(final int newPlayerNumber)
    {
        if ((newPlayerNumber < 5) || (newPlayerNumber > 10))
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::playerNumberSelectionLayoutChecker(): " +
                    "Invalid input " + newPlayerNumber);
        }

        Pair<Integer, Integer> newPlayerComposition = getPlayerComposition(newPlayerNumber);
        int newExpectedGoodTotal = newPlayerComposition.first;
        int newExpectedEvilTotal = newPlayerComposition.second;

        int expectedGoodChange = newExpectedGoodTotal - mExpectedGoodTotal;  // new - old
        int expectedEvilChange = newExpectedEvilTotal - mExpectedEvilTotal;  // new - old

        if (expectedGoodChange > 0)  // new > old (increase)
        {
            searchAndCheckNewCharacters(CharacterName.LOYAL0, CharacterName.LOYAL5, expectedGoodChange);
        }
        else  // new < old (decrease)
        {
            searchAndUncheckOldCharacters(CharacterName.LOYAL5, CharacterName.LOYAL0, -expectedGoodChange);
        }

        if (expectedEvilChange > 0)  // new > old (increase)
        {
            searchAndCheckNewCharacters(CharacterName.MINION0, CharacterName.MINION3, expectedEvilChange);
        }
        else  // new < old (decrease)
        {
            searchAndUncheckOldCharacters(CharacterName.MINION3, CharacterName.MORGANA, -expectedEvilChange);
        }

        // update old values to new values
        mExpectedGoodTotal = newExpectedGoodTotal;
        mExpectedEvilTotal = newExpectedEvilTotal;
    }

    private Pair<Integer, Integer> getPlayerComposition(final int playerNumber)
    {
        if ((playerNumber < 5) || (playerNumber > 10))
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::getPlayerComposition(): " +
                    "Invalid input " + playerNumber);
        }

        switch (playerNumber)
        {
            case 5:
                return new Pair<>(3, 2);
            case 6:
                return new Pair<>(4, 2);
            case 7:
                return new Pair<>(4, 3);
            case 8:
                return new Pair<>(5, 3);
            case 9:
                return new Pair<>(6, 3);
            case 10:
                return new Pair<>(6, 4);
        }

        return new Pair<>(null, null);  // quick and dirty fix
    }

    private void addSoundToPlayOnButtonClick()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        addSoundToPlayOnButtonClick(mCharacterImageButtonArray[CharacterName.MERLIN]);
        addSoundToPlayOnButtonClick(mCharacterImageButtonArray[CharacterName.PERCIVAL]);
        addSoundToPlayOnButtonClick(mCharacterImageButtonArray[CharacterName.ASSASSIN]);
        addSoundToPlayOnButtonClick(mCharacterImageButtonArray[CharacterName.MORGANA]);
        addSoundToPlayOnButtonClick(mCharacterImageButtonArray[CharacterName.MORDRED]);
        addSoundToPlayOnButtonClick(mCharacterImageButtonArray[CharacterName.OBERON]);

        CustomTypefaceableObserverButton gameSwitcherButton = findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        addSoundToPlayOnButtonClick(gameSwitcherButton);

        CustomTypefaceableObserverButton playButton = findViewById(R.id.characterSelectionLayoutPlayButton);
        addSoundToPlayOnButtonClick(playButton);

        ObserverImageButton settingsButton = findViewById(R.id.characterSelectionLayoutSettingsButton);
        addSoundToPlayOnButtonClick(settingsButton);
    }

    private void addSoundToPlayOnButtonClick(ObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            if (mGeneralMediaPlayer != null)
            {
                mGeneralMediaPlayer.stop();
            }

            mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 1, 0, 1f);
        });
    }

    private void addCharacterDescriptions()
    {
        try
        {
            mCharacterImageButtonArray[CharacterName.MERLIN].addOnLongClickObserver(() -> playCharacterDescription(R.raw.merlindescription));
            mCharacterImageButtonArray[CharacterName.PERCIVAL].addOnLongClickObserver(() -> playCharacterDescription(R.raw.percivaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL0].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL1].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL2].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL3].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL4].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL5].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.ASSASSIN].addOnLongClickObserver(() -> playCharacterDescription(R.raw.assassindescription));
            mCharacterImageButtonArray[CharacterName.MORGANA].addOnLongClickObserver(() -> playCharacterDescription(R.raw.morganadescription));
            mCharacterImageButtonArray[CharacterName.MORDRED].addOnLongClickObserver(() -> playCharacterDescription(R.raw.mordreddescription));
            mCharacterImageButtonArray[CharacterName.OBERON].addOnLongClickObserver(() -> playCharacterDescription(R.raw.oberondescription));
            mCharacterImageButtonArray[CharacterName.MINION0].addOnLongClickObserver(() -> playCharacterDescription(R.raw.miniondescription));
            mCharacterImageButtonArray[CharacterName.MINION1].addOnLongClickObserver(() -> playCharacterDescription(R.raw.miniondescription));
            mCharacterImageButtonArray[CharacterName.MINION2].addOnLongClickObserver(() -> playCharacterDescription(R.raw.miniondescription));
            mCharacterImageButtonArray[CharacterName.MINION3].addOnLongClickObserver(() -> playCharacterDescription(R.raw.miniondescription));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void playCharacterDescription(int descriptionId)
    {
        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.stop();
        }

        try
        {
            // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
            mGeneralMediaPlayer = MediaPlayer.create(this, descriptionId);
            mGeneralMediaPlayer.start();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }

    private void addSelectionRules()
    {
        mCharacterImageButtonArray[CharacterName.MERLIN].addOnClickObserver(this::addMerlinSelectionRules);
        mCharacterImageButtonArray[CharacterName.PERCIVAL].addOnClickObserver(this::addPercivalSelectionRules);
        mCharacterImageButtonArray[CharacterName.LOYAL0].addOnClickObserver(this::addGeneralGoodSelectionRules);
        mCharacterImageButtonArray[CharacterName.LOYAL1].addOnClickObserver(this::addGeneralGoodSelectionRules);
        mCharacterImageButtonArray[CharacterName.LOYAL2].addOnClickObserver(this::addGeneralGoodSelectionRules);
        mCharacterImageButtonArray[CharacterName.LOYAL3].addOnClickObserver(this::addGeneralGoodSelectionRules);
        mCharacterImageButtonArray[CharacterName.LOYAL4].addOnClickObserver(this::addGeneralGoodSelectionRules);
        mCharacterImageButtonArray[CharacterName.LOYAL5].addOnClickObserver(this::addGeneralGoodSelectionRules);
        mCharacterImageButtonArray[CharacterName.ASSASSIN].addOnClickObserver(this::addMerlinSelectionRules);
        mCharacterImageButtonArray[CharacterName.MORGANA].addOnClickObserver(this::addMorganaSelectionRules);
        mCharacterImageButtonArray[CharacterName.MORDRED].addOnClickObserver(this::addMordredSelectionRules);
        mCharacterImageButtonArray[CharacterName.OBERON].addOnClickObserver(this::addOberonSelectionRules);
        mCharacterImageButtonArray[CharacterName.MINION0].addOnClickObserver(this::addGeneralEvilSelectionRules);
        mCharacterImageButtonArray[CharacterName.MINION1].addOnClickObserver(this::addGeneralEvilSelectionRules);
        mCharacterImageButtonArray[CharacterName.MINION2].addOnClickObserver(this::addGeneralEvilSelectionRules);
        mCharacterImageButtonArray[CharacterName.MINION3].addOnClickObserver(this::addGeneralEvilSelectionRules);
    }

    /**
     * If Merlin is selected, then Assassin is auto-selected, and vice versa.
     * In addition, one of the LOYAL is auto-deselected, and one of the MINIONS is auto-deselected.
     *
     * If Merlin is deselected, then Assassin is auto-deselected, and vice versa.
     * In addition, one of the LOYAL is auto-selected, and one of the MINIONS is auto-selected.
     * If Percival, Morgana, or Mordred are already selected, they should be replaced by MINIONS.
     */
    private void addMerlinSelectionRules()
    {
        if (mCharacterImageButtonArray[CharacterName.MERLIN].isChecked())
        {
            if (mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked())
            {
                mCharacterImageButtonArray[CharacterName.PERCIVAL].performClick();
            }

            if (mCharacterImageButtonArray[CharacterName.MORDRED].isChecked())
            {
                mCharacterImageButtonArray[CharacterName.MORDRED].performClick();
            }

            mCharacterImageButtonArray[CharacterName.MERLIN].uncheck();
            mCharacterImageButtonArray[CharacterName.ASSASSIN].uncheck();
            checkNewLoyal();
            checkNewMinion();
        }
        else
        {
            mCharacterImageButtonArray[CharacterName.MERLIN].check();
            mCharacterImageButtonArray[CharacterName.ASSASSIN].check();
            uncheckOldLoyal();
            searchAndUncheckOldCharacters(CharacterName.MINION3, CharacterName.OBERON, 1);
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMerlinSelectionRules(): " +
                    "expected good player total is " + mExpectedGoodTotal +
                    " but actual good player total is " + actualGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMerlinSelectionRules(): " +
                    "expected evil player total is " + mExpectedEvilTotal +
                    " but actual evil player total is " + actualEvilTotal);
        }
    }

    /**
     * TRIANGLE amongst Percival, Morgana, and Mordred:
     * You can have Percival without Morgana but you cannot have Morgana without Percival.
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * You can have Percival without Mordred and you can also have Mordred without Percival.
     *
     * If Percival is selected:
     *  - If Merlin is not already selected, then Merlin is auto-selected.
     *  - In a 5-player game, Morgana is auto-selected.
     *     - If, prior to this, Morgana was not already selected, then one of the Evil players is auto-deselected.
     *  - One of the LOYAL is auto-deselected.
     *
     * If Percival is deselected:
     *  - Morgana is auto-deselected.
     *     - If, prior to this, Morgana was already selected, then one of the MINIONS is auto-selected.
     *  - One of the LOYAL is auto-selected.
     */
    private void addPercivalSelectionRules()
    {
        if (mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked())
        {
            mCharacterImageButtonArray[CharacterName.PERCIVAL].uncheck();
            checkNewLoyal();

            if (mCharacterImageButtonArray[CharacterName.MORGANA].isChecked())
            {
                checkNewMinion();
            }
            mCharacterImageButtonArray[CharacterName.MORGANA].uncheck();
        }
        else
        {
            mCharacterImageButtonArray[CharacterName.PERCIVAL].check();
            uncheckOldLoyal();

            if (!mCharacterImageButtonArray[CharacterName.MERLIN].isChecked())
            {
                mCharacterImageButtonArray[CharacterName.MERLIN].performClick();
            }

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (!mCharacterImageButtonArray[CharacterName.MORDRED].isChecked()) )
            {
                if (!mCharacterImageButtonArray[CharacterName.MORGANA].isChecked())
                {
                    searchAndUncheckOldCharacters(CharacterName.MINION3, CharacterName.MORDRED, 1);
                }
                mCharacterImageButtonArray[CharacterName.MORGANA].check();
            }
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addPercivalSelectionRules(): " +
                    "expected good player total is " + mExpectedGoodTotal +
                    " but actual good player total is " + actualGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addPercivalSelectionRules(): " +
                    "expected evil player total is " + mExpectedEvilTotal +
                    " but actual evil player total is " + actualEvilTotal);
        }
    }

    /**
     * TRIANGLE amongst Percival, Morgana, and Mordred:
     * You can have Percival without Morgana but you cannot have Morgana without Percival.
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * You can have Percival without Mordred and you can also have Mordred without Percival.
     *
     * If Morgana is selected:
     *  - If Percival is not already selected, then Percival is auto-selected and one of the LOYAL is auto-deselected.
     *  - One of the Evil players is auto-deselected.
     * If Morgana is deselected:
     *  - If Percival is not already selected, then throw error.
     *  - In a 5-player game, if Percival is already selected, then Mordred is auto-selected.
     *     - If, prior to this, Mordred was already selected, then one of the MINIONS is auto-selected.
     *  - In a non 5-player game, one of the MINIONS is auto-selected.
     */
    private void addMorganaSelectionRules()
    {
        if (mCharacterImageButtonArray[CharacterName.MORGANA].isChecked())
        {
            if (!mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked())
            {
                throw new RuntimeException(
                    "CharacterSelectionActivity::addMorganaSelectionRules(): " +
                        "Morgana was active in the absence of Percival");
            }

            mCharacterImageButtonArray[CharacterName.MORGANA].uncheck();

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked()) )
            {
                if (mCharacterImageButtonArray[CharacterName.MORDRED].isChecked())
                {
                    checkNewMinion();
                }
                mCharacterImageButtonArray[CharacterName.MORDRED].check();
            }
            else
            {
                checkNewMinion();
            }
        }
        else
        {
            mCharacterImageButtonArray[CharacterName.MORGANA].check();
            searchAndUncheckOldCharacters(CharacterName.MINION3, CharacterName.MORDRED, 1);

            if (!mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked())
            {
                mCharacterImageButtonArray[CharacterName.PERCIVAL].performClick();
            }
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMorganaSelectionRules(): " +
                    "expected good player total is " + mExpectedGoodTotal +
                    " but actual good player total is " + actualGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMorganaSelectionRules(): " +
                    "expected evil player total is " + mExpectedEvilTotal +
                    " but actual evil player total is " + actualEvilTotal);
        }
    }

    /**
     * TRIANGLE amongst Percival, Morgana, and Mordred:
     * You can have Percival without Morgana but you cannot have Morgana without Percival.
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * You can have Percival without Mordred and you can also have Mordred without Percival.
     *
     * You can have Merlin without Mordred but you cannot have Mordred without Merlin.
     *
     * If Mordred is selected:
     *  - If Merlin is not already selected, then Merlin is auto-selected and one of the LOYAL is auto-deselected.
     *  - In a 5-player game, if Percival is not already selected, then Percival is auto-selected.
     *  - One of the Evil players is auto-deselected.
     * If Mordred is deselected:
     *  - If Merlin is not already selected, then throw error.
     *  - In a 5-player game, if Percival is already selected, then Morgana is auto-selected.
     *     - If, prior to this, Morgana was already selected, then one of the MINIONS is auto-selected.
     *  - In a non 5-player game, one of the MINIONS is auto-selected.
     */
    private void addMordredSelectionRules()
    {
        if (mCharacterImageButtonArray[CharacterName.MORDRED].isChecked())
        {
            if (!mCharacterImageButtonArray[CharacterName.MERLIN].isChecked())
            {
                throw new RuntimeException(
                    "CharacterSelectionActivity::addMordredSelectionRules(): " +
                        "Mordred was active in the absence of Merlin");
            }

            mCharacterImageButtonArray[CharacterName.MORDRED].uncheck();

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked()) )
            {
                if (mCharacterImageButtonArray[CharacterName.MORGANA].isChecked())
                {
                    checkNewMinion();
                }
                mCharacterImageButtonArray[CharacterName.MORGANA].check();
            }
            else
            {
                checkNewMinion();
            }
        }
        else
        {
            searchAndUncheckOldCharacters(CharacterName.MINION3, CharacterName.MORGANA, 1);
            mCharacterImageButtonArray[CharacterName.MORDRED].check();

            if (!mCharacterImageButtonArray[CharacterName.MERLIN].isChecked())
            {
                mCharacterImageButtonArray[CharacterName.MERLIN].performClick();
            }

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (!mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked()) )
            {
                mCharacterImageButtonArray[CharacterName.PERCIVAL].performClick();
            }
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMordredSelectionRules(): " +
                    "expected good player total is " + mExpectedGoodTotal +
                    " but actual good player total is " + actualGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMordredSelectionRules(): " +
                    "expected evil player total is " + mExpectedEvilTotal +
                    " but actual evil player total is " + actualEvilTotal);
        }
    }

    private void addGeneralGoodSelectionRules()
    {
        showNewToast("Loyal cannot be manually selected or deselected.");
    }

    private void addOberonSelectionRules()
    {
        if (mCharacterImageButtonArray[CharacterName.OBERON].isChecked())
        {
            mCharacterImageButtonArray[CharacterName.OBERON].uncheck();
            searchAndCheckNewCharacters(CharacterName.MINION0, CharacterName.MINION3, 1);
        }
        else
        {
            searchAndUncheckOldCharacters(CharacterName.MINION3, CharacterName.MORGANA, 1);
            mCharacterImageButtonArray[CharacterName.OBERON].check();
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addOberonSelectionRules(): " +
                    "expected good player total is " + mExpectedGoodTotal +
                    " but actual good player total is " + actualGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addOberonSelectionRules(): " +
                    "expected evil player total is " + mExpectedEvilTotal +
                    " but actual evil player total is " + actualEvilTotal);
        }
    }

    private void addGeneralEvilSelectionRules()
    {
        showNewToast("Minions cannot be manually selected or deselected.");
    }

    /**
     * Find the first LOYAL who is VISIBLE and not checked. Then, check him/her.
     */
    private void checkNewLoyal()
    {
        searchAndCheckNewCharacters(CharacterName.LOYAL0, CharacterName.LOYAL5, 1);
    }

    /**
     * Find the first MINION who is VISIBLE and not checked. Then, check him/her.
     */
    private void checkNewMinion()
    {
        searchAndCheckNewCharacters(CharacterName.MINION0, CharacterName.MINION3, 1);
    }

    /**
     * Find the first X characters between startIdx and endIdx who are VISIBLE and not checked. Then, check them.
     * Pre-condition: startIdx <= endIdx
     */
    private void searchAndCheckNewCharacters(final int startIdx, final int endIdx, final int X)
    {
        if (startIdx > endIdx)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::searchAndCheckNewCharacter(): " +
                    "startIdx must be <= endIdx");
        }

        if (X < 0)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::searchAndCheckNewCharacter(): " +
                    "X must be >= 0");
        }

        int currIdx = startIdx;
        int charactersToCheck = X;
        while ( (currIdx <= endIdx) &&
                (mCharacterImageButtonArray[currIdx].getVisibility() == View.VISIBLE) &&
                (charactersToCheck > 0) )
        {
            if (!mCharacterImageButtonArray[currIdx].isChecked())
            {
                mCharacterImageButtonArray[currIdx].check();
                --charactersToCheck;
            }

            ++currIdx;
        }
    }

    /**
     * Find the last LOYAL who is VISIBLE and checked. Then, uncheck him/her.
     */
    private void uncheckOldLoyal()
    {
        searchAndUncheckOldCharacters(CharacterName.LOYAL5, CharacterName.LOYAL0, 1);
    }

    /**
     * Find the last MINION who is VISIBLE and checked. Then, uncheck him/her.
     */
    private void uncheckOldMinion()
    {
        searchAndUncheckOldCharacters(CharacterName.MINION3, CharacterName.MINION0, 1);
    }

    /**
     * Find the last X characters between startIdx and endIdx who are VISIBLE and checked. Then, uncheck them.
     * Pre-condition: startIdx >= endIdx
     */
    private void searchAndUncheckOldCharacters(final int startIdx, final int endIdx, final int X)
    {
        if (startIdx < endIdx)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::searchAndUncheckOldCharacter(): " +
                "startIdx must be >= endIdx");
        }

        if (X < 0)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::searchAndUncheckOldCharacter(): " +
                    "X must be >= 0");
        }

        int currIdx = startIdx;
        int charactersToUncheck = X;
        while ((currIdx >= endIdx) && (charactersToUncheck > 0))
        {
            if (mCharacterImageButtonArray[currIdx].getVisibility() == View.INVISIBLE)
            {
                --currIdx;
                continue;
            }

            if (mCharacterImageButtonArray[currIdx].isChecked())
            {
                mCharacterImageButtonArray[currIdx].uncheck();
                --charactersToUncheck;
            }

            --currIdx;
        }
    }

    public int getActualGoodTotal()
    {
        int actualGoodTotal = 0;
        for (int idx = CharacterName.MERLIN; idx <= CharacterName.LOYAL5; ++idx)
        {
            if (mCharacterImageButtonArray[idx].isChecked())
            {
                ++actualGoodTotal;
            }
        }

        return actualGoodTotal;
    }

    public int getActualEvilTotal()
    {
        int actualEvilTotal = 0;
        for (int idx = CharacterName.ASSASSIN; idx <= CharacterName.MINION3; ++idx)
        {
            if (mCharacterImageButtonArray[idx].isChecked())
            {
                ++actualEvilTotal;
            }
        }

        return actualEvilTotal;
    }

    private void showNewToast(final String message)
    {
        if (mToast != null)
        {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public CheckableObserverImageButton[] getCharacterImageButtonArray()
    {
        return mCharacterImageButtonArray;
    }

    private void navigateToSecretHitlerCharacterSelectionActivity(@NotNull View view)
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putInt(getString(R.string.last_selected_game_key), Constants.GAME_SECRETHITLER);
        sharedPrefEditor.apply();

        Intent intent = new Intent(view.getContext(), SecretHitlerCharacterSelectionActivity.class);
        finish();
        view.getContext().startActivity(intent);
    }

    private void navigateToPlayIntroductionActivity(@NotNull View view)
    {
        ArrayList<Integer> introSegmentArrayList = new ArrayList<>();

        introSegmentArrayList.add(R.raw.introsegment0);

        introSegmentArrayList.add(mCharacterImageButtonArray[CharacterName.OBERON].isChecked() ?
            R.raw.introsegment1withoberon :
            R.raw.introsegment1nooberon);

        introSegmentArrayList.add(R.raw.introsegment2);

        if (mCharacterImageButtonArray[CharacterName.MERLIN].isChecked())
        {
            introSegmentArrayList.add(mCharacterImageButtonArray[CharacterName.MORDRED].isChecked() ?
                R.raw.introsegment3withmordred :
                R.raw.introsegment3nomordred);

            introSegmentArrayList.add(R.raw.introsegment4);

            if (mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked())
            {
                introSegmentArrayList.add(mCharacterImageButtonArray[CharacterName.MORGANA].isChecked() ?
                    R.raw.introsegment5withpercivalwithmorgana :
                    R.raw.introsegment5withpercivalnomorgana);

                introSegmentArrayList.add(mCharacterImageButtonArray[CharacterName.MORGANA].isChecked() ?
                    R.raw.introsegment6withpercivalwithmorgana :
                    R.raw.introsegment6withpercivalnomorgana);

                introSegmentArrayList.add(R.raw.introsegment7);
            }
            else
            {
                introSegmentArrayList.add(R.raw.introsegment5nopercival);
            }
        }
        else
        {
            introSegmentArrayList.add(R.raw.introsegment3nomerlin);
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

    private void navigateToSettingsHomeActivity(@NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), SettingsHomeActivity.class);
        intent.putExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        intent.putExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        intent.putExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        intent.putExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        startActivityForResult(intent, Constants.REQUEST_SETTINGS_HOME);
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private void savePreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        sharedPrefEditor.putInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        sharedPrefEditor.putFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        sharedPrefEditor.putFloat(getString(R.string.narration_volume_key), mNarrationVolume);
        sharedPrefEditor.putInt(getString(R.string.good_player_number_key), mExpectedGoodTotal);
        sharedPrefEditor.putInt(getString(R.string.evil_player_number_key), mExpectedEvilTotal);

        if (mCharacterImageButtonArray[CharacterName.MERLIN].isChecked() != mCharacterImageButtonArray[CharacterName.ASSASSIN].isChecked())
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::savePreferences(): " +
                    "Checked state of Assassin should be identical to that of Merlin");
        }

        sharedPrefEditor.putBoolean(getString(R.string.is_merlin_checked_key), mCharacterImageButtonArray[CharacterName.MERLIN].isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_percival_checked_key), mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_morgana_checked_key), mCharacterImageButtonArray[CharacterName.MORGANA].isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_mordred_checked_key), mCharacterImageButtonArray[CharacterName.MORDRED].isChecked());
        sharedPrefEditor.putBoolean(getString(R.string.is_oberon_checked_key), mCharacterImageButtonArray[CharacterName.OBERON].isChecked());
        sharedPrefEditor.apply();
    }

    private void loadPreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);

        // Avalon is the default game but another game might have been the last selected
        int lastSelectedGame = sharedPref.getInt(getString(R.string.last_selected_game_key), Constants.GAME_AVALON);
        if (lastSelectedGame != Constants.GAME_AVALON)  // if last selected game is not the default game, then switch game
        {
            CustomTypefaceableObserverButton gameSwitcherButton = findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
            gameSwitcherButton.performClick();
        }

        mPauseDurationInMilliSecs = sharedPref.getLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mBackgroundSoundRawResId = sharedPref.getInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        mBackgroundSoundVolume = sharedPref.getFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mNarrationVolume = sharedPref.getFloat(getString(R.string.narration_volume_key), mNarrationVolume);

        if (mCharacterImageButtonArray == null)
        {
            return;
        }

        int expectedGoodTotal = sharedPref.getInt(getString(R.string.good_player_number_key), mExpectedGoodTotal);
        int expectedEvilTotal = sharedPref.getInt(getString(R.string.evil_player_number_key), mExpectedEvilTotal);
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
                    "CharacterSelectionActivity::loadPreferences(): " +
                        "Invalid no of players: " + (expectedGoodTotal + expectedEvilTotal));
        }

        // default: Merlin is checked
        // saved preferences: if Merlin is not checked
        if ( (!isMerlinChecked) && (mCharacterImageButtonArray[CharacterName.MERLIN].isChecked()) )
        {
            mCharacterImageButtonArray[CharacterName.MERLIN].performClick();
        }

        // default: Percival is not checked
        // saved preferences: if Percival is checked
        if ( (isPercivalChecked) && (!mCharacterImageButtonArray[CharacterName.PERCIVAL].isChecked()) )
        {
            mCharacterImageButtonArray[CharacterName.PERCIVAL].performClick();
        }

        // default: Morgana is not checked
        // saved preferences: if Morgana is checked
        if ( (isMorganaChecked) && (!mCharacterImageButtonArray[CharacterName.MORGANA].isChecked()) )
        {
            mCharacterImageButtonArray[CharacterName.MORGANA].performClick();
        }

        // default: Mordred is not checked
        // saved preferences: if Mordred is checked
        if ( (isMordredChecked) && (!mCharacterImageButtonArray[CharacterName.MORDRED].isChecked()) )
        {
            mCharacterImageButtonArray[CharacterName.MORDRED].performClick();
        }

        // default: Oberon is not checked
        // saved preferences: if Oberon is checked
        if ( (isOberonChecked) && (!mCharacterImageButtonArray[CharacterName.OBERON].isChecked()) )
        {
            mCharacterImageButtonArray[CharacterName.OBERON].performClick();
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::loadPreferences(): " +
                    "expected good player total is " + mExpectedGoodTotal +
                    " but actual good player total is " + actualGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::loadPreferences(): " +
                    "expected evil player total is " + mExpectedEvilTotal +
                    " but actual evil player total is " + actualEvilTotal);
        }

        if (mCharacterImageButtonArray[CharacterName.MERLIN].isChecked() != mCharacterImageButtonArray[CharacterName.ASSASSIN].isChecked())
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::loadPreferences(): " +
                    "Checked state of Assassin should be identical to that of Merlin");
        }
    }

    private SoundPool mGeneralSoundPool;
    private int mClickSoundId;

    private MediaPlayer mGeneralMediaPlayer;
    private int mGeneralMediaPlayerCurrentLength;

    private CheckableObserverImageButton[] mCharacterImageButtonArray;
    private int mExpectedGoodTotal = 3;
    private int mExpectedEvilTotal = 2;

    private Toast mToast;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;
}