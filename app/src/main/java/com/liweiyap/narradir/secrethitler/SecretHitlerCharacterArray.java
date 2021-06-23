package com.liweiyap.narradir.secrethitler;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.liweiyap.narradir.ui.ObserverImageButton;
import com.liweiyap.narradir.ui.ToastSingleton;

class SecretHitlerCharacterArray
{
    public SecretHitlerCharacterArray(
        @NonNull final Context context,
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

        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL0] = liberal0Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL0].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL1] = liberal1Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL1].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL2] = liberal2Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL2].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3] = liberal3Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4] = liberal4Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5] = liberal5Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.HITLER] = hitlerButton;
        mCharacterImageButtonArray[SecretHitlerCharacterName.HITLER].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST0] = fascist0Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST0].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1] = fascist1Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1].addOnClickObserver(this::showNewToast);

        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2] = fascist2Button;
        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2].addOnClickObserver(this::showNewToast);
    }

    public ObserverImageButton getCharacter(final int idx)
    {
        if ( (idx < SecretHitlerCharacterName.LIBERAL0) || (idx > SecretHitlerCharacterName.FASCIST2) )
        {
            throw new RuntimeException("SecretHitlerCharacterArray::getCharacter(): Invalid index " + idx);
        }

        return mCharacterImageButtonArray[idx];
    }

    public ObserverImageButton[] getCharacterImageButtonArray()
    {
        return mCharacterImageButtonArray;
    }

    private void showNewToast()
    {
        final String toastMessage = "Characters in Secret Hitler cannot be manually selected or deselected. Use the player number selection buttons above instead.";

        try
        {
            ToastSingleton.getInstance().showNewToast(mContext, toastMessage, Toast.LENGTH_SHORT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getActualGoodTotal()
    {
        int actualGoodTotal = 0;
        for (int idx = SecretHitlerCharacterName.LIBERAL0; idx <= SecretHitlerCharacterName.LIBERAL5; ++idx)
        {
            if (mCharacterImageButtonArray[idx].getVisibility() == View.VISIBLE)
            {
                ++actualGoodTotal;
            }
        }

        return actualGoodTotal;
    }

    public int getActualEvilTotal()
    {
        int actualEvilTotal = 0;
        for (int idx = SecretHitlerCharacterName.HITLER; idx <= SecretHitlerCharacterName.FASCIST2; ++idx)
        {
            if (mCharacterImageButtonArray[idx].getVisibility() == View.VISIBLE)
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

    private final ObserverImageButton[] mCharacterImageButtonArray = new ObserverImageButton[SecretHitlerCharacterName.getNumberOfCharacters()];
    private int mExpectedGoodTotal = 3;
    private int mExpectedEvilTotal = 2;

    private final Context mContext;
}