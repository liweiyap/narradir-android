package com.liweiyap.narradir.util.audio;

import android.annotation.SuppressLint;

import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import com.liweiyap.narradir.R;

public final class IntroSegmentDictionary
{
    private IntroSegmentDictionary(){}

    @SuppressLint("NonConstantResourceId")
    public static boolean canPauseManuallyAtEnd(@RawRes final int resId)
    {
        switch (resId)
        {
            case R.raw.avalonintrosegment1nooberon:
            case R.raw.avalonintrosegment1withoberon:
            case R.raw.avalonintrosegment3nomordred:
            case R.raw.avalonintrosegment3withmordred:
            case R.raw.avalonintrosegment5withpercivalnomorgana:
            case R.raw.avalonintrosegment5withpercivalwithmorgana:
            case R.raw.secrethitlerintrosegment1small:
            case R.raw.secrethitlerintrosegment1large:
            case R.raw.secrethitlerintrosegment2large:
                return true;
            default:
                return false;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public static @StringRes int getSubtitleResIdFromIntroSegmentResId(@RawRes final int resId)
    {
        switch (resId)
        {
            case R.raw.avalonintrosegment0:
                return R.string.avalonintrosegment0_text;
            case R.raw.avalonintrosegment1nooberon:
                return R.string.avalonintrosegment1nooberon_text;
            case R.raw.avalonintrosegment1withoberon:
                return R.string.avalonintrosegment1withoberon_text;
            case R.raw.avalonintrosegment2:
                return R.string.avalonintrosegment2_text;
            case R.raw.avalonintrosegment3nomerlin:
                return R.string.avalonintrosegment3nomerlin_text;
            case R.raw.avalonintrosegment3nomordred:
                return R.string.avalonintrosegment3nomordred_text;
            case R.raw.avalonintrosegment3withmordred:
                return R.string.avalonintrosegment3withmordred_text;
            case R.raw.avalonintrosegment4:
                return R.string.avalonintrosegment4_text;
            case R.raw.avalonintrosegment5nopercival:
                return R.string.avalonintrosegment5nopercival_text;
            case R.raw.avalonintrosegment5withpercivalnomorgana:
                return R.string.avalonintrosegment5withpercivalnomorgana_text;
            case R.raw.avalonintrosegment5withpercivalwithmorgana:
                return R.string.avalonintrosegment5withpercivalwithmorgana_text;
            case R.raw.avalonintrosegment6withpercivalnomorgana:
                return R.string.avalonintrosegment6withpercivalnomorgana_text;
            case R.raw.avalonintrosegment6withpercivalwithmorgana:
                return R.string.avalonintrosegment6withpercivalwithmorgana_text;
            case R.raw.avalonintrosegment7:
                return R.string.avalonintrosegment7_text;
            case R.raw.secrethitlerintrosegment0small:
                return R.string.secrethitlerintrosegment0small_text;
            case R.raw.secrethitlerintrosegment1small:
                return R.string.secrethitlerintrosegment1small_text;
            case R.raw.secrethitlerintrosegment2small:
                return R.string.secrethitlerintrosegment2small_text;
            case R.raw.secrethitlerintrosegment3small:
                return R.string.secrethitlerintrosegment3small_text;
            case R.raw.secrethitlerintrosegment0large:
                return R.string.secrethitlerintrosegment0large_text;
            case R.raw.secrethitlerintrosegment1large:
                return R.string.secrethitlerintrosegment1large_text;
            case R.raw.secrethitlerintrosegment2large:
                return R.string.secrethitlerintrosegment2large_text;
            case R.raw.secrethitlerintrosegment3large:
                return R.string.secrethitlerintrosegment3large_text;
            case R.raw.secrethitlerintrosegment4large:
                return R.string.secrethitlerintrosegment4large_text;
        }

        return 0;
    }
}