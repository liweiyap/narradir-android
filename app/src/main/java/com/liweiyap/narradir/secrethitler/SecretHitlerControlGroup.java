package com.liweiyap.narradir.secrethitler;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.liweiyap.narradir.R;
import com.liweiyap.narradir.ui.ObserverImageButton;
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.util.CharacterSelectionControlGroup;
import com.liweiyap.narradir.util.PlayerNumbers;

public class SecretHitlerControlGroup extends CharacterSelectionControlGroup
{
    public SecretHitlerControlGroup(
        @NonNull final Context activityContext,
        @NonNull final LinearLayout playerNumberSelectionLayout,
        @NonNull final CustomTypefaceableCheckableObserverButton p5Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p6Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p7Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p8Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p9Button,
        @NonNull final CustomTypefaceableCheckableObserverButton p10Button,
        @NonNull final ObserverImageButton liberal0Button,
        @NonNull final ObserverImageButton liberal1Button,
        @NonNull final ObserverImageButton liberal2Button,
        @NonNull final ObserverImageButton liberal3Button,
        @NonNull final ObserverImageButton liberal4Button,
        @NonNull final ObserverImageButton liberal5Button,
        @NonNull final ObserverImageButton hitlerButton,
        @NonNull final ObserverImageButton fascist0Button,
        @NonNull final ObserverImageButton fascist1Button,
        @NonNull final ObserverImageButton fascist2Button)
    {
        super(activityContext);

        mCharacterArray = new SecretHitlerCharacterArray(
            liberal0Button, liberal1Button, liberal2Button, liberal3Button,
            liberal4Button, liberal5Button,
            hitlerButton, fascist0Button, fascist1Button, fascist2Button);

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
            mCharacterArray.setExpectedGoodTotal(3);
            mCharacterArray.setExpectedEvilTotal(2);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p5Button.onClick()");
        });

        mPlayerNumberButtonArray[PlayerNumbers.P6].addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(4);
            mCharacterArray.setExpectedEvilTotal(2);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p6Button.onClick()");
        });

        mPlayerNumberButtonArray[PlayerNumbers.P7].addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(4);
            mCharacterArray.setExpectedEvilTotal(3);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p7Button.onClick()");
        });

        mPlayerNumberButtonArray[PlayerNumbers.P8].addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(5);
            mCharacterArray.setExpectedEvilTotal(3);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p8Button.onClick()");
        });

        mPlayerNumberButtonArray[PlayerNumbers.P9].addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(5);
            mCharacterArray.setExpectedEvilTotal(4);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.VISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p9Button.onClick()");
        });

        mPlayerNumberButtonArray[PlayerNumbers.P10].addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(6);
            mCharacterArray.setExpectedEvilTotal(4);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.VISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p10Button.onClick()");
        });

        // -----------------------------------------------------------------------------------------
        // MediaPlayer for character descriptions
        // -----------------------------------------------------------------------------------------

        addCharacterDescriptions();
    }

    public void destroy()
    {
        super.destroy();

        if (mCharacterArray != null)
        {
            mCharacterArray.destroy();
            mCharacterArray = null;
        }
    }

    private void addCharacterDescriptions()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("SecretHitlerControlGroup::addCharacterDescriptions(): mCharacterArray is NULL");
        }

        if (mActivityContext == null)
        {
            return;
        }

        try
        {
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.HITLER).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.hitlerdescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.fascistdescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.fascistdescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mActivityContext.getString(R.string.fascistdescription_key)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addSnackbarMessages()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("SecretHitlerControlGroup::addSnackbarMessages(): mCharacterArray is NULL");
        }

        for (ObserverImageButton btn : mCharacterArray.getCharacterImageButtonArray())
        {
            if (btn != null)
            {
                btn.addOnClickObserver(() -> showSnackbar(mActivityContext.getString(R.string.secrethitler_all_notification)));
            }
        }
    }

    public int getExpectedGoodTotal()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("SecretHitlerControlGroup::getExpectedGoodTotal(): mCharacterArray is NULL");
        }

        return mCharacterArray.getExpectedGoodTotal();
    }

    public int getExpectedEvilTotal()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("SecretHitlerControlGroup::getExpectedEvilTotal(): mCharacterArray is NULL");
        }

        return mCharacterArray.getExpectedEvilTotal();
    }

    private SecretHitlerCharacterArray mCharacterArray;
}