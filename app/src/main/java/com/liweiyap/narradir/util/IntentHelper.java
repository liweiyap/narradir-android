package com.liweiyap.narradir.util;

import android.content.Intent;

import androidx.annotation.NonNull;

public final class IntentHelper
{
    private IntentHelper(){}

    public static @NonNull String getStringExtra(final Intent intent, final String name, final @NonNull String defaultValue)
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