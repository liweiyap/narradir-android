package com.liweiyap.narradir.secrethitler;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.liweiyap.narradir.R;
import com.liweiyap.narradir.ui.ObserverImageButton;
import com.liweiyap.narradir.ui.SnackbarWrapper;
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.util.SnackbarBuilderFlag;
import com.liweiyap.narradir.util.audio.CharacterDescriptionMediaPlayer;

import java.util.EnumSet;

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

        mCharacterArray = new SecretHitlerCharacterArray(
            liberal0Button, liberal1Button, liberal2Button, liberal3Button,
            liberal4Button, liberal5Button,
            hitlerButton, fascist0Button, fascist1Button, fascist2Button);

        mSnackbar = new SnackbarWrapper(context);
        addSnackbarMessages();

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
            throw new RuntimeException("SecretHitlerControlGroup::addCharacterDescriptions(): mCharacterArray is NULL");
        }

        if (mContext == null)
        {
            return;
        }

        try
        {
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL3).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL4).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.LIBERAL5).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.liberaldescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.HITLER).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.hitlerdescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST0).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.fascistdescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST1).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.fascistdescription_key)));
            mCharacterArray.getCharacter(SecretHitlerCharacterName.FASCIST2).addOnLongClickObserver(() -> startCharacterDescriptionMediaPlayer(mContext.getString(R.string.fascistdescription_key)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void startCharacterDescriptionMediaPlayer(final @NonNull String description)
    {
        if ( (!(mContext instanceof AppCompatActivity)) || (mCharacterDescriptionMediaPlayer == null) )
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.play(description, 1f, mp -> ((AppCompatActivity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON));
        ((AppCompatActivity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        if ( (!(mContext instanceof AppCompatActivity)) || (mCharacterDescriptionMediaPlayer == null) )
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.stop();
        ((AppCompatActivity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void freeCharacterDescriptionMediaPlayer()
    {
        if (mCharacterDescriptionMediaPlayer == null)
        {
            return;
        }

        mCharacterDescriptionMediaPlayer.free();
    }

    public void addSnackbarMessages()
    {
        if (mCharacterArray == null)
        {
            throw new RuntimeException("SecretHitlerControlGroup::addSnackbarMessages(): mCharacterArray is NULL");
        }

        for (ObserverImageButton btn : mCharacterArray.getCharacterImageButtonArray())
        {
            btn.addOnClickObserver(this::showSnackbar);
        }
    }

    private void showSnackbar()
    {
        if (!(mContext instanceof AppCompatActivity))
        {
            return;
        }

        try
        {
            mSnackbar.show(
                ((AppCompatActivity) mContext).findViewById(R.id.characterSelectionLayoutNavBar),
                mContext.getString(R.string.secrethitler_all_notification),
                BaseTransientBottomBar.LENGTH_SHORT,
                EnumSet.of(SnackbarBuilderFlag.SHOW_ABOVE_XY, SnackbarBuilderFlag.ACTION_DISMISSABLE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

    private final SecretHitlerCharacterArray mCharacterArray;
    private final CharacterDescriptionMediaPlayer mCharacterDescriptionMediaPlayer;
    private final SnackbarWrapper mSnackbar;

    private final Context mContext;
}