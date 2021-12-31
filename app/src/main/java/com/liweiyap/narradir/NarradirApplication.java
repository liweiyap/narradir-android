package com.liweiyap.narradir;

import android.app.Application;

import com.liweiyap.narradir.util.SamsungSemEmergencyManagerLeakFixActivity;

public class NarradirApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        SamsungSemEmergencyManagerLeakFixActivity.applyFix(this);
    }
}