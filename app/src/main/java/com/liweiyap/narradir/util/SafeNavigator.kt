package com.liweiyap.narradir.util

import android.os.Bundle

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
            navController.navigate(resId)
        }
    }

    fun navigate(fragment: Fragment, @IdRes resId: Int, args: Bundle?) {
        navigateSafely {
            val navController: NavController = NavHostFragment.findNavController(fragment)
            navController.navigate(resId, args)
        }
    }

    fun navigateUp(fragment: Fragment, steps: Int) {
        navigateSafely {
            val navController: NavController = NavHostFragment.findNavController(fragment)
            for (step: Int in 0 until steps) {
                navController.navigateUp()
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
}