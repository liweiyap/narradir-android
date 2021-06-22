package com.liweiyap.narradir;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.liweiyap.narradir.utils.ObserverImageButton;
import com.liweiyap.narradir.utils.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;

public class SecretHitlerControlGroup
{
    public SecretHitlerControlGroup(
        @NonNull final Context context,
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
        mCharacterArray = new SecretHitlerCharacterArray(
            context,
            liberal0Button, liberal1Button, liberal2Button, liberal3Button,
            liberal4Button, liberal5Button,
            hitlerButton, fascist0Button, fascist1Button, fascist2Button);

        ViewGroupSingleTargetSelector.addSingleTargetSelection(playerNumberSelectionLayout);

        // -----------------------------------------------------------------------------------------
        // adapt available characters according to player number
        // -----------------------------------------------------------------------------------------

        p5Button.addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(3);
            mCharacterArray.setExpectedEvilTotal(2);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p5Button.onClick()");
        });

        p6Button.addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(4);
            mCharacterArray.setExpectedEvilTotal(2);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p6Button.onClick()");
        });

        p7Button.addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(4);
            mCharacterArray.setExpectedEvilTotal(3);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p7Button.onClick()");
        });

        p8Button.addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(5);
            mCharacterArray.setExpectedEvilTotal(3);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p8Button.onClick()");
        });

        p9Button.addOnClickObserver(() -> {
            mCharacterArray.setExpectedGoodTotal(5);
            mCharacterArray.setExpectedEvilTotal(4);

            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.VISIBLE);

            mCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p9Button.onClick()");
        });

        p10Button.addOnClickObserver(() -> {
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
        // initialise MediaPlayer for character descriptions
        // -----------------------------------------------------------------------------------------

        mCharacterDescriptionMediaPlayer = new CharacterDescriptionMediaPlayer(context);
        addCharacterDescriptions();
    }

    private void addCharacterDescriptions()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("SecretHitlerControlGroup::addCharacterDescriptions(): mSecretHitlerCharacterArray is NULL");
        }

        try
        {
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL0).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.liberaldescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL1).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.liberaldescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL2).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.liberaldescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.liberaldescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.liberaldescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.liberaldescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.HITLER).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.hitlerdescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST0).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.fascistdescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.fascistdescription));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).addOnLongClickObserver(() -> resumeCharacterDescriptionMediaPlayer(R.raw.fascistdescription));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void resumeCharacterDescriptionMediaPlayer(@RawRes int descriptionId)
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

    public int getExpectedGoodTotal()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("AvalonControlGroup::getExpectedGoodTotal(): mCharacterSelectionRules is NULL");
        }

        return mCharacterArray.getExpectedGoodTotal();
    }

    public int getExpectedEvilTotal()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("AvalonControlGroup::getExpectedEvilTotal(): mCharacterSelectionRules is NULL");
        }

        return mCharacterArray.getExpectedEvilTotal();
    }

    private final SecretHitlerCharacterArray mCharacterArray;
    private final CharacterDescriptionMediaPlayer mCharacterDescriptionMediaPlayer;
}