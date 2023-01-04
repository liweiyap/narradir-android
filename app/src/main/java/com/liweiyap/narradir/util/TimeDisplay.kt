package com.liweiyap.narradir.util

import android.content.res.Resources

import com.liweiyap.narradir.R

import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeDisplay {
    fun shortFormat(msec: Long): String {
        return String.format(
            // guaranteed to be available on all devices, has no surprising special cases, and tends to be most efficient due to its frequency of use
            // (https://developer.android.com/reference/java/util/Locale.html#default_locale)
            locale = Locale.US,
            format = "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(msec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
            TimeUnit.MILLISECONDS.toSeconds(msec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec))
        )
    }

    fun longFormat(resources: Resources, msec: Long): String {
        return String.format(
            // guaranteed to be available on all devices, has no surprising special cases, and tends to be most efficient due to its frequency of use
            // (https://developer.android.com/reference/java/util/Locale.html#default_locale)
            locale = Locale.US,
            // this long to int conversion is safe for the current (values/1000) that we have but this may change in future
            format = resources.getQuantityString(R.plurals.pauseduration_text, msec.toInt()),
            msec.toInt()
        )
    }
}