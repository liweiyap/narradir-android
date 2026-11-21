package com.liweiyap.narradir.ui.age

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.NarradirDialogFragmentBase

class UnderageDialogFragment: NarradirDialogFragmentBase() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = requireActivity()

        val dialog = AlertDialog.Builder(activity)
            .setIcon(R.drawable.ic_dialog_error)
            .setTitle(getString(R.string.underage_dialog_title))
            .setMessage(getString(R.string.underage_dialog_msg))
            .setPositiveButton(getString(R.string.exit_app_button_text)) { _, _ ->
                mNarradirControl?.playClickSound()
                activity.finish()
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}