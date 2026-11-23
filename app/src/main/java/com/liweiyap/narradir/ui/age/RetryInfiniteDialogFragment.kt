package com.liweiyap.narradir.ui.age

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.NarradirDialogFragmentBase
import com.liweiyap.narradir.util.BundleHelper
import com.liweiyap.narradir.util.age.AgeSignalsStep

class RetryInfiniteDialogFragment: NarradirDialogFragmentBase() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()
        val title = args.getString(getString(R.string.dialog_title_key))
        val message = args.getString(getString(R.string.dialog_msg_key))
        val step = BundleHelper.getSerializable<AgeSignalsStep>(args, getString(R.string.age_signals_step_enum_key))

        val activity = requireActivity()

        val dialog = AlertDialog.Builder(activity)
            .setIcon(R.drawable.ic_dialog_warning)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.retry_button_text)) { _, _ ->
                mNarradirControl?.playClickSound()
                mNarradirControl?.retryAgeSignals(step)
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}