package com.liweiyap.narradir.ui

import android.content.Context

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import com.liweiyap.narradir.util.INarradirControl
import com.liweiyap.narradir.util.NarradirViewModel

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

    protected fun navigateUp(steps: Int) {
        val navController: NavController = NavHostFragment.findNavController(this)
        for (step: Int in 0 until steps) {
            navController.navigateUp()
        }
    }

    protected var mNarradirControl: INarradirControl? = null
        private set

    protected val viewModel: NarradirViewModel?
        get() = mNarradirControl?.getViewModel()
}