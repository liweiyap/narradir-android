package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback

import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.ObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton
import com.liweiyap.narradir.util.audio.IntroAudioPlayer

class SettingsRoleTimerFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_roletimer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPauseControlLayoutValueTextView = view.findViewById(R.id.updownControlLayoutValue)
        val pauseIncreaseButton = view.findViewById<ObserverButton>(R.id.upControlButton)
        val pauseDecreaseButton = view.findViewById<ObserverButton>(R.id.downControlButton)
        val generalBackButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.generalBackButton)
        val mainButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.mainButton)
        val settingsTitle = view.findViewById<CustomTypefaceableTextView>(R.id.settingsTitleTextView)
        val timeControlLayoutLabelTextView = view.findViewById<CustomTypefaceableTextView>(R.id.updownControlLayoutLabel)

        settingsTitle.setText(R.string.settings_title_roletimer)
        timeControlLayoutLabelTextView.setText(R.string.time_control_layout_label)

        // ----------------------------------------------------------------------
        // pause duration control
        // ----------------------------------------------------------------------

        displayPauseDuration()
        pauseIncreaseButton.addOnClickObserver { increasePauseDuration() }
        pauseDecreaseButton.addOnClickObserver { decreasePauseDuration() }

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(generalBackButton)
        addSoundToPlayOnButtonClick(mainButton)
        addSoundToPlayOnButtonClick(pauseIncreaseButton)
        addSoundToPlayOnButtonClick(pauseDecreaseButton)

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

    override fun onPause() {
        super.onPause()
        viewModel?.savePreferences()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPauseControlLayoutValueTextView = null
    }

    private fun displayPauseDuration() {
        viewModel?.let {
            mPauseControlLayoutValueTextView?.text = (it.mPauseDurationInMilliSecs / 1000).toString()
        }
    }

    private fun increasePauseDuration() {
        viewModel?.let {
            it.mPauseDurationInMilliSecs = (it.mPauseDurationInMilliSecs + IntroAudioPlayer.sPauseInterval).coerceAtMost(mMaxPauseDurationInMilliSecs)
            displayPauseDuration()
        }
    }

    private fun decreasePauseDuration() {
        viewModel?.let {
            it.mPauseDurationInMilliSecs = (it.mPauseDurationInMilliSecs - IntroAudioPlayer.sPauseInterval).coerceAtLeast(mMinPauseDurationInMilliSecs)
            displayPauseDuration()
        }
    }

    private val mMaxPauseDurationInMilliSecs: Long = 10000L
    private val mMinPauseDurationInMilliSecs: Long = 0L

    private var mPauseControlLayoutValueTextView: CustomTypefaceableTextView? = null
}