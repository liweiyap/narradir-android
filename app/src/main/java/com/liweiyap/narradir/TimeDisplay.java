package com.liweiyap.narradir;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeDisplay
{
    public static @NotNull String fromMilliseconds(final long msec)
    {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(msec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
            TimeUnit.MILLISECONDS.toSeconds(msec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec))
        );
    }
}
