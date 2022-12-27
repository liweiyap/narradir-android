package com.liweiyap.narradir.util;

import android.content.Context;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.liweiyap.narradir.R;
import com.liweiyap.narradir.ui.SnackbarWrapper;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.util.audio.CharacterDescriptionMediaPlayer;

import java.util.EnumSet;

public abstract class CharacterSelectionControlGroup
{
    public CharacterSelectionControlGroup(final Context context)
    {
        mContext = context;
        mSnackbar = new SnackbarWrapper(context);
        mCharacterDescriptionMediaPlayer = new CharacterDescriptionMediaPlayer(context);
    }

    public void destroy()
    {
        mContext = null;

        if (mSnackbar != null)
        {
            mSnackbar.destroy();
            mSnackbar = null;
        }

        if (mCharacterDescriptionMediaPlayer != null)
        {
            mCharacterDescriptionMediaPlayer.destroy();
            mCharacterDescriptionMediaPlayer = null;
        }

        if (mPlayerNumberButtonArray != null)
        {
            for (int idx = 0; idx < mPlayerNumberButtonArray.length; ++idx)
            {
                if (mPlayerNumberButtonArray[idx] != null)
                {
                    mPlayerNumberButtonArray[idx].destroy();
                    mPlayerNumberButtonArray[idx] = null;
                }
            }

            mPlayerNumberButtonArray = null;
        }
    }

    public CustomTypefaceableCheckableObserverButton[] getPlayerNumberButtonArray()
    {
        return mPlayerNumberButtonArray;
    }

    public void selectPlayerNumberButton(final int playerNumber)
    {
        if (mPlayerNumberButtonArray == null)
        {
            throw new RuntimeException("CharacterSelectionControlGroup::selectPlayerNumberButton(): mPlayerNumberButtonArray is NULL");
        }

        CustomTypefaceableCheckableObserverButton selectorButton;
        switch (playerNumber)
        {
            case 5:
                selectorButton = mPlayerNumberButtonArray[PlayerNumbers.P5];
                break;
            case 6:
                selectorButton = mPlayerNumberButtonArray[PlayerNumbers.P6];
                break;
            case 7:
                selectorButton = mPlayerNumberButtonArray[PlayerNumbers.P7];
                break;
            case 8:
                selectorButton = mPlayerNumberButtonArray[PlayerNumbers.P8];
                break;
            case 9:
                selectorButton = mPlayerNumberButtonArray[PlayerNumbers.P9];
                break;
            case 10:
                selectorButton = mPlayerNumberButtonArray[PlayerNumbers.P10];
                break;
            default:
                throw new RuntimeException("CharacterSelectionControlGroup::selectPlayerNumberButton(): Invalid no of players: " + playerNumber);
        }

        if (selectorButton != null)
        {
            selectorButton.performClick();
        }
    }

    protected void startCharacterDescriptionMediaPlayer(final @NonNull String description)
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

    protected void showSnackbar(final String message)
    {
        if (!(mContext instanceof AppCompatActivity))
        {
            return;
        }

        try
        {
            mSnackbar.show(
                ((AppCompatActivity) mContext).findViewById(R.id.characterSelectionLayoutNavBar),
                message,
                BaseTransientBottomBar.LENGTH_SHORT,
                mContext.getString(R.string.positive_button_text),
                null,
                EnumSet.of(SnackbarBuilderFlag.SHOW_ABOVE_XY, SnackbarBuilderFlag.ACTION_DISMISSABLE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected CustomTypefaceableCheckableObserverButton[] mPlayerNumberButtonArray = new CustomTypefaceableCheckableObserverButton[PlayerNumbers.getNumberOfPlayerNumbers()];
    protected CharacterDescriptionMediaPlayer mCharacterDescriptionMediaPlayer;
    protected SnackbarWrapper mSnackbar;

    protected Context mContext;
}