package com.liweiyap.narradir

import android.app.Application

import com.liweiyap.narradir.util.SamsungSemEmergencyManagerLeakFixActivity

class NarradirApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        SamsungSemEmergencyManagerLeakFixActivity.applyFix(this)
    }
}