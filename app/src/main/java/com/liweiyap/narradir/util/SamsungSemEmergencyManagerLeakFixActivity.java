package com.liweiyap.narradir.util;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;

/**
 * Fixes a (third-party) library leak in Samsung Galaxy A3 (2016). See: https://github.com/square/leakcanary/issues/762
 * Might be removed eventually if we decide to stop supporting Android 7
 */
public final class SamsungSemEmergencyManagerLeakFixActivity implements Application.ActivityLifecycleCallbacks
{
    private SamsungSemEmergencyManagerLeakFixActivity(Application application)
    {
        mApplication = application;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState){}

    @Override
    public void onActivityStarted(@NonNull Activity activity){}

    @Override
    public void onActivityResumed(@NonNull Activity activity){}

    @Override
    public void onActivityPaused(@NonNull Activity activity){}

    @Override
    public void onActivityStopped(@NonNull Activity activity){}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState){}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity)
    {
        try
        {
            swapActivityWithApplicationContext();
        }
        catch (Exception e)
        {
            // The same result is expected on subsequent tries.
            e.printStackTrace();
        }

        mApplication.unregisterActivityLifecycleCallbacks(this);
    }

    public static void applyFix(Application application)
    {
        if ( (Build.MANUFACTURER.equals("samsung")) && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) )
        {
            application.registerActivityLifecycleCallbacks(new SamsungSemEmergencyManagerLeakFixActivity(application));
        }
    }

    private void swapActivityWithApplicationContext() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException
    {
        Class<?> semEmergencyManagerClass = Class.forName("com.samsung.android.emergencymode.SemEmergencyManager");
        Field sInstanceField = semEmergencyManagerClass.getDeclaredField("sInstance");
        sInstanceField.setAccessible(true);
        Object sInstance = sInstanceField.get(null);
        Field mContextField = semEmergencyManagerClass.getDeclaredField("mContext");
        mContextField.setAccessible(true);
        mContextField.set(sInstance, mApplication);
    }

    private final Application mApplication;
}