package com.liweiyap.narradir.util

import com.liweiyap.narradir.util.age.AgeSignalsStep

interface INarradirControl {
    fun getViewModel(): NarradirViewModel?
    fun playClickSound()
    fun navigateAwayFromApp()
    fun retryAgeSignals(step: AgeSignalsStep?, isMax: Boolean)
}