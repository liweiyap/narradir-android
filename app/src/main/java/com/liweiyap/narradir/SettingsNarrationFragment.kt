package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback

import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.ObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView
import com.liweiyap.narradir.util.IObserver

import kotlin.math.roundToInt

class SettingsNarrationFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_narration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mVolumeControlLayoutValueTextView = view.findViewById(R.id.updownControlLayoutValue)
        val volumeIncreaseButton = view.findViewById<ObserverButton>(R.id.upControlButton)
        val volumeDecreaseButton = view.findViewById<ObserverButton>(R.id.downControlButton)
        val generalBackButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.generalBackButton)
        val mainButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.mainButton)
        val settingsTitle = view.findViewById<CustomTypefaceableTextView>(R.id.settingsTitleTextView)
        val timeControlLayoutLabelTextView = view.findViewById<CustomTypefaceableTextView>(R.id.updownControlLayoutLabel)

        settingsTitle.setText(R.string.settings_title_narration)
        timeControlLayoutLabelTextView.setText(R.string.volume_control_layout_label)

        // ----------------------------------------------------------------------
        // volume control
        // ----------------------------------------------------------------------

        displayVolume()
        volumeIncreaseButton.addOnClickObserver(object: IObserver { override fun update() { increaseVolume() } })
        volumeDecreaseButton.addOnClickObserver(object: IObserver { override fun update() { decreaseVolume() } })

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(generalBackButton)
        addSoundToPlayOnButtonClick(mainButton)
        addSoundToPlayOnButtonClick(volumeIncreaseButton)
        addSoundToPlayOnButtonClick(volumeDecreaseButton)

        // ----------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // ----------------------------------------------------------------------

        generalBackButton.addOnClickObserver(object: IObserver { override fun update() { navigateUp(steps = 1) } })
        mainButton.addOnClickObserver(object: IObserver { override fun update() { navigateUp(steps = 2) } })

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
        mVolumeControlLayoutValueTextView = null
    }

    private fun displayVolume() {
        viewModel?.let {
            mVolumeControlLayoutValueTextView?.text = (it.narrationVolume * 10).roundToInt().toString()
        }
    }

    private fun increaseVolume() {
        viewModel?.let {
            it.narrationVolume = (it.narrationVolume + 0.1f).coerceAtMost(maximumValue = 1f)
            displayVolume()
        }
    }

    private fun decreaseVolume() {
        viewModel?.let {
            it.narrationVolume = (it.narrationVolume - 0.1f).coerceAtLeast(minimumValue = 0f)
            displayVolume()
        }
    }

    private var mVolumeControlLayoutValueTextView: CustomTypefaceableTextView? = null
}