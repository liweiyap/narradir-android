package com.liweiyap.narradir.ui

import android.view.View
import android.view.ViewGroup
import android.widget.Checkable

object ViewGroupSingleTargetSelector {
    @Throws(RuntimeException::class)
    fun addSingleTargetSelection(viewGroup: ViewGroup?) {
        if (viewGroup == null) {
            return
        }

        for (childIdx: Int in 0 until viewGroup.childCount) {
            val view: View = viewGroup.getChildAt(childIdx)

            if (view !is Checkable) {
                throw RuntimeException(
                    "ViewGroupSingleTargetSelector::addSingleTargetSelection(): " +
                            "Programming error: View is not Checkable.")
            }

            if (view !is IObserverListener) {
                throw RuntimeException(
                    "ViewGroupSingleTargetSelector::addSingleTargetSelection(): " +
                            "Programming error: View is not IObserverListener.")
            }

            (view as IObserverListener).addOnClickObserver {
                (view as Checkable).isChecked = true
                for (idx: Int in 0 until viewGroup.childCount) {
                    if (childIdx != idx) {
                        val tmp: View = viewGroup.getChildAt(idx)
                        (tmp as Checkable).isChecked = false
                    }
                }
            }
        }
    }
}