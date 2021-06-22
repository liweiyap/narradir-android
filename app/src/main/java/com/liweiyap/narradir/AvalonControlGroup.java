package com.liweiyap.narradir;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.core.util.Pair;

import com.liweiyap.narradir.utils.CheckableObserverImageButton;
import com.liweiyap.narradir.utils.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;

public class AvalonControlGroup
{
    public AvalonControlGroup(
        @NonNull final Context context,
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
        mCharacterSelectionRules = new AvalonCharacterSelectionRules(
            context,
            merlinButton, percivalButton, loyal0Button, loyal1Button,
            loyal2Button, loyal3Button, loyal4Button, loyal5Button,
            assassinButton, morganaButton, mordredButton, oberonButton,
            minion0Button, minion1Button, minion2Button, minion3Button);

        ViewGroupSingleTargetSelector.addSingleTargetSelection(playerNumberSelectionLayout);

        // -----------------------------------------------------------------------------------------
        // adapt available characters according to player number
        // -----------------------------------------------------------------------------------------

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

        // -----------------------------------------------------------------------------------------
        // initialise MediaPlayer for character descriptions
        // -----------------------------------------------------------------------------------------

        mCharacterDescriptionMediaPlayer = new CharacterDescriptionMediaPlayer(context);
        addCharacterDescriptions();
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

    private void addCharacterDescriptions()
    {
        if (mCharacterSelectionRules == null)
        {
            throw new RuntimeException("AvalonControlGroup::addCharacterDescriptions(): mCharacterSelectionRules is NULL");
        }

        try
        {
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MERLIN).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.merlindescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.PERCIVAL).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.percivaldescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.loyaldescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.loyaldescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.loyaldescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL3).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.loyaldescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL4).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.loyaldescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.LOYAL5).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.loyaldescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.ASSASSIN).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.assassindescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MORGANA).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.morganadescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MORDRED).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.mordreddescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.OBERON).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.oberondescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.miniondescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.miniondescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.miniondescription));
            mCharacterSelectionRules.getCharacter(AvalonCharacterName.MINION3).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(R.raw.miniondescription));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void startCharacterDescriptionMediaPlayer(@RawRes int descriptionId)
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.play(descriptionId);
    }

    public void resumeCharacterDescriptionMediaPlayer()
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.resume();
    }

    public void pauseCharacterDescriptionMediaPlayer()
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.pause();
    }

    public void stopCharacterDescriptionMediaPlayer()
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.stop();
    }

    public void freeCharacterDescriptionMediaPlayer()
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.free();
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

    private final AvalonCharacterSelectionRules mCharacterSelectionRules;
    private final CharacterDescriptionMediaPlayer mCharacterDescriptionMediaPlayer;
}