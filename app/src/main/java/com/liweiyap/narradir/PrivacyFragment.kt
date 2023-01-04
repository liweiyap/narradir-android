package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback

import com.liweiyap.narradir.ui.HtmlHelper
import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton

class PrivacyFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading0_subsubheading0_list0), getString(R.string.privacy_subheading0_subsubheading0_list0))
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading2_subsubheading0_list0), getString(R.string.privacy_subheading2_subsubheading0_list0))
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading3_subsubheading0_list0), getString(R.string.privacy_subheading3_subsubheading0_list0))
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading5_para0), getString(R.string.privacy_subheading5_para0))

        val generalBackButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.generalBackButton)
        val mainButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.mainButton)

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