package com.liweiyap.narradir;

import android.content.Context;
import android.media.MediaPlayer;
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
        mContext = context;

        mSecretHitlerCharacterArray = new SecretHitlerCharacterArray(
            context,
            liberal0Button, liberal1Button, liberal2Button, liberal3Button,
            liberal4Button, liberal5Button,
            hitlerButton, fascist0Button, fascist1Button, fascist2Button);

        ViewGroupSingleTargetSelector.addSingleTargetSelection(playerNumberSelectionLayout);

        // -----------------------------------------------------------------------------------------
        // adapt available characters according to player number
        // -----------------------------------------------------------------------------------------

        p5Button.addOnClickObserver(() -> {
            mSecretHitlerCharacterArray.setExpectedGoodTotal(3);
            mSecretHitlerCharacterArray.setExpectedEvilTotal(2);

            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mSecretHitlerCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p5Button.onClick()");
        });

        p6Button.addOnClickObserver(() -> {
            mSecretHitlerCharacterArray.setExpectedGoodTotal(4);
            mSecretHitlerCharacterArray.setExpectedEvilTotal(2);

            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mSecretHitlerCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p6Button.onClick()");
        });

        p7Button.addOnClickObserver(() -> {
            mSecretHitlerCharacterArray.setExpectedGoodTotal(4);
            mSecretHitlerCharacterArray.setExpectedEvilTotal(3);

            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mSecretHitlerCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p7Button.onClick()");
        });

        p8Button.addOnClickObserver(() -> {
            mSecretHitlerCharacterArray.setExpectedGoodTotal(5);
            mSecretHitlerCharacterArray.setExpectedEvilTotal(3);

            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.INVISIBLE);

            mSecretHitlerCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p8Button.onClick()");
        });

        p9Button.addOnClickObserver(() -> {
            mSecretHitlerCharacterArray.setExpectedGoodTotal(5);
            mSecretHitlerCharacterArray.setExpectedEvilTotal(4);

            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.INVISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.VISIBLE);

            mSecretHitlerCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p9Button.onClick()");
        });

        p10Button.addOnClickObserver(() -> {
            mSecretHitlerCharacterArray.setExpectedGoodTotal(6);
            mSecretHitlerCharacterArray.setExpectedEvilTotal(4);

            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).setVisibility(View.VISIBLE);
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).setVisibility(View.VISIBLE);

            mSecretHitlerCharacterArray.checkPlayerComposition("SecretHitlerControlGroup::p10Button.onClick()");
        });

        // -----------------------------------------------------------------------------------------
        // initialise MediaPlayer for character descriptions
        // -----------------------------------------------------------------------------------------

        addCharacterDescriptions();
    }

    private void addCharacterDescriptions()
    {
        if (mSecretHitlerCharacterArray == null)
        {
            throw new RuntimeException("SecretHitlerControlGroup::addCharacterDescriptions(): mSecretHitlerCharacterArray is NULL");
        }

        try
        {
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL0).addOnLongClickObserver(() -> playCharacterDescription(R.raw.liberaldescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL1).addOnLongClickObserver(() -> playCharacterDescription(R.raw.liberaldescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL2).addOnLongClickObserver(() -> playCharacterDescription(R.raw.liberaldescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).addOnLongClickObserver(() -> playCharacterDescription(R.raw.liberaldescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).addOnLongClickObserver(() -> playCharacterDescription(R.raw.liberaldescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).addOnLongClickObserver(() -> playCharacterDescription(R.raw.liberaldescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.HITLER).addOnLongClickObserver(() -> playCharacterDescription(R.raw.hitlerdescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST0).addOnLongClickObserver(() -> playCharacterDescription(R.raw.fascistdescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).addOnLongClickObserver(() -> playCharacterDescription(R.raw.fascistdescription));
            mSecretHitlerCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).addOnLongClickObserver(() -> playCharacterDescription(R.raw.fascistdescription));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void playCharacterDescription(@RawRes int descriptionId)
    {
        if (mCharacterDescriptionMediaPlayer != null)
        {
            mCharacterDescriptionMediaPlayer.stop();
        }

        try
        {
            // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
            mCharacterDescriptionMediaPlayer = MediaPlayer.create(mContext, descriptionId);
            mCharacterDescriptionMediaPlayer.start();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }

    public void startCharacterDescriptionMediaPlayer()
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.seekTo(mCharacterDescriptionMediaPlayerCurrentLength);
        mCharacterDescriptionMediaPlayer.start();
    }

    public void pauseCharacterDescriptionMediaPlayer()
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.pause();
        mCharacterDescriptionMediaPlayerCurrentLength = mCharacterDescriptionMediaPlayer.getCurrentPosition();
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

        mCharacterDescriptionMediaPlayer.release();
        mCharacterDescriptionMediaPlayer = null;
    }

    public int getExpectedGoodTotal()
    {
        if (mSecretHitlerCharacterArray == null)
        {
            throw new RuntimeException("AvalonControlGroup::getExpectedGoodTotal(): mCharacterSelectionRules is NULL");
        }

        return mSecretHitlerCharacterArray.getExpectedGoodTotal();
    }

    public int getExpectedEvilTotal()
    {
        if (mSecretHitlerCharacterArray == null)
        {
            throw new RuntimeException("AvalonControlGroup::getExpectedEvilTotal(): mCharacterSelectionRules is NULL");
        }

        return mSecretHitlerCharacterArray.getExpectedEvilTotal();
    }

    private final Context mContext;

    private final SecretHitlerCharacterArray mSecretHitlerCharacterArray;

    private MediaPlayer mCharacterDescriptionMediaPlayer;
    private int mCharacterDescriptionMediaPlayerCurrentLength;
}