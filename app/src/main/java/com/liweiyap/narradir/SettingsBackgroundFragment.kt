package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout

import androidx.activity.OnBackPressedCallback

import com.google.android.material.snackbar.BaseTransientBottomBar

import com.liweiyap.narradir.ui.IObserverListener
import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.ObserverButton
import com.liweiyap.narradir.ui.SnackbarWrapper
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView
import com.liweiyap.narradir.util.SnackbarBuilderFlag
import com.liweiyap.narradir.util.audio.BackgroundSoundTestMediaPlayer

import java.util.EnumSet

import kotlin.math.roundToInt

class SettingsBackgroundFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_background, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mVolumeControlLayoutValueTextView = view.findViewById(R.id.updownControlLayoutValue)
        val volumeIncreaseButton = view.findViewById<ObserverButton>(R.id.upControlButton)
        val volumeDecreaseButton = view.findViewById<ObserverButton>(R.id.downControlButton)
        val generalBackButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.generalBackButton)
        val mainButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.mainButton)
        val settingsTitle = view.findViewById<CustomTypefaceableTextView>(R.id.settingsTitleTextView)
        val volumeControlLayoutLabelTextView = view.findViewById<CustomTypefaceableTextView>(R.id.updownControlLayoutLabel)
        val backgroundSoundSelectionLayout = view.findViewById<LinearLayout>(R.id.backgroundSoundSelectionLayout)
        val backgroundSoundNoneButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundNoneButton)
        val backgroundSoundCardsButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundCardsButton)
        val backgroundSoundCricketsButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundCricketsButton)
        val backgroundSoundFireplaceButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundFireplaceButton)
        val backgroundSoundRainButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundRainButton)
        val backgroundSoundRainforestButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundRainforestButton)
        val backgroundSoundRainstormButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundRainstormButton)
        val backgroundSoundWolvesButton = view.findViewById<CustomTypefaceableCheckableObserverButton>(R.id.backgroundSoundWolvesButton)
        mNavBar = view.findViewById(R.id.nonStartingActivityLayoutNavBar)

        settingsTitle.setText(R.string.settings_title_background)
        volumeControlLayoutLabelTextView.setText(R.string.volume_control_layout_label)

        ViewGroupSingleTargetSelector.addSingleTargetSelection(viewGroup = backgroundSoundSelectionLayout)

        mSnackbar = SnackbarWrapper(requireContext())
        mBackgroundSoundTestMediaPlayer = BackgroundSoundTestMediaPlayer(requireContext())

        // ----------------------------------------------------------------------
        // init background sounds and background sound setters
        // ----------------------------------------------------------------------

        if (viewModel == null) {
            backgroundSoundNoneButton.performClick()
        }
        else {
            // does not look elegant but it's safe because resource ID integers are non-constant from Gradle version >4.
            when (viewModel!!.backgroundSoundName) {
                getString(R.string.backgroundsound_cards) -> backgroundSoundCardsButton.performClick()
                getString(R.string.backgroundsound_crickets) -> backgroundSoundCricketsButton.performClick()
                getString(R.string.backgroundsound_fireplace) -> backgroundSoundFireplaceButton.performClick()
                getString(R.string.backgroundsound_rain) -> backgroundSoundRainButton.performClick()
                getString(R.string.backgroundsound_rainforest) -> backgroundSoundRainforestButton.performClick()
                getString(R.string.backgroundsound_rainstorm) -> backgroundSoundRainstormButton.performClick()
                getString(R.string.backgroundsound_wolves) -> backgroundSoundWolvesButton.performClick()
                else -> backgroundSoundNoneButton.performClick()
            }
        }

        backgroundSoundNoneButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_none)) }
        backgroundSoundCardsButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_cards)) }
        backgroundSoundCricketsButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_crickets)) }
        backgroundSoundFireplaceButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_fireplace)) }
        backgroundSoundRainButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_rain)) }
        backgroundSoundRainforestButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_rainforest)) }
        backgroundSoundRainstormButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_rainstorm)) }
        backgroundSoundWolvesButton.addOnClickObserver { selectBackgroundSound(getString(R.string.backgroundsound_wolves)) }

        backgroundSoundCardsButton.addOnLongClickObserver { playBackgroundSound(getString(R.string.backgroundsound_cards)) }
        backgroundSoundCricketsButton.addOnLongClickObserver { playBackgroundSound(getString(R.string.backgroundsound_crickets)) }
        backgroundSoundFireplaceButton.addOnLongClickObserver { playBackgroundSound(getString(R.string.backgroundsound_fireplace)) }
        backgroundSoundRainButton.addOnLongClickObserver { playBackgroundSound(getString(R.string.backgroundsound_rain)) }
        backgroundSoundRainforestButton.addOnLongClickObserver { playBackgroundSound(getString(R.string.backgroundsound_rainforest)) }
        backgroundSoundRainstormButton.addOnLongClickObserver { playBackgroundSound(getString(R.string.backgroundsound_rainstorm)) }
        backgroundSoundWolvesButton.addOnLongClickObserver { playBackgroundSound(getString(R.string.backgroundsound_wolves)) }
        backgroundSoundNoneButton.addOnLongClickObserver { stopBackgroundSound() }

        // ----------------------------------------------------------------------
        // volume control
        // ----------------------------------------------------------------------

        displayVolume()

        volumeIncreaseButton.addOnClickObserver { increaseVolume() }
        volumeDecreaseButton.addOnClickObserver { decreaseVolume() }

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        for (childIdx: Int in 0 until backgroundSoundSelectionLayout.childCount) {
            val btn: CustomTypefaceableCheckableObserverButton = backgroundSoundSelectionLayout.getChildAt(childIdx) as CustomTypefaceableCheckableObserverButton
            addSoundToPlayOnButtonClick(btn = btn, isVolumeControl = false)
        }

        addSoundToPlayOnButtonClick(btn = generalBackButton, isVolumeControl = false)
        addSoundToPlayOnButtonClick(btn = mainButton, isVolumeControl = false)
        addSoundToPlayOnButtonClick(btn = volumeIncreaseButton, isVolumeControl = true)
        addSoundToPlayOnButtonClick(btn = volumeDecreaseButton, isVolumeControl = true)

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

    override fun onResume() {
        super.onResume()

        mBackgroundSoundTestMediaPlayer?.resume()
    }

    override fun onPause() {
        super.onPause()

        viewModel?.savePreferences()
        mBackgroundSoundTestMediaPlayer?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mSnackbar?.destroy()
        mSnackbar = null

        mBackgroundSoundTestMediaPlayer?.destroy()
        mBackgroundSoundTestMediaPlayer = null

        mVolumeControlLayoutValueTextView = null
        mNavBar = null
    }

    private fun addSoundToPlayOnButtonClick(btn: IObserverListener?, isVolumeControl: Boolean) {
        if (viewModel == null) {
            return
        }

        btn?.addOnClickObserver {
            mBackgroundSoundTestMediaPlayer?.let {
                if (isVolumeControl) {
                    it.setVolume(viewModel!!.backgroundSoundVolume)
                }
                else {
                    stopBackgroundSound()
                }
            }

            narradirControl?.playClickSound()
        }
    }

    private fun selectBackgroundSound(sound: String) {
        viewModel?.backgroundSoundName = sound
    }

    private fun playBackgroundSound(sound: String) {
        if ( (viewModel == null) || (mBackgroundSoundTestMediaPlayer == null) ) {
            return
        }

        mBackgroundSoundTestMediaPlayer!!.play(res = sound, volume = viewModel!!.backgroundSoundVolume, listener = null)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        showSnackbar()
    }

    private fun stopBackgroundSound() {
        mBackgroundSoundTestMediaPlayer?.let {
            it.stop()
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            dismissSnackbar()
        }
    }

    private fun displayVolume() {
        viewModel?.let {
            mVolumeControlLayoutValueTextView?.text = (it.backgroundSoundVolume * 10).roundToInt().toString()
        }
    }

    private fun increaseVolume() {
        viewModel?.let {
            it.backgroundSoundVolume = (it.backgroundSoundVolume + 0.1F).coerceAtMost(maximumValue = 1F)
            displayVolume()
        }
    }

    private fun decreaseVolume() {
        viewModel?.let {
            it.backgroundSoundVolume = (it.backgroundSoundVolume - 0.1F).coerceAtLeast(minimumValue = 0F)
            displayVolume()
        }
    }

    private fun showSnackbar() {
        if ( (viewModel == null) || (viewModel!!.doHideBackgroundSoundHint()) ) {
            return
        }

        try {
            mSnackbar?.show(
                view = mNavBar!!,
                message = getString(R.string.backgroundsound_mute_notification),
                duration = BaseTransientBottomBar.LENGTH_SHORT,
                actionMessage = getString(R.string.acknowledge_button_text),
                actionCallback = { viewModel!!.setHideBackgroundSoundHint(true) },
                flags = EnumSet.of(SnackbarBuilderFlag.SHOW_ABOVE_XY, SnackbarBuilderFlag.ACTION_DISMISSABLE))
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dismissSnackbar() {
        try {
            mSnackbar?.dismissOldSnackbar()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mSnackbar: SnackbarWrapper? = null
    private var mBackgroundSoundTestMediaPlayer: BackgroundSoundTestMediaPlayer? = null

    private var mVolumeControlLayoutValueTextView: CustomTypefaceableTextView? = null
    private var mNavBar: LinearLayout? = null
}