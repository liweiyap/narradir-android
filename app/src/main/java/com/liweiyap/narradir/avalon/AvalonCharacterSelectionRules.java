package com.liweiyap.narradir.avalon;

import android.view.View;

import androidx.annotation.NonNull;

import com.liweiyap.narradir.ui.CheckableObserverImageButton;

class AvalonCharacterSelectionRules
{
    public AvalonCharacterSelectionRules(
        @NonNull final CheckableObserverImageButton merlinButton,
        @NonNull final CheckableObserverImageButton percivalButton,
        @NonNull final CheckableObserverImageButton loyal0Button,
        @NonNull final CheckableObserverImageButton loyal1Button,
        @NonNull final CheckableObserverImageButton loyal2Button,
        @NonNull final CheckableObserverImageButton loyal3Button,
        @NonNull final CheckableObserverImageButton loyal4Button,
        @NonNull final CheckableObserverImageButton loyal5Button,
        @NonNull final CheckableObserverImageButton assassinButton,
        @NonNull final CheckableObserverImageButton morganaButton,
        @NonNull final CheckableObserverImageButton mordredButton,
        @NonNull final CheckableObserverImageButton oberonButton,
        @NonNull final CheckableObserverImageButton minion0Button,
        @NonNull final CheckableObserverImageButton minion1Button,
        @NonNull final CheckableObserverImageButton minion2Button,
        @NonNull final CheckableObserverImageButton minion3Button)
    {
        mCharacterImageButtonArray[AvalonCharacterName.MERLIN] = merlinButton;
        mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL] = percivalButton;
        mCharacterImageButtonArray[AvalonCharacterName.LOYAL0] = loyal0Button;
        mCharacterImageButtonArray[AvalonCharacterName.LOYAL1] = loyal1Button;
        mCharacterImageButtonArray[AvalonCharacterName.LOYAL2] = loyal2Button;
        mCharacterImageButtonArray[AvalonCharacterName.LOYAL3] = loyal3Button;
        mCharacterImageButtonArray[AvalonCharacterName.LOYAL4] = loyal4Button;
        mCharacterImageButtonArray[AvalonCharacterName.LOYAL5] = loyal5Button;
        mCharacterImageButtonArray[AvalonCharacterName.ASSASSIN] = assassinButton;
        mCharacterImageButtonArray[AvalonCharacterName.MORGANA] = morganaButton;
        mCharacterImageButtonArray[AvalonCharacterName.MORDRED] = mordredButton;
        mCharacterImageButtonArray[AvalonCharacterName.OBERON] = oberonButton;
        mCharacterImageButtonArray[AvalonCharacterName.MINION0] = minion0Button;
        mCharacterImageButtonArray[AvalonCharacterName.MINION1] = minion1Button;
        mCharacterImageButtonArray[AvalonCharacterName.MINION2] = minion2Button;
        mCharacterImageButtonArray[AvalonCharacterName.MINION3] = minion3Button;

        mCharacterImageButtonArray[AvalonCharacterName.MERLIN].addOnClickObserver(this::runMerlinSelectionRules);
        mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].addOnClickObserver(this::runPercivalSelectionRules);
        mCharacterImageButtonArray[AvalonCharacterName.ASSASSIN].addOnClickObserver(this::runMerlinSelectionRules);
        mCharacterImageButtonArray[AvalonCharacterName.MORGANA].addOnClickObserver(this::runMorganaSelectionRules);
        mCharacterImageButtonArray[AvalonCharacterName.MORDRED].addOnClickObserver(this::runMordredSelectionRules);
        mCharacterImageButtonArray[AvalonCharacterName.OBERON].addOnClickObserver(this::runOberonSelectionRules);
    }

    public CheckableObserverImageButton getCharacter(final int idx)
    {
        if ( (idx < AvalonCharacterName.MERLIN) || (idx > AvalonCharacterName.MINION3) )
        {
            throw new RuntimeException("AvalonCharacterSelectionRules::getCharacter(): Invalid index " + idx);
        }

        return mCharacterImageButtonArray[idx];
    }

    public CheckableObserverImageButton[] getCharacterImageButtonArray()
    {
        return mCharacterImageButtonArray;
    }

    /**
     * If Merlin is selected, then Assassin is auto-selected, and vice versa.
     * In addition, one of the LOYAL is auto-deselected, and one of the MINIONS is auto-deselected.
     *
     * If Merlin is deselected, then Assassin is auto-deselected, and vice versa.
     * In addition, one of the LOYAL is auto-selected, and one of the MINIONS is auto-selected.
     * If Percival, Morgana, or Mordred are already selected, they should be replaced by MINIONS.
     */
    private void runMerlinSelectionRules()
    {
        if (mCharacterImageButtonArray[AvalonCharacterName.MERLIN].isChecked())
        {
            if (mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].isChecked())
            {
                mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].performClick();
            }

            if (mCharacterImageButtonArray[AvalonCharacterName.MORDRED].isChecked())
            {
                mCharacterImageButtonArray[AvalonCharacterName.MORDRED].performClick();
            }

            mCharacterImageButtonArray[AvalonCharacterName.MERLIN].uncheck();
            mCharacterImageButtonArray[AvalonCharacterName.ASSASSIN].uncheck();
            checkNewLoyal();
            checkNewMinion();
        }
        else
        {
            mCharacterImageButtonArray[AvalonCharacterName.MERLIN].check();
            mCharacterImageButtonArray[AvalonCharacterName.ASSASSIN].check();
            uncheckOldLoyal();
            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.OBERON, 1);
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::addMerlinSelectionRules()");
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
    private void runPercivalSelectionRules()
    {
        if (mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].isChecked())
        {
            mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].uncheck();
            checkNewLoyal();

            if (mCharacterImageButtonArray[AvalonCharacterName.MORGANA].isChecked())
            {
                checkNewMinion();
            }
            mCharacterImageButtonArray[AvalonCharacterName.MORGANA].uncheck();
        }
        else
        {
            mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].check();
            uncheckOldLoyal();

            if (!mCharacterImageButtonArray[AvalonCharacterName.MERLIN].isChecked())
            {
                mCharacterImageButtonArray[AvalonCharacterName.MERLIN].performClick();
            }

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (!mCharacterImageButtonArray[AvalonCharacterName.MORDRED].isChecked()) )
            {
                if (!mCharacterImageButtonArray[AvalonCharacterName.MORGANA].isChecked())
                {
                    searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORDRED, 1);
                }
                mCharacterImageButtonArray[AvalonCharacterName.MORGANA].check();
            }
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runPercivalSelectionRules()");
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
    private void runMorganaSelectionRules()
    {
        if (mCharacterImageButtonArray[AvalonCharacterName.MORGANA].isChecked())
        {
            if (!mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].isChecked())
            {
                throw new RuntimeException(
                    "AvalonCharacterSelectionRules::runMorganaSelectionRules(): " +
                        "Morgana was active in the absence of Percival");
            }

            mCharacterImageButtonArray[AvalonCharacterName.MORGANA].uncheck();

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].isChecked()) )
            {
                if (mCharacterImageButtonArray[AvalonCharacterName.MORDRED].isChecked())
                {
                    checkNewMinion();
                }
                mCharacterImageButtonArray[AvalonCharacterName.MORDRED].check();
            }
            else
            {
                checkNewMinion();
            }
        }
        else
        {
            mCharacterImageButtonArray[AvalonCharacterName.MORGANA].check();
            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORDRED, 1);

            if (!mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].isChecked())
            {
                mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].performClick();
            }
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runMorganaSelectionRules()");
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
    private void runMordredSelectionRules()
    {
        if (mCharacterImageButtonArray[AvalonCharacterName.MORDRED].isChecked())
        {
            if (!mCharacterImageButtonArray[AvalonCharacterName.MERLIN].isChecked())
            {
                throw new RuntimeException(
                    "AvalonCharacterSelectionRules::runMordredSelectionRules(): " +
                        "Mordred was active in the absence of Merlin");
            }

            mCharacterImageButtonArray[AvalonCharacterName.MORDRED].uncheck();

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].isChecked()) )
            {
                if (mCharacterImageButtonArray[AvalonCharacterName.MORGANA].isChecked())
                {
                    checkNewMinion();
                }
                mCharacterImageButtonArray[AvalonCharacterName.MORGANA].check();
            }
            else
            {
                checkNewMinion();
            }
        }
        else
        {
            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORGANA, 1);
            mCharacterImageButtonArray[AvalonCharacterName.MORDRED].check();

            if (!mCharacterImageButtonArray[AvalonCharacterName.MERLIN].isChecked())
            {
                mCharacterImageButtonArray[AvalonCharacterName.MERLIN].performClick();
            }

            if ( (mExpectedGoodTotal + mExpectedEvilTotal == 5) &&
                 (!mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].isChecked()) )
            {
                mCharacterImageButtonArray[AvalonCharacterName.PERCIVAL].performClick();
            }
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runMordredSelectionRules()");
    }

    private void runOberonSelectionRules()
    {
        if (mCharacterImageButtonArray[AvalonCharacterName.OBERON].isChecked())
        {
            mCharacterImageButtonArray[AvalonCharacterName.OBERON].uncheck();
            searchAndCheckNewCharacters(AvalonCharacterName.MINION0, AvalonCharacterName.MINION3, 1);
        }
        else
        {
            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORGANA, 1);
            mCharacterImageButtonArray[AvalonCharacterName.OBERON].check();
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runOberonSelectionRules()");
    }

    /**
     * Find the first LOYAL who is VISIBLE and not checked. Then, check him/her.
     */
    private void checkNewLoyal()
    {
        searchAndCheckNewCharacters(AvalonCharacterName.LOYAL0, AvalonCharacterName.LOYAL5, 1);
    }

    /**
     * Find the first MINION who is VISIBLE and not checked. Then, check him/her.
     */
    private void checkNewMinion()
    {
        searchAndCheckNewCharacters(AvalonCharacterName.MINION0, AvalonCharacterName.MINION3, 1);
    }

    /**
     * Find the first X characters between startIdx and endIdx who are VISIBLE and not checked. Then, check them.
     * Pre-condition: startIdx <= endIdx
     */
    public void searchAndCheckNewCharacters(final int startIdx, final int endIdx, final int X)
    {
        if (startIdx > endIdx)
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionRules::searchAndCheckNewCharacters(): " +
                    "startIdx must be <= endIdx");
        }

        if (X < 0)
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionRules::searchAndCheckNewCharacters(): " +
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
        searchAndUncheckOldCharacters(AvalonCharacterName.LOYAL5, AvalonCharacterName.LOYAL0, 1);
    }

    /**
     * Find the last MINION who is VISIBLE and checked. Then, uncheck him/her.
     */
    private void uncheckOldMinion()
    {
        searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MINION0, 1);
    }

    /**
     * Find the last X characters between startIdx and endIdx who are VISIBLE and checked. Then, uncheck them.
     * Pre-condition: startIdx >= endIdx
     */
    public void searchAndUncheckOldCharacters(final int startIdx, final int endIdx, final int X)
    {
        if (startIdx < endIdx)
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionRules::searchAndUncheckOldCharacters(): " +
                    "startIdx must be >= endIdx");
        }

        if (X < 0)
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionRules::searchAndUncheckOldCharacters(): " +
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
        for (int idx = AvalonCharacterName.MERLIN; idx <= AvalonCharacterName.LOYAL5; ++idx)
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
        for (int idx = AvalonCharacterName.ASSASSIN; idx <= AvalonCharacterName.MINION3; ++idx)
        {
            if (mCharacterImageButtonArray[idx].isChecked())
            {
                ++actualEvilTotal;
            }
        }

        return actualEvilTotal;
    }

    public int getExpectedGoodTotal()
    {
        return mExpectedGoodTotal;
    }

    public int getExpectedEvilTotal()
    {
        return mExpectedEvilTotal;
    }

    public void setExpectedGoodTotal(final int newExpectedGoodTotal)
    {
        mExpectedGoodTotal = newExpectedGoodTotal;
    }

    public void setExpectedEvilTotal(final int newExpectedEvilTotal)
    {
        mExpectedEvilTotal = newExpectedEvilTotal;
    }

    public void checkPlayerComposition(final String callingFuncName)
    {
        if (callingFuncName == null)
        {
            return;
        }

        int actualGoodTotal = getActualGoodTotal();
        if (actualGoodTotal != mExpectedGoodTotal)
        {
            throw new RuntimeException(
                callingFuncName +
                    ": expected good player total is " + mExpectedGoodTotal +
                    " but actual good player total is " + actualGoodTotal);
        }

        int actualEvilTotal = getActualEvilTotal();
        if (actualEvilTotal != mExpectedEvilTotal)
        {
            throw new RuntimeException(
                callingFuncName +
                    ": expected evil player total is " + mExpectedEvilTotal +
                    " but actual evil player total is " + actualEvilTotal);
        }
    }

    private final CheckableObserverImageButton[] mCharacterImageButtonArray = new CheckableObserverImageButton[AvalonCharacterName.getNumberOfCharacters()];
    private int mExpectedGoodTotal = 3;
    private int mExpectedEvilTotal = 2;
}