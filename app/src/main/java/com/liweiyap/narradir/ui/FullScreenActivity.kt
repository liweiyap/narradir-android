package com.liweiyap.narradir.ui

import android.content.res.Configuration
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RequiresApi

/**
 * https://stackoverflow.com/questions/31198923/preventing-android-navigation-and-action-bars-from-appearing-when-switching-acti
 * https://stackoverflow.com/questions/34394916/why-navigation-bar-is-appearing-in-fullscreen-apps-when-clicked-on-popup-menu
 */
open class FullScreenActivity: AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        makeFullScreen()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        makeFullScreen()
        return super.onTouchEvent(event)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        makeFullScreen()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        makeFullScreen()
        return super.dispatchTouchEvent(ev)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            makeFullScreen()
        }
    }

    private fun makeFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            makeFullScreenSinceApi29()
        }
        else {
            makeFullScreenPreApi29()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun makeFullScreenSinceApi29() {
        // https://stackoverflow.com/questions/62643517/immersive-fullscreen-on-android-11
        window.setDecorFitsSystemWindows(false)
        val controller: WindowInsetsController = window.insetsController
            ?: return

        controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    @Suppress("DEPRECATION")
    private fun makeFullScreenPreApi29() {
        // make the below show-/hide-changes temporary
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                // set the content to appear under the system bars
                // so that the content does not resize when the system bars hide and show
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                // hide navigation bar
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                // hide status bar
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}