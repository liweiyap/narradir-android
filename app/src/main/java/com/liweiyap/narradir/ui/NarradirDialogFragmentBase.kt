package com.liweiyap.narradir.ui

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.liweiyap.narradir.util.INarradirControl
import com.liweiyap.narradir.util.NarradirViewModel

abstract class NarradirDialogFragmentBase: DialogFragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is INarradirControl) {
            throw RuntimeException(
                "NarradirDialogFragmentBase::onAttach(): " +
                    "Programming error: Context is not INarradirControl.")
        }

        mNarradirControl = context
    }

    override fun onDetach() {
        super.onDetach()
        mNarradirControl = null
    }

    protected var mNarradirControl: INarradirControl? = null
        private set
}