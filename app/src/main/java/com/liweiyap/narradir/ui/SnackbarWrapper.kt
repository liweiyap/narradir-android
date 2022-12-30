package com.liweiyap.narradir.ui

import android.content.Context
import android.text.TextUtils
import android.view.View

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

import com.liweiyap.narradir.R
import com.liweiyap.narradir.util.IObserver
import com.liweiyap.narradir.util.SnackbarBuilderFlag

import java.util.EnumSet

class SnackbarWrapper(context: Context) {
    private var mSnackbar: Snackbar? = null
    private val mMaxInlineActionWidth: Int

    init {
        mMaxInlineActionWidth = context.resources.getDimensionPixelSize(R.dimen.narradir_design_snackbar_action_inline_max_width)
    }

    fun destroy() {
        mSnackbar?.dismiss()
        mSnackbar = null
    }

    fun show(view: View, message: String, duration: Int, actionMessage: String?, actionCallback: IObserver?, flags: EnumSet<SnackbarBuilderFlag>) {
        if (!isValidSnackbarDuration(duration)) {
            throw RuntimeException(
                "SnackbarWrapper::show()" +
                    ": Programming Error. Value for duration (" + duration + ") not recognised.")
        }

        dismissOldSnackbar()

        if (isValidSnackbar(view, message, flags)) {
            showNewSnackbar(view, message, duration, actionMessage, actionCallback, flags)
        }
    }

    private fun showNewSnackbar(view: View, message: String, duration: Int, actionMessage: String?, actionCallback: IObserver?, flags: EnumSet<SnackbarBuilderFlag>) {
        mSnackbar = Snackbar.make(view, message, duration)

        if (flags.contains(SnackbarBuilderFlag.SHOW_ABOVE_XY)) {
            mSnackbar!!.anchorView = view
        }

        if (flags.contains(SnackbarBuilderFlag.ACTION_DISMISSABLE)) {
            mSnackbar!!.setMaxInlineActionWidth(mMaxInlineActionWidth)
            actionMessage?.let {
                mSnackbar!!.setAction(it) {
                    dismissOldSnackbar()
                    actionCallback?.update()
                }
            }
        }

        mSnackbar!!.show()
    }

    fun dismissOldSnackbar() {
        mSnackbar?.dismiss()
    }

    private fun isValidSnackbarDuration(duration: Int): Boolean {
        return ((duration == BaseTransientBottomBar.LENGTH_SHORT) || (duration == BaseTransientBottomBar.LENGTH_LONG) || (duration == BaseTransientBottomBar.LENGTH_INDEFINITE))
    }

    private fun isValidSnackbar(view: View?, message: String?, flags: EnumSet<SnackbarBuilderFlag>?): Boolean {
        return ((view != null) && (!TextUtils.isEmpty(message)) && (flags != null))
    }
}