package com.liweiyap.narradir;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.liweiyap.narradir.utils.CheckableObserverImageButton;
import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;

public class CharacterSelectionActivity extends FullScreenPortraitActivity
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

        // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
        mClickSoundMediaPlayer = MediaPlayer.create(this, R.raw.clicksound);
        addSoundToPlayOnButtonClick();

        for (CheckableObserverImageButton characterImageButton : mCharacterImageButtonArray)
        {
            characterImageButton.addOnClickObserver(() -> {
                if (mGeneralMediaPlayer != null)
                {
                    mGeneralMediaPlayer.stop();
                }
            });
        }

        addSelectionRules();
        addCharacterDescriptions();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.seekTo(mGeneralMediaPlayerCurrentLength);
            mGeneralMediaPlayer.start();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.pause();
            mGeneralMediaPlayerCurrentLength = mGeneralMediaPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mClickSoundMediaPlayer.release();
        mClickSoundMediaPlayer = null;

        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.release();
            mGeneralMediaPlayer = null;
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
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            mExpectedGoodTotal = 3;
            mExpectedEvilTotal = 2;
        });

        p6Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            mExpectedGoodTotal = 4;
            mExpectedEvilTotal = 2;
        });

        p7Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            mExpectedGoodTotal = 4;
            mExpectedEvilTotal = 3;
        });

        p8Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            mExpectedGoodTotal = 5;
            mExpectedEvilTotal = 3;
        });

        p9Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.INVISIBLE);
            mExpectedGoodTotal = 6;
            mExpectedEvilTotal = 3;
        });

        p10Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION3].setVisibility(View.VISIBLE);
            mExpectedGoodTotal = 6;
            mExpectedEvilTotal = 4;
        });
    }

    private void addSoundToPlayOnButtonClick()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        for (CheckableObserverImageButton characterImageButton : mCharacterImageButtonArray)
        {
            addSoundToPlayOnButtonClick(characterImageButton);
        }
    }

    private void addSoundToPlayOnButtonClick(ObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            if (mClickSoundMediaPlayer != null)
            {
                mClickSoundMediaPlayer.start();
            }
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

        mCharacterImageButtonArray[CharacterName.LOYAL0].addOnClickObserver(() -> addGeneralGoodSelectionRules(CharacterName.LOYAL0));

        mCharacterImageButtonArray[CharacterName.LOYAL1].addOnClickObserver(() -> addGeneralGoodSelectionRules(CharacterName.LOYAL1));

        mCharacterImageButtonArray[CharacterName.LOYAL2].addOnClickObserver(() -> addGeneralGoodSelectionRules(CharacterName.LOYAL2));

        mCharacterImageButtonArray[CharacterName.LOYAL3].addOnClickObserver(() -> addGeneralGoodSelectionRules(CharacterName.LOYAL3));

        mCharacterImageButtonArray[CharacterName.LOYAL4].addOnClickObserver(() -> addGeneralGoodSelectionRules(CharacterName.LOYAL4));

        mCharacterImageButtonArray[CharacterName.LOYAL5].addOnClickObserver(() -> addGeneralGoodSelectionRules(CharacterName.LOYAL5));

        mCharacterImageButtonArray[CharacterName.ASSASSIN].addOnClickObserver(this::addMerlinSelectionRules);

        mCharacterImageButtonArray[CharacterName.MORGANA].addOnClickObserver(this::addMorganaSelectionRules);

        mCharacterImageButtonArray[CharacterName.MORDRED].addOnClickObserver(this::addMordredSelectionRules);

        mCharacterImageButtonArray[CharacterName.OBERON].addOnClickObserver(() -> addGeneralEvilSelectionRules(CharacterName.OBERON));

        mCharacterImageButtonArray[CharacterName.MINION0].addOnClickObserver(() -> addGeneralEvilSelectionRules(CharacterName.MINION0));

        mCharacterImageButtonArray[CharacterName.MINION1].addOnClickObserver(() -> addGeneralEvilSelectionRules(CharacterName.MINION1));

        mCharacterImageButtonArray[CharacterName.MINION2].addOnClickObserver(() -> addGeneralEvilSelectionRules(CharacterName.MINION2));

        mCharacterImageButtonArray[CharacterName.MINION3].addOnClickObserver(() -> addGeneralEvilSelectionRules(CharacterName.MINION3));
    }

    /**
     * If Merlin is selected, then Assassin is auto-selected, and vice versa.
     * In addition, one of the LOYAL is auto-unselected, and one of the MINIONS is auto-unselected.
     *
     * If Merlin is unselected, then Assassin is auto-unselected, and vice versa.
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
            uncheckOldMinion();
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMerlinSelectionRules(): " +
                    "expected good player total is " + actualGoodTotal +
                    " but actual good player total is " + mExpectedGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMerlinSelectionRules(): " +
                    "expected evil player total is " + actualEvilTotal +
                    " but actual evil player total is " + mExpectedEvilTotal);
        }
    }

    /**
     * If Percival is selected:
     *  - If Merlin is not already selected, then Merlin is auto-selected.
     *  - Morgana is auto-selected.
     *     - If, prior to this, Morgana was not already selected, then one of the Evil players is auto-unselected.
     *  - One of the LOYAL is auto-unselected.
     *
     * If Percival is unselected:
     *  - Morgana is auto-unselected.
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

            if (!mCharacterImageButtonArray[CharacterName.MORDRED].isChecked())
            {
                if (!mCharacterImageButtonArray[CharacterName.MORGANA].isChecked())
                {
                    searchAndUncheckOldCharacter(CharacterName.MINION3, CharacterName.MORDRED);
                }
                mCharacterImageButtonArray[CharacterName.MORGANA].check();
            }
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addPercivalSelectionRules(): " +
                    "expected good player total is " + actualGoodTotal +
                    " but actual good player total is " + mExpectedGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addPercivalSelectionRules(): " +
                    "expected evil player total is " + actualEvilTotal +
                    " but actual evil player total is " + mExpectedEvilTotal);
        }
    }

    /**
     * TRIANGLE amongst Percival, Morgana, and Mordred:
     * You can have Percival without Morgana but you cannot have Morgana without Percival.
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * You can have Percival without Mordred and you can also have Mordred without Percival.
     *
     * If Morgana is selected:
     *  - If Percival is not already selected, then Percival is auto-selected and one of the LOYAL is auto-unselected.
     *  - One of the Evil players is auto-unselected.
     * If Morgana is unselected:
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
            searchAndUncheckOldCharacter(CharacterName.MINION3, CharacterName.MORDRED);

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
                    "expected good player total is " + actualGoodTotal +
                    " but actual good player total is " + mExpectedGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMorganaSelectionRules(): " +
                    "expected evil player total is " + actualEvilTotal +
                    " but actual evil player total is " + mExpectedEvilTotal);
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
     *  - If Merlin is not already selected, then Merlin is auto-selected and one of the LOYAL is auto-unselected.
     *  - In a 5-player game, if Percival is not already selected, then Percival is auto-selected.
     *  - One of the Evil players is auto-unselected.
     * If Mordred is unselected:
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
            searchAndUncheckOldCharacter(CharacterName.MINION3, CharacterName.MORGANA);
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
                    "expected good player total is " + actualGoodTotal +
                    " but actual good player total is " + mExpectedGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addMordredSelectionRules(): " +
                    "expected evil player total is " + actualEvilTotal +
                    " but actual evil player total is " + mExpectedEvilTotal);
        }
    }

    private void addGeneralGoodSelectionRules(final int idx)
    {
        if ((idx <= CharacterName.PERCIVAL) || (idx >= CharacterName.ASSASSIN))
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addGeneralGoodSelectionRules(): " +
                    "Invalid index " + idx);
        }

        if (mCharacterImageButtonArray[idx].isChecked())
        {
            mCharacterImageButtonArray[idx].uncheck();
            checkNewLoyal();
        }
        else
        {
            uncheckOldLoyal();
            mCharacterImageButtonArray[idx].check();
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addGeneralGoodSelectionRules(): " +
                    "expected good player total is " + actualGoodTotal +
                    " but actual good player total is " + mExpectedGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addGeneralGoodSelectionRules(): " +
                    "expected evil player total is " + actualEvilTotal +
                    " but actual evil player total is " + mExpectedEvilTotal);
        }
    }

    private void addGeneralEvilSelectionRules(final int idx)
    {
        if (idx < CharacterName.OBERON)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addGeneralEvilSelectionRules(): " +
                    "Invalid index " + idx);
        }

        if (mCharacterImageButtonArray[idx].isChecked())
        {
            mCharacterImageButtonArray[idx].uncheck();
            searchAndCheckNewCharacter(CharacterName.MINION0, CharacterName.MINION3);
        }
        else
        {
            searchAndUncheckOldCharacter(CharacterName.MINION3, CharacterName.MORGANA);
            mCharacterImageButtonArray[idx].check();
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addGeneralEvilSelectionRules(): " +
                    "expected good player total is " + actualGoodTotal +
                    " but actual good player total is " + mExpectedGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::addGeneralEvilSelectionRules(): " +
                    "expected evil player total is " + actualEvilTotal +
                    " but actual evil player total is " + mExpectedEvilTotal);
        }
    }

    /**
     * Find the first LOYAL who is VISIBLE and not checked. Then, check him/her.
     */
    private void checkNewLoyal()
    {
        searchAndCheckNewCharacter(CharacterName.LOYAL0, CharacterName.LOYAL5);
    }

    /**
     * Find the first MINION who is VISIBLE and not checked. Then, check him/her.
     */
    private void checkNewMinion()
    {
        searchAndCheckNewCharacter(CharacterName.MINION0, CharacterName.MINION3);
    }

    /**
     * Find the first character between startIdx and endIdx who is VISIBLE and not checked. Then, check him/her.
     * Pre-condition: startIdx <= endIdx
     */
    private void searchAndCheckNewCharacter(final int startIdx, final int endIdx)
    {
        if (startIdx > endIdx)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::searchAndCheckNewCharacter(): " +
                    "startIdx must be <= endIdx");
        }

        int currIdx = startIdx;
        while (currIdx <= endIdx && mCharacterImageButtonArray[currIdx].getVisibility() == View.VISIBLE)
        {
            if (!mCharacterImageButtonArray[currIdx].isChecked())
            {
                mCharacterImageButtonArray[currIdx].check();
                break;
            }

            ++currIdx;
        }
    }

    /**
     * Find the last LOYAL who is VISIBLE and checked. Then, uncheck him/her.
     */
    private void uncheckOldLoyal()
    {
        searchAndUncheckOldCharacter(CharacterName.LOYAL5, CharacterName.LOYAL0);
    }

    /**
     * Find the last MINION who is VISIBLE and checked. Then, uncheck him/her.
     */
    private void uncheckOldMinion()
    {
        searchAndUncheckOldCharacter(CharacterName.MINION3, CharacterName.MINION0);
    }

    /**
     * Find the last character between startIdx and endIdx who is VISIBLE and checked. Then, uncheck him/her.
     * Pre-condition: startIdx >= endIdx
     */
    private void searchAndUncheckOldCharacter(final int startIdx, final int endIdx)
    {
        if (startIdx < endIdx)
        {
            throw new RuntimeException(
                "CharacterSelectionActivity::searchAndUncheckOldCharacter(): " +
                "startIdx must be >= endIdx");
        }

        int currIdx = startIdx;
        while (currIdx >= endIdx)
        {
            if (mCharacterImageButtonArray[currIdx].getVisibility() == View.INVISIBLE)
            {
                --currIdx;
                continue;
            }

            if (mCharacterImageButtonArray[currIdx].isChecked())
            {
                mCharacterImageButtonArray[currIdx].uncheck();
                break;
            }

            --currIdx;
        }
    }

    private int getActualGoodTotal()
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

    private int getActualEvilTotal()
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

    private MediaPlayer mClickSoundMediaPlayer;
    private MediaPlayer mGeneralMediaPlayer;
    private int mGeneralMediaPlayerCurrentLength;

    private CheckableObserverImageButton[] mCharacterImageButtonArray;
    private int mExpectedGoodTotal = 3;
    private int mExpectedEvilTotal = 2;
}