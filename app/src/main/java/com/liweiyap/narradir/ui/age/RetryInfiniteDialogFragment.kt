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
        val title = args.getString(getString(R.string.retry_infinite_dialog_title_tag))
        val message = args.getString(getString(R.string.retry_infinite_dialog_msg_tag))
        val step = BundleHelper.getSerializable<AgeSignalsStep>(args, getString(R.string.age_signals_step_enum_tag))

        val activity = requireActivity()

        val dialog = AlertDialog.Builder(activity)
            .setIcon(R.drawable.ic_dialog_warning)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.retry_button_text)) { _, _ ->
                mNarradirControl?.playClickSound()
                retryAgeSignals(step)
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun retryAgeSignals(step: AgeSignalsStep?) {
        val doIgnoreOldDialog = true

        if (step == AgeSignalsStep.REQUEST_ACCESS) {
            mNarradirControl?.requestAgeSignals(doIgnoreOldDialog)
        }
        else if (step == AgeSignalsStep.CHECK_SHARED) {
            mNarradirControl?.checkSharedAgeSignals(doIgnoreOldDialog)
        }
    }
}