package com.liweiyap.narradir.util.audio;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.liweiyap.narradir.R;

public final class BackgroundSoundDictionary
{
    private BackgroundSoundDictionary(){}

    @SuppressLint("NonConstantResourceId")
    public static String getNameStringFromResId(final @NonNull Context context, final @RawRes int resId)
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

    @SuppressLint("NonConstantResourceId")
    public static int getSelectorButtonIdFromSoundResId(final @RawRes int resId)
    {
        switch (resId)
        {
            case R.raw.backgroundcards:
                return R.id.backgroundSoundCardsButton;
            case R.raw.backgroundcrickets:
                return R.id.backgroundSoundCricketsButton;
            case R.raw.backgroundfireplace:
                return R.id.backgroundSoundFireplaceButton;
            case R.raw.backgroundrain:
                return R.id.backgroundSoundRainButton;
            case R.raw.backgroundrainforest:
                return R.id.backgroundSoundRainforestButton;
            case R.raw.backgroundrainstorm:
                return R.id.backgroundSoundRainstormButton;
            case R.raw.backgroundwolves:
                return R.id.backgroundSoundWolvesButton;
        }

        return R.id.backgroundSoundNoneButton;
    }
}