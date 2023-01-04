package com.liweiyap.narradir.util

interface INarradirControl {
    fun getViewModel(): NarradirViewModel?
    fun playClickSound()
    fun navigateAwayFromApp()
}