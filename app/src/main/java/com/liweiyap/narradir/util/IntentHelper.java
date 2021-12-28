package com.liweiyap.narradir.util;

import android.content.Intent;

import org.jetbrains.annotations.NotNull;

public final class IntentHelper
{
    private IntentHelper(){}

    public static @NotNull String getStringExtra(final Intent intent, final String name, final @NotNull String defaultValue)
    {
        if (intent == null)
        {
            return defaultValue;
        }

        final String stringInIntent = intent.getStringExtra(name);
        if (stringInIntent == null)
        {
            return defaultValue;
        }

        return stringInIntent;
    }
}