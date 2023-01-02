package com.liweiyap.narradir.util

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle

import java.lang.reflect.Field

/**
 * Fixes a (third-party) library leak in Samsung Galaxy A3 (2016). See: https://github.com/square/leakcanary/issues/762
 * Might be removed eventually if we decide to stop supporting Android 7
 */
class SamsungSemEmergencyManagerLeakFixActivity private constructor(private val mApplication: Application): Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        try {
            swapActivityWithApplicationContext()
        }
        catch (e: Exception) {
            // The same result is expected on subsequent tries.
            e.printStackTrace()
        }

        mApplication.unregisterActivityLifecycleCallbacks(this)
    }

    @Throws(
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class)
    private fun swapActivityWithApplicationContext() {
        val semEmergencyManagerClass: Class<*> = Class.forName("com.samsung.android.emergencymode.SemEmergencyManager")
        val sInstanceField: Field = semEmergencyManagerClass.getDeclaredField("sInstance")
        sInstanceField.isAccessible = true
        val sInstance: Any? = sInstanceField.get(null)
        val mContextField: Field = semEmergencyManagerClass.getDeclaredField("mContext")
        mContextField.isAccessible = true
        mContextField.set(sInstance, mApplication)
    }

    companion object {
        @JvmStatic
        fun applyFix(application: Application) {
            if ( (Build.MANUFACTURER == "samsung") && (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) ) {
                application.registerActivityLifecycleCallbacks(SamsungSemEmergencyManagerLeakFixActivity(application))
            }
        }
    }
}