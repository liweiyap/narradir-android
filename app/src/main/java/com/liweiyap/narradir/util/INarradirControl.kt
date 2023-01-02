package com.liweiyap.narradir.util

interface INarradirControl {
    val viewModel: NarradirViewModel?
    fun playClickSound()
    fun navigateAwayFromApp()
}