package com.liweiyap.narradir.util;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * The LifecycleOwner automatically removes your registered launcher when the Lifecycle is destroyed.
 * (https://developer.android.com/training/basics/intents/result#separate)
 */
public class LifecycleActivityResultObserverListener implements DefaultLifecycleObserver
{
    public LifecycleActivityResultObserverListener(
        final @NonNull ActivityResultRegistry registry,
        final @NonNull String callbackKey,
        final @NonNull ActivityResultCallback<ActivityResult> callback)
    {
        mRegistry = registry;
        mCallbackKey = callbackKey;
        mCallback = callback;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner)
    {
        mActivityResultLauncher = mRegistry.register(mCallbackKey, owner, new ActivityResultContracts.StartActivityForResult(), mCallback);
    }

    public void launch(final Intent intent)
    {
        if ( (intent == null) || (mActivityResultLauncher == null) )
        {
            return;
        }

        mActivityResultLauncher.launch(intent);
    }

    private final ActivityResultRegistry mRegistry;
    private final String mCallbackKey;
    private final ActivityResultCallback<ActivityResult> mCallback;
    private ActivityResultLauncher<Intent> mActivityResultLauncher;
}