package com.liweiyap.narradir.ui.age

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.NarradirDialogFragmentBase
import com.liweiyap.narradir.util.BundleHelper
import com.liweiyap.narradir.util.age.AgeSignalsRetryableWithCountdown
import com.liweiyap.narradir.util.age.AgeSignalsStep
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.Long

class RetryCountdownDialogFragment: NarradirDialogFragmentBase(doAutoDismissOnPause = false) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()
        val title = args.getString(getString(R.string.dialog_title_key))
        val message = args.getString(getString(R.string.dialog_msg_key))
        val step = BundleHelper.getSerializable<AgeSignalsStep>(args, getString(R.string.age_signals_step_enum_key))
        val retryType = BundleHelper.getSerializable<AgeSignalsRetryableWithCountdown>(args, getString(R.string.age_signals_retryable_with_countdown_enum_key))

        viewModel?.let {
            if (it.mLastAgeSignalsRetryableWithCountdown != retryType) {
                it.mLastAgeSignalsRetryCount = 0
            }
            it.mLastAgeSignalsRetryableWithCountdown = retryType
        }

        val dialog = AlertDialog.Builder(requireActivity())
            .setIcon(R.drawable.ic_dialog_warning)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.retry_button_text)) { _, _ ->
                mNarradirControl?.playClickSound()

                val isMax: Boolean = viewModel?.let {
                    ++it.mLastAgeSignalsRetryCount >= (retryType!!.retryMax)
                } ?: false
                mNarradirControl?.retryAgeSignals(step, isMax)
            }
            .create()
        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnShowListener { dialog ->
            val retryBtn: Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            retryBtn.isEnabled = false

            object : CountDownTimer(
                retryType!!.getCurrentWaitDuration(viewModel?.mLastAgeSignalsRetryCount),
                retryType.waitDurationStepMillis
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    retryBtn.text = String.format(
                        // guaranteed to be available on all devices, has no surprising special cases, and tends to be most efficient due to its frequency of use
                        // (https://developer.android.com/reference/java/util/Locale.html#default_locale)
                        locale = Locale.US,
                        format = "%s (%d)",
                        getString(R.string.retry_button_text),
                        // add one so it never displays zero
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                    )
                }

                override fun onFinish() {
                    retryBtn.text = getString(R.string.retry_button_text)
                    retryBtn.isEnabled = true
                }
            }.start()
        }

        return dialog
    }
}