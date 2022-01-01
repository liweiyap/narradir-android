package com.liweiyap.narradir.util;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.liweiyap.narradir.R;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;

public final class PlayerNumberDictionary
{
    private PlayerNumberDictionary(){}

    // more verbose than previously but it's safe because resource ID integers are non-constant from Gradle version >4.
    public static void selectPlayerNumberButton(final Context context, final int playerNumber, final String callingFuncName) throws RuntimeException
    {
        if (!(context instanceof AppCompatActivity))
        {
            return;
        }

        CustomTypefaceableCheckableObserverButton selectorButton;

        switch (playerNumber)
        {
            case 5:
                selectorButton = ((AppCompatActivity) context).findViewById(R.id.p5Button);
                break;
            case 6:
                selectorButton = ((AppCompatActivity) context).findViewById(R.id.p6Button);
                break;
            case 7:
                selectorButton = ((AppCompatActivity) context).findViewById(R.id.p7Button);
                break;
            case 8:
                selectorButton = ((AppCompatActivity) context).findViewById(R.id.p8Button);
                break;
            case 9:
                selectorButton = ((AppCompatActivity) context).findViewById(R.id.p9Button);
                break;
            case 10:
                selectorButton = ((AppCompatActivity) context).findViewById(R.id.p10Button);
                break;
            default:
                throw new RuntimeException(callingFuncName + ": Invalid no of players: " + playerNumber);
        }

        if (selectorButton != null)
        {
            selectorButton.performClick();
        }
    }
}