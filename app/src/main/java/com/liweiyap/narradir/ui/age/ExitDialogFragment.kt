package com.liweiyap.narradir.ui.age

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.NarradirDialogFragmentBase

class ExitDialogFragment: NarradirDialogFragmentBase(doAutoDismissOnPause = false) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = requireActivity()
        val args = requireArguments()
        val title = args.getString(getString(R.string.dialog_title_key))
        val message = args.getString(getString(R.string.dialog_msg_key))

        val dialog = AlertDialog.Builder(activity)
            .setIcon(R.drawable.ic_dialog_error)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.exit_app_button_text)) { _, _ ->
                mNarradirControl?.playClickSound()
                activity.finish()
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}