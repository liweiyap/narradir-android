package com.liweiyap.narradir.util.audio;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;

public final class IntroSegmentDictionary
{
    private IntroSegmentDictionary(){}

    public static boolean canPauseManuallyAtEnd(final @NonNull Context context, final @NonNull String resName)
    {
        return ( resName.equals(context.getString(R.string.avalonintrosegment1nooberon_key))
            || resName.equals(context.getString(R.string.avalonintrosegment1withoberon_key))
            || resName.equals(context.getString(R.string.avalonintrosegment3nomordred_key))
            || resName.equals(context.getString(R.string.avalonintrosegment3withmordred_key))
            || resName.equals(context.getString(R.string.avalonintrosegment5withpercivalnomorgana_key))
            || resName.equals(context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_key))
            || resName.equals(context.getString(R.string.secrethitlerintrosegment1small_key))
            || resName.equals(context.getString(R.string.secrethitlerintrosegment1large_key))
            || resName.equals(context.getString(R.string.secrethitlerintrosegment2large_key)) );
    }

    public static @Nullable String getSubtitleFromIntroSegmentRes(final @NonNull Context context, final @NonNull String resName)
    {
        if (resName.equals(context.getString(R.string.avalonintrosegment0_key)))
        {
            return context.getString(R.string.avalonintrosegment0_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment1nooberon_key)))
        {
            return context.getString(R.string.avalonintrosegment1nooberon_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment1withoberon_key)))
        {
            return context.getString(R.string.avalonintrosegment1withoberon_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment2_key)))
        {
            return context.getString(R.string.avalonintrosegment2_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment3nomerlin_key)))
        {
            return context.getString(R.string.avalonintrosegment3nomerlin_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment3nomordred_key)))
        {
            return context.getString(R.string.avalonintrosegment3nomordred_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment3withmordred_key)))
        {
            return context.getString(R.string.avalonintrosegment3withmordred_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment4_key)))
        {
            return context.getString(R.string.avalonintrosegment4_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment5nopercival_key)))
        {
            return context.getString(R.string.avalonintrosegment5nopercival_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment5withpercivalnomorgana_key)))
        {
            return context.getString(R.string.avalonintrosegment5withpercivalnomorgana_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_key)))
        {
            return context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment6withpercivalnomorgana_key)))
        {
            return context.getString(R.string.avalonintrosegment6withpercivalnomorgana_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment6withpercivalwithmorgana_key)))
        {
            return context.getString(R.string.avalonintrosegment6withpercivalwithmorgana_text);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment7_key)))
        {
            return context.getString(R.string.avalonintrosegment7_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment0small_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment0small_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment1small_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment1small_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment2small_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment2small_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment3small_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment3small_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment0large_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment0large_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment1large_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment1large_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment2large_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment2large_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment3large_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment3large_text);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment4large_key)))
        {
            return context.getString(R.string.secrethitlerintrosegment4large_text);
        }

        return null;
    }
}