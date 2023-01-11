package com.liweiyap.narradir

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.TextViewSpanHelper
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton

class HelpFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val generalBackButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.generalBackButton)
        val mainButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.mainButton)

        TextViewSpanHelper.setSpan(
            textView = view.findViewById(R.id.tv_help_subheading3_para1),
            text = getString(R.string.help_subheading3_para1),
            spanStart = 6,
            spanEnd = 10,
        ) {
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
        }

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(generalBackButton)
        addSoundToPlayOnButtonClick(mainButton)

        // ----------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // ----------------------------------------------------------------------

        generalBackButton.addOnClickObserver { navigateUp(steps = 1) }
        mainButton.addOnClickObserver { navigateUp(steps = 2) }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                generalBackButton.performClick()
            }
        })
    }
}