package com.liweiyap.narradir.util

interface INarradirControl {
    fun getViewModel(): NarradirViewModel?
    fun playClickSound()
    fun navigateAwayFromApp()
    fun requestAgeSignals(doIgnoreOldDialog: Boolean)
    fun checkSharedAgeSignals(doIgnoreOldDialog: Boolean)
}