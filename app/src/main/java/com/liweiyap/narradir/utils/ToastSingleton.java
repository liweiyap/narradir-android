package com.liweiyap.narradir.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class ToastSingleton
{
    private ToastSingleton(){}

    public static synchronized ToastSingleton getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new ToastSingleton();
        }

        return mInstance;
    }

    public void showNewToast(@NonNull Context context, final String message, final int duration) throws RuntimeException
    {
        if ( !((duration == Toast.LENGTH_SHORT) || (duration == Toast.LENGTH_LONG)) )
        {
            throw new RuntimeException(
                "ToastSingleton::showNewToast(): " +
                    "Programming Error. Value for duration (" + duration + ") not recognised.");
        }

        if (message == null)
        {
            return;
        }

        if (mToast != null)
        {
            mToast.cancel();
        }

        if (message.equals(""))
        {
            return;
        }

        mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    private Toast mToast;
    private static ToastSingleton mInstance;
}