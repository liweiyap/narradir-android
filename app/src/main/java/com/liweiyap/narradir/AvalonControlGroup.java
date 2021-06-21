package com.liweiyap.narradir;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.util.Pair;

import com.liweiyap.narradir.utils.CheckableObserverImageButton;
import com.liweiyap.narradir.utils.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;

public class AvalonControlGroup
{
    public AvalonControlGroup(
        Context applicationContext,
        LinearLayout playerNumberSelectionLayout,
        CustomTypefaceableCheckableObserverButton p5Button,
        CustomTypefaceableCheckableObserverButton p6Button,
        CustomTypefaceableCheckableObserverButton p7Button,
        CustomTypefaceableCheckableObserverButton p8Button,
        CustomTypefaceableCheckableObserverButton p9Button,
        CustomTypefaceableCheckableObserverButton p10Button,
        CheckableObserverImageButton merlinButton,
        CheckableObserverImageButton percivalButton,
        CheckableObserverImageButton loyal0Button,
        CheckableObserverImageButton loyal1Button,
        CheckableObserverImageButton loyal2Button,
        CheckableObserverImageButton loyal3Button,
        CheckableObserverImageButton loyal4Button,
        CheckableObserverImageButton loyal5Button,
        CheckableObserverImageButton assassinButton,
        CheckableObserverImageButton morganaButton,
        CheckableObserverImageButton mordredButton,
        CheckableObserverImageButton oberonButton,
        CheckableObserverImageButton minion0Button,
        CheckableObserverImageButton minion1Button,
        CheckableObserverImageButton minion2Button,
        CheckableObserverImageButton minion3Button)
    {
        mCharacterSelectionRules = new AvalonCharacterSelectionRules(
            applicationContext,
            merlinButton, percivalButton, loyal0Button, loyal1Button,
            loyal2Button, loyal3Button, loyal4Button, loyal5Button,
            assassinButton, morganaButton, mordredButton, oberonButton,
            minion0Button, minion1Button, minion2Button, minion3Button);

        ViewGroupSingleTargetSelector.addSingleTargetSelection(playerNumberSelectionLayout);

        /* adapt available characters according to player number */

        p5Button.addOnClickObserver(() -> {
            final int playerNumberChange = 5 - (mCharacterSelectionRules.getExpectedGoodTotal() + mCharacterSelectionRules.getExpectedEvilTotal());  // new - old

            if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(5);
            }

            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).setVisibility(View.INVISIBLE);

            if (playerNumberChange > 0)  // new > old (increase)
            {
                playerNumberSelectionLayoutChecker(5);
            }
        });

        p6Button.addOnClickObserver(() -> {
            final int playerNumberChange = 6 - (mCharacterSelectionRules.getExpectedGoodTotal() + mCharacterSelectionRules.getExpectedEvilTotal());  // new - old

            if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(6);
            }

            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).setVisibility(View.INVISIBLE);

            if (playerNumberChange > 0)  // new > old (increase)
            {
                playerNumberSelectionLayoutChecker(6);
            }
        });

        p7Button.addOnClickObserver(() -> {
            final int playerNumberChange = 7 - (mCharacterSelectionRules.getExpectedGoodTotal() + mCharacterSelectionRules.getExpectedEvilTotal());  // new - old

            if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(7);
            }

            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).setVisibility(View.INVISIBLE);

            if (playerNumberChange > 0)  // new > old (increase)
            {
                playerNumberSelectionLayoutChecker(7);
            }
        });

        p8Button.addOnClickObserver(() -> {
            final int playerNumberChange = 8 - (mCharacterSelectionRules.getExpectedGoodTotal() + mCharacterSelectionRules.getExpectedEvilTotal());  // new - old

            if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(8);
            }

            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).setVisibility(View.INVISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).setVisibility(View.INVISIBLE);

            if (playerNumberChange > 0)  // new > old (increase)
            {
                playerNumberSelectionLayoutChecker(8);
            }
        });

        p9Button.addOnClickObserver(() -> {
            final int playerNumberChange = 9 - (mCharacterSelectionRules.getExpectedGoodTotal() + mCharacterSelectionRules.getExpectedEvilTotal());  // new - old

            if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(9);
            }

            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).setVisibility(View.INVISIBLE);

            if (playerNumberChange > 0)  // new > old (increase)
            {
                playerNumberSelectionLayoutChecker(9);
            }
        });

        p10Button.addOnClickObserver(() -> {
            final int playerNumberChange = 10 - (mCharacterSelectionRules.getExpectedGoodTotal() + mCharacterSelectionRules.getExpectedEvilTotal());  // new - old

            if (playerNumberChange < 0)  // new < old (decrease)
            {
                playerNumberSelectionLayoutChecker(10);
            }

            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).setVisibility(View.VISIBLE);
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).setVisibility(View.VISIBLE);

            if (playerNumberChange > 0)  // new > old (increase)
            {
                playerNumberSelectionLayoutChecker(10);
            }
        });
    }

    private void playerNumberSelectionLayoutChecker(final int newPlayerNumber)
    {
        if ((newPlayerNumber < 5) || (newPlayerNumber > 10))
        {
            throw new RuntimeException(
                "AvalonControlGroup::playerNumberSelectionLayoutChecker(): " +
                    "Invalid input " + newPlayerNumber);
        }

        Pair<Integer, Integer> newPlayerComposition = getPlayerComposition(newPlayerNumber);
        if ( (newPlayerComposition.first == null) || (newPlayerComposition.second == null) )
        {
            throw new RuntimeException(
                "AvalonControlGroup::playerNumberSelectionLayoutChecker(): getPlayerComposition() returned a pair of NULL");
        }

        int newExpectedGoodTotal = newPlayerComposition.first;
        int newExpectedEvilTotal = newPlayerComposition.second;

        int expectedGoodChange = newExpectedGoodTotal - mCharacterSelectionRules.getExpectedGoodTotal();  // new - old
        int expectedEvilChange = newExpectedEvilTotal - mCharacterSelectionRules.getExpectedEvilTotal();  // new - old

        if (expectedGoodChange > 0)  // new > old (increase)
        {
            mCharacterSelectionRules.searchAndCheckNewCharacters(AvalonCharacterName.LOYAL0, AvalonCharacterName.LOYAL5, expectedGoodChange);
        }
        else  // new < old (decrease)
        {
            mCharacterSelectionRules.searchAndUncheckOldCharacters(AvalonCharacterName.LOYAL5, AvalonCharacterName.LOYAL0, -expectedGoodChange);
        }

        if (expectedEvilChange > 0)  // new > old (increase)
        {
            mCharacterSelectionRules.searchAndCheckNewCharacters(AvalonCharacterName.MINION0, AvalonCharacterName.MINION3, expectedEvilChange);
        }
        else  // new < old (decrease)
        {
            mCharacterSelectionRules.searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORGANA, -expectedEvilChange);
        }

        // update old values to new values
        mCharacterSelectionRules.setExpectedGoodTotal(newExpectedGoodTotal);
        mCharacterSelectionRules.setExpectedEvilTotal(newExpectedEvilTotal);
    }

    private Pair<Integer, Integer> getPlayerComposition(final int playerNumber)
    {
        if ((playerNumber < 5) || (playerNumber > 10))
        {
            throw new RuntimeException(
                "AvalonControlGroup::getPlayerComposition(): " +
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

    public CheckableObserverImageButton getCharacter(final int idx)
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::getCharacter(): mCharacterSelectionRules is NULL");
        }

        return mCharacterSelectionRules.getCharacter(idx);
    }

    public CheckableObserverImageButton[] getCharacterImageButtonArray()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::getCharacterImageButtonArray(): mCharacterSelectionRules is NULL");
        }

        return mCharacterSelectionRules.getCharacterImageButtonArray();
    }

    public int getActualGoodTotal()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::getActualGoodTotal(): mCharacterSelectionRules is NULL");
        }

        return mCharacterSelectionRules.getActualGoodTotal();
    }

    public int getActualEvilTotal()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::getActualEvilTotal(): mCharacterSelectionRules is NULL");
        }

        return mCharacterSelectionRules.getActualEvilTotal();
    }

    public int getExpectedGoodTotal()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::getExpectedGoodTotal(): mCharacterSelectionRules is NULL");
        }

        return mCharacterSelectionRules.getExpectedGoodTotal();
    }

    public int getExpectedEvilTotal()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::getExpectedEvilTotal(): mCharacterSelectionRules is NULL");
        }

        return mCharacterSelectionRules.getExpectedEvilTotal();
    }

    private final AvalonCharacterSelectionRules mCharacterSelectionRules;
}