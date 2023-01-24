package com.liweiyap.narradir.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes

import androidx.fragment.app.Fragment

import com.liweiyap.narradir.util.INarradirControl
import com.liweiyap.narradir.util.NarradirViewModel
import com.liweiyap.narradir.util.SafeNavigator

abstract class NarradirFragmentBase: Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is INarradirControl) {
            throw RuntimeException(
                "NarradirFragmentBase::onAttach(): " +
                    "Programming error: Context is not INarradirControl.")
        }

        mNarradirControl = context
    }

    override fun onDetach() {
        super.onDetach()
        mNarradirControl = null
    }

    protected open fun addSoundToPlayOnButtonClick(btn: IObserverListener?) {
        btn?.addOnClickObserver {
            mNarradirControl?.playClickSound()
        }
    }

    protected fun navigateToFragment(@IdRes resId: Int) {
        SafeNavigator.navigate(fragment = this, resId = resId)
    }

    protected fun navigateToFragment(@IdRes resId: Int, args: Bundle?) {
        SafeNavigator.navigate(fragment = this, resId = resId, args = args)
    }

    protected fun navigateUp(steps: Int) {
        SafeNavigator.navigateUp(fragment = this, steps = steps)
    }

    protected var mNarradirControl: INarradirControl? = null
        private set

    protected val viewModel: NarradirViewModel?
        get() = mNarradirControl?.getViewModel()
}