package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class BackgroundSoundDictionary
{
    @SuppressLint("NonConstantResourceId")
    public static String getNameStringFromResId(@NonNull Context context, @RawRes int resId)
    {
        switch (resId)
        {
            case R.raw.backgroundcards:
                return context.getString(R.string.backgroundsound_cards);
            case R.raw.backgroundcrickets:
                return context.getString(R.string.backgroundsound_crickets);
            case R.raw.backgroundfireplace:
                return context.getString(R.string.backgroundsound_fireplace);
            case R.raw.backgroundrain:
                return context.getString(R.string.backgroundsound_rain);
            case R.raw.backgroundrainforest:
                return context.getString(R.string.backgroundsound_rainforest);
            case R.raw.backgroundrainstorm:
                return context.getString(R.string.backgroundsound_rainstorm);
            case R.raw.backgroundwolves:
                return context.getString(R.string.backgroundsound_wolves);
        }

        return context.getString(R.string.backgroundsound_none);
    }
}