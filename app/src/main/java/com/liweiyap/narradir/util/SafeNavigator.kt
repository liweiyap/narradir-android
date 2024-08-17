package com.liweiyap.narradir.util

import android.os.Bundle
import android.os.Handler
import android.os.Looper

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * Catch IllegalStateException or IllegalArgumentException during navigation, which can occur if navigation is called twice, e.g. via double tap on button
 * (see discussion at: https://www.reddit.com/r/androiddev/comments/104u0im/single_activity_apps_fragments_vs_views_in_2022/)
 */
object SafeNavigator {
    fun navigate(fragment: Fragment, @IdRes resId: Int) {
        navigateSafely {
            val navController: NavController = NavHostFragment.findNavController(fragment)
            postDelayed({
                navController.navigate(resId)
            })
        }
    }

    fun navigate(fragment: Fragment, @IdRes resId: Int, args: Bundle?) {
        navigateSafely {
            val navController: NavController = NavHostFragment.findNavController(fragment)
            postDelayed({
                navController.navigate(resId, args)
            })
        }
    }

    fun navigateUp(fragment: Fragment, steps: Int) {
        navigateSafely {
            val navController: NavController = NavHostFragment.findNavController(fragment)
            val runnable = Runnable { navController.navigateUp() }
            for (step: Int in 0 until steps) {
                postDelayed(runnable, step + 1)
            }
        }
    }

    private fun navigateSafely(navCall: () -> Unit) {
        try {
            navCall()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun postDelayed(runnable: Runnable, delay: Int = 1) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, delay.toLong())
    }
}