package com.liweiyap.narradir

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

import androidx.activity.OnBackPressedCallback

import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.SettingsLayout
import com.liweiyap.narradir.ui.TextViewAutosizeHelper
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton
import com.liweiyap.narradir.util.TimeDisplay

import kotlin.math.roundToInt

class SettingsHomeFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mNarrationSettingsLayout = view.findViewById(R.id.narrationSettingsLayout)
        mBackgroundSettingsLayout = view.findViewById(R.id.backgroundSettingsLayout)
        mRoleTimerSettingsLayout = view.findViewById(R.id.roleTimerSettingsLayout)
        val privacyButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.privacyButton)
        val backButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.settingsHomeLayoutBackButton)
        val helpButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.settingsHomeLayoutHelpButton)
        val authorInfoLayout = view.findViewById<LinearLayout>(R.id.authorInfoLayout)

        // ----------------------------------------------------------------------
        // set key and value of each individual SettingsLayout
        // ----------------------------------------------------------------------

        mNarrationSettingsLayout!!.setKey(getString(R.string.settings_title_narration))
        mBackgroundSettingsLayout!!.setKey(getString(R.string.settings_title_background))
        mRoleTimerSettingsLayout!!.setKey(getString(R.string.settings_title_roletimer))

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(mNarrationSettingsLayout!!.editButton)
        addSoundToPlayOnButtonClick(mBackgroundSettingsLayout!!.editButton)
        addSoundToPlayOnButtonClick(mRoleTimerSettingsLayout!!.editButton)
        addSoundToPlayOnButtonClick(privacyButton)
        addSoundToPlayOnButtonClick(backButton)
        addSoundToPlayOnButtonClick(helpButton)

        // ----------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // ----------------------------------------------------------------------

        privacyButton.addOnClickObserver { navigateToFragment(resId = R.id.actionSettingsHomeToPrivacy) }
        backButton.addOnClickObserver { navigateUp(steps = 1) }
        helpButton.addOnClickObserver { navigateToFragment(resId = R.id.actionSettingsHomeToHelp) }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backButton.performClick()
            }
        })

        // ----------------------------------------------------------------------
        // navigation from background sound selection layout itself
        // ----------------------------------------------------------------------

        mNarrationSettingsLayout!!.editButton.addOnClickObserver { navigateToFragment(resId = R.id.actionSettingsHomeToNarration) }
        mBackgroundSettingsLayout!!.editButton.addOnClickObserver { navigateToFragment(resId = R.id.actionSettingsHomeToBackground) }
        mRoleTimerSettingsLayout!!.editButton.addOnClickObserver { navigateToFragment(resId = R.id.actionSettingsHomeToRoleTimer) }

        // ----------------------------------------------------------------------
        // navigation to external web browser
        // ----------------------------------------------------------------------

        authorInfoLayout.setOnClickListener { navigateToAuthorWebsite() }

        // -----------------------------------------------------------------------------------------
        // auto-sizing TextViews
        // -----------------------------------------------------------------------------------------

        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(view.findViewById(R.id.authorWebsiteTextView))
    }

    override fun onResume() {
        super.onResume()

        if (viewModel == null) {
            return
        }

        mNarrationSettingsLayout?.setValue("Vol ${(viewModel!!.mNarrationVolume * 10).roundToInt()}")
        mBackgroundSettingsLayout?.setValue("${viewModel!!.mBackgroundSoundName}, Vol ${(viewModel!!.mBackgroundSoundVolume * 10).roundToInt()}")
        mRoleTimerSettingsLayout?.setValue(TimeDisplay.shortFormat(msec = viewModel!!.mPauseDurationInMilliSecs))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mNarrationSettingsLayout = null
        mBackgroundSettingsLayout = null
        mRoleTimerSettingsLayout = null
    }

    private fun navigateToAuthorWebsite() {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://liweiyap.github.io"))
            startActivity(browserIntent)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mNarrationSettingsLayout: SettingsLayout? = null
    private var mBackgroundSettingsLayout: SettingsLayout? = null
    private var mRoleTimerSettingsLayout: SettingsLayout? = null
}