package com.liweiyap.narradir.util;

import androidx.annotation.NonNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class TimeDisplay
{
    private TimeDisplay(){}

    public static @NonNull String fromMilliseconds(final long msec)
    {
        return String.format(
            Locale.ENGLISH,
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(msec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
            TimeUnit.MILLISECONDS.toSeconds(msec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec))
        );
    }
}