package com.liweiyap.narradir.util.audio

import android.content.Context

import com.liweiyap.narradir.R

object IntroSegmentDictionary {
    @JvmStatic
    fun canPauseManuallyAtEnd(context: Context?, resName: String?): Boolean {
        return (context != null)
            && (resName != null)
            && ( resName == context.getString(R.string.avalonintrosegment1nooberon_key)
            || resName == context.getString(R.string.avalonintrosegment1withoberon_key)
            || resName == context.getString(R.string.avalonintrosegment3nomordred_key)
            || resName == context.getString(R.string.avalonintrosegment3withmordred_key)
            || resName == context.getString(R.string.avalonintrosegment5withpercivalnomorgana_key)
            || resName == context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_key)
            || resName == context.getString(R.string.secrethitlerintrosegment1small_key)
            || resName == context.getString(R.string.secrethitlerintrosegment1large_key)
            || resName == context.getString(R.string.secrethitlerintrosegment2large_key) )
    }

    @JvmStatic
    fun getSubtitleFromIntroSegmentRes(context: Context?, resName: String?): String? {
        if (context == null || resName == null) {
            return null
        }

        return when (resName) {
            context.getString(R.string.avalonintrosegment0nomerlin_key) -> context.getString(R.string.avalonintrosegment0nomerlin_text)
            context.getString(R.string.avalonintrosegment0withmerlin_key) -> context.getString(R.string.avalonintrosegment0withmerlin_text)
            context.getString(R.string.avalonintrosegment1nooberon_key) -> context.getString(R.string.avalonintrosegment1nooberon_text)
            context.getString(R.string.avalonintrosegment1withoberon_key) -> context.getString(R.string.avalonintrosegment1withoberon_text)
            context.getString(R.string.avalonintrosegment2_key) -> context.getString(R.string.avalonintrosegment2_text)
            context.getString(R.string.avalonintrosegment3nomerlin_key) -> context.getString(R.string.avalonintrosegment3nomerlin_text)
            context.getString(R.string.avalonintrosegment3nomordred_key) -> context.getString(R.string.avalonintrosegment3nomordred_text)
            context.getString(R.string.avalonintrosegment3withmordred_key) -> context.getString(R.string.avalonintrosegment3withmordred_text)
            context.getString(R.string.avalonintrosegment4_key) -> context.getString(R.string.avalonintrosegment4_text)
            context.getString(R.string.avalonintrosegment5nopercival_key) -> context.getString(R.string.avalonintrosegment5nopercival_text)
            context.getString(R.string.avalonintrosegment5withpercivalnomorgana_key) -> context.getString(R.string.avalonintrosegment5withpercivalnomorgana_text)
            context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_key) -> context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_text)
            context.getString(R.string.avalonintrosegment6withpercivalnomorgana_key) -> context.getString(R.string.avalonintrosegment6withpercivalnomorgana_text)
            context.getString(R.string.avalonintrosegment6withpercivalwithmorgana_key) -> context.getString(R.string.avalonintrosegment6withpercivalwithmorgana_text)
            context.getString(R.string.avalonintrosegment7_key) -> context.getString(R.string.avalonintrosegment7_text)
            context.getString(R.string.secrethitlerintrosegment0small_key) -> context.getString(R.string.secrethitlerintrosegment0small_text)
            context.getString(R.string.secrethitlerintrosegment1small_key) -> context.getString(R.string.secrethitlerintrosegment1small_text)
            context.getString(R.string.secrethitlerintrosegment2small_key) -> context.getString(R.string.secrethitlerintrosegment2small_text)
            context.getString(R.string.secrethitlerintrosegment3small_key) -> context.getString(R.string.secrethitlerintrosegment3small_text)
            context.getString(R.string.secrethitlerintrosegment0large_key) -> context.getString(R.string.secrethitlerintrosegment0large_text)
            context.getString(R.string.secrethitlerintrosegment1large_key) -> context.getString(R.string.secrethitlerintrosegment1large_text)
            context.getString(R.string.secrethitlerintrosegment2large_key) -> context.getString(R.string.secrethitlerintrosegment2large_text)
            context.getString(R.string.secrethitlerintrosegment3large_key) -> context.getString(R.string.secrethitlerintrosegment3large_text)
            context.getString(R.string.secrethitlerintrosegment4large_key) -> context.getString(R.string.secrethitlerintrosegment4large_text)
            else -> null
        }
    }
}