package com.liweiyap.narradir.ui

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

object DialogHelper {
    fun showDialog(
        dialog: DialogFragment?,
        args: Bundle?,
        fragmentManager: FragmentManager?,
        tag: String?,
        doIgnoreOldDialog: Boolean,
        canCancel: Boolean = false,
    ) {
        if (dialog == null || fragmentManager == null) {
            return
        }

        if (!doIgnoreOldDialog && fragmentManager.findFragmentByTag(tag) != null) {
            return
        }

        dialog.isCancelable = canCancel
        if (args != null) {
            dialog.arguments = args
        }
        dialog.show(fragmentManager, tag)
    }
}