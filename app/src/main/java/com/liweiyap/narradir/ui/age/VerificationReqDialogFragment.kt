package com.liweiyap.narradir.ui.age

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.NarradirDialogFragmentBase
import com.liweiyap.narradir.util.SafeNavigator

class VerificationReqDialogFragment: NarradirDialogFragmentBase() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireActivity())
            .setIcon(R.drawable.ic_dialog_warning)
            .setTitle(getString(R.string.verify_req_dialog_title))
            .setMessage(getString(R.string.verify_req_dialog_msg))
            .setPositiveButton(getString(R.string.playstore_link_button_text)) { _, _ ->
                mNarradirControl?.playClickSound()
                navigateToPlayStore()
                // dialog auto-dismissed
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    private fun navigateToPlayStore() {
        SafeNavigator.navigateSafely {
            val appIntent: Intent? = requireActivity().packageManager.getLaunchIntentForPackage("com.android.vending")
            if (appIntent == null) {
                // Play Store unavailable; optionally open in browser
                val browserIntent = Intent(Intent.ACTION_VIEW, "https://play.google.com/store".toUri())
                startActivity(browserIntent)
            }
            else {
                startActivity(appIntent)
            }
        }
    }
}