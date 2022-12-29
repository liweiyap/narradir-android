package com.liweiyap.narradir.avalon;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.liweiyap.narradir.R;
import com.liweiyap.narradir.ui.CheckableObserverImageButton;
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.util.CharacterSelectionControlGroup;
import com.liweiyap.narradir.util.PlayerNumbers;

public class AvalonControlGroup extends CharacterSelectionControlGroup
{
    public AvalonControlGroup(
        @NonNull final Context activityContext,
        @NonNull final LinearLayout playerNumberSelectionLayout,
        @NonNull final CustomTypefaceableCheckableObserverButton p5Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p6Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p7Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p8Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p9Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p10Button,
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
        super(activityContext);

        mCharacterSelectionRules = new AvalonCharacterSelectionRules(
            merlinButton, percivalButton, loyal0Button, loyal1Button,
            loyal2Button, loyal3Button, loyal4Button, loyal5Button,
            assassinButton, morganaButton, mordredButton, oberonButton,
            minion0Button, minion1Button, minion2Button, minion3Button);

        addSnackbarMessages();

        ViewGroupSingleTargetSelector.addSingleTargetSelection(playerNumberSelectionLayout);

        // -----------------------------------------------------------------------------------------
        // adapt available characters according to player number
        // -----------------------------------------------------------------------------------------

        mPlayerNumberButtonArray[PlayerNumbers.P5] = p5Button;
        mPlayerNumberButtonArray[PlayerNumbers.P6] = p6Button;
        mPlayerNumberButtonArray[PlayerNumbers.P7] = p7Button;
        mPlayerNumberButtonArray[PlayerNumbers.P8] = p8Button;
        mPlayerNumberButtonArray[PlayerNumbers.P9] = p9Button;
        mPlayerNumberButtonArray[PlayerNumbers.P10] = p10Button;

        mPlayerNumberButtonArray[PlayerNumbers.P5].addOnClickObserver(() -> {
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

        mPlayerNumberButtonArray[PlayerNumbers.P6].addOnClickObserver(() -> {
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

        mPlayerNumberButtonArray[PlayerNumbers.P7].addOnClickObserver(() -> {
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

        mPlayerNumberButtonArray[PlayerNumbers.P8].addOnClickObserver(() -> {
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

        mPlayerNumberButtonArray[PlayerNumbers.P9].addOnClickObserver(() -> {
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

        mPlayerNumberButtonArray[PlayerNumbers.P10].addOnClickObserver(() -> {
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

        // -----------------------------------------------------------------------------------------
        // MediaPlayer for character descriptions
        // -----------------------------------------------------------------------------------------

        addCharacterDescriptions();
    }

    public void destroy()
    {
        super.destroy();

        if (mCharacterSelectionRules != null)
        {
            mCharacterSelectionRules.destroy();
            mCharacterSelectionRules = null;
        }
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

        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException(
                "AvalonControlGroup::playerNumberSelectionLayoutChecker(): mCharacterSelectionRules is NULL");
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

        // callback for externally-driven selection rules
        mCharacterSelectionRules.onPlayerNumberChange();
    }

    @NonNull
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

    private void addCharacterDescriptions()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::addCharacterDescriptions(): mCharacterSelectionRules is NULL");
        }

        if (mActivityContext == null)
        {
            return;
        }

        try
        {
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MERLIN).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.merlindescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.PERCIVAL).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.percivaldescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.loyaldescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.loyaldescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.loyaldescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.loyaldescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.loyaldescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.loyaldescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.ASSASSIN).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.assassindescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MORGANA).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.morganadescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MORDRED).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.mordreddescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.OBERON).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.oberondescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.miniondescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.miniondescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.miniondescription_key)));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.miniondescription_key)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addSnackbarMessages()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::addSnackbarMessages(): mCharacterSelectionRules is NULL");
        }

        if (mActivityContext == null)
        {
            return;
        }

        for (int idx = AvalonCharacterName.LOYAL0; idx <= AvalonCharacterName.LOYAL5; ++idx)
        {
            CheckableObserverImageButton character = mCharacterSelectionRules.getCharacter(idx);
            if (character != null)
            {
                character.addOnClickObserver(() -> showSnackbar(mActivityContext.getString(R.string.avalon_loyal_notification)));
            }
        }

        for (int idx = AvalonCharacterName.MINION0; idx <= AvalonCharacterName.MINION3; ++idx)
        {
            CheckableObserverImageButton character = mCharacterSelectionRules.getCharacter(idx);
            if (character != null)
            {
                character.addOnClickObserver(() -> showSnackbar(mActivityContext.getString(R.string.avalon_minion_notification)));
            }
        }
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

    public void checkPlayerComposition(final String callingFuncName)
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::checkPlayerComposition(): mCharacterSelectionRules is NULL");
        }

        mCharacterSelectionRules.checkPlayerComposition(callingFuncName);
    }

    private AvalonCharacterSelectionRules mCharacterSelectionRules;
}