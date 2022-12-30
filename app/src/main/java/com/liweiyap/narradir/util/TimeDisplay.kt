package com.liweiyap.narradir.util

import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeDisplay {
    @JvmStatic
    fun fromMilliseconds(msec: Long): String {
        return String.format(
            // guaranteed to be available on all devices, has no surprising special cases, and tends to be most efficient due to its frequency of use
            // (https://developer.android.com/reference/java/util/Locale.html#default_locale)
            Locale.US,
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(msec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
            TimeUnit.MILLISECONDS.toSeconds(msec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec))
        )
    }
}