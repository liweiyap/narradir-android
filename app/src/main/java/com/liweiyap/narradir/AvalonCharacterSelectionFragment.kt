package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import com.liweiyap.narradir.avalon.AvalonCharacterName
import com.liweiyap.narradir.avalon.AvalonControlGroup
import com.liweiyap.narradir.ui.IObserverListener
import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.ObserverImageButton
import com.liweiyap.narradir.ui.TextViewAutosizeHelper
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton

import java.util.ArrayList

class AvalonCharacterSelectionFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_selection_avalon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val playerNumberSelectionLayout = view.findViewById<LinearLayout>(R.id.playerNumberSelectionLayout)
        val gameSwitcherButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.characterSelectionLayoutGameSwitcherButton)
        val playButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.characterSelectionLayoutPlayButton)
        val settingsButton = view.findViewById<ObserverImageButton>(R.id.characterSelectionLayoutSettingsButton)

        // -----------------------------------------------------------------------------------------
        // Avalon is the default game but another game might have been the last selected.
        // If last selected game is not the default game, then switch game.
        // -----------------------------------------------------------------------------------------

        gameSwitcherButton.text = getString(R.string.game_switcher_button_secrethitler)
        gameSwitcherButton.addOnClickObserver { navigateToSecretHitlerCharacterSelectionFragment() }
        if ( (viewModel != null) && (!viewModel!!.isAvalonLastSelected) ) {
            gameSwitcherButton.performClick()
        }

        // -----------------------------------------------------------------------------------------
        // character image button array, player number selection layout, character selection layouts
        // -----------------------------------------------------------------------------------------

        mAvalonControlGroup = AvalonControlGroup(
            requireActivity(),
            playerNumberSelectionLayout,
            view.findViewById(R.id.p5Button), view.findViewById(R.id.p6Button), view.findViewById(R.id.p7Button),
            view.findViewById(R.id.p8Button), view.findViewById(R.id.p9Button), view.findViewById(R.id.p10Button),
            view.findViewById(R.id.merlinButton), view.findViewById(R.id.percivalButton), view.findViewById(R.id.loyal0Button), view.findViewById(R.id.loyal1Button),
            view.findViewById(R.id.loyal2Button), view.findViewById(R.id.loyal3Button), view.findViewById(R.id.loyal4Button), view.findViewById(R.id.loyal5Button),
            view.findViewById(R.id.assassinButton), view.findViewById(R.id.morganaButton), view.findViewById(R.id.mordredButton), view.findViewById(R.id.oberonButton),
            view.findViewById(R.id.minion0Button), view.findViewById(R.id.minion1Button), view.findViewById(R.id.minion2Button), view.findViewById(R.id.minion3Button))

        applyPreferences()

        // -----------------------------------------------------------------------------------------
        // sounds
        // -----------------------------------------------------------------------------------------

        for (childIdx: Int in 0 until playerNumberSelectionLayout.childCount) {
            val btn: CustomTypefaceableCheckableObserverButton = playerNumberSelectionLayout.getChildAt(childIdx) as CustomTypefaceableCheckableObserverButton
            addSoundToPlayOnButtonClick(btn)
        }

        addSoundToPlayOnButtonClick(mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN))
        addSoundToPlayOnButtonClick(mAvalonControlGroup!!.getCharacter(AvalonCharacterName.PERCIVAL))
        addSoundToPlayOnButtonClick(mAvalonControlGroup!!.getCharacter(AvalonCharacterName.ASSASSIN))
        addSoundToPlayOnButtonClick(mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORGANA))
        addSoundToPlayOnButtonClick(mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORDRED))
        addSoundToPlayOnButtonClick(mAvalonControlGroup!!.getCharacter(AvalonCharacterName.OBERON))
        addSoundToPlayOnButtonClick(gameSwitcherButton)
        addSoundToPlayOnButtonClick(playButton)
        addSoundToPlayOnButtonClick(settingsButton)

        // -----------------------------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // -----------------------------------------------------------------------------------------

        playButton.addOnClickObserver { navigateToPlayIntroductionFragment() }
        settingsButton.addOnClickObserver { navigateToSettingsHomeFragment() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mNarradirControl?.navigateAwayFromApp()
            }
        })

        // -----------------------------------------------------------------------------------------
        // auto-sizing TextViews
        // -----------------------------------------------------------------------------------------

        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(view.findViewById(R.id.gameHintTextView))
        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(gameSwitcherButton)
    }

    override fun onResume() {
        super.onResume()
        mAvalonControlGroup?.resumeCharacterDescriptionMediaPlayer()
    }

    override fun onPause() {
        super.onPause()

        savePreferences()  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873

        mAvalonControlGroup?.pauseCharacterDescriptionMediaPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mAvalonControlGroup?.destroy()
        mAvalonControlGroup = null
    }

    override fun addSoundToPlayOnButtonClick(btn: IObserverListener?) {
        btn?.addOnClickObserver {
            mAvalonControlGroup?.stopCharacterDescriptionMediaPlayer()
            mNarradirControl?.playClickSound()
        }
    }

    private fun navigateToSecretHitlerCharacterSelectionFragment() {
        viewModel?.saveSecretHitlerLastSelected()

        val navController: NavController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.secretHitlerCharacterSelectionFragment)
    }

    private fun navigateToPlayIntroductionFragment() {
        if (mAvalonControlGroup == null) {
            throw RuntimeException("AvalonCharacterSelectionFragment::navigateToPlayIntroductionFragment(): mAvalonControlGroup is NULL")
        }

        val introSegmentArrayList = ArrayList<String>()

        introSegmentArrayList.add(getString(
            if (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN).isChecked)
                R.string.avalonintrosegment0withmerlin_key
            else
                R.string.avalonintrosegment0nomerlin_key))

        introSegmentArrayList.add(getString(
            if (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.OBERON).isChecked)
                R.string.avalonintrosegment1withoberon_key
            else
                R.string.avalonintrosegment1nooberon_key))

        introSegmentArrayList.add(getString(R.string.avalonintrosegment2_key))

        if (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN).isChecked) {
            introSegmentArrayList.add(getString(
                if (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORDRED).isChecked)
                    R.string.avalonintrosegment3withmordred_key
                else
                    R.string.avalonintrosegment3nomordred_key))

            introSegmentArrayList.add(getString(R.string.avalonintrosegment4_key))

            if (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.PERCIVAL).isChecked) {
                val isMorganaChecked: Boolean = mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORGANA).isChecked

                introSegmentArrayList.add(getString(
                    if (isMorganaChecked)
                        R.string.avalonintrosegment5withpercivalwithmorgana_key
                    else
                        R.string.avalonintrosegment5withpercivalnomorgana_key))

                introSegmentArrayList.add(getString(
                    if (isMorganaChecked)
                        R.string.avalonintrosegment6withpercivalwithmorgana_key
                    else
                        R.string.avalonintrosegment6withpercivalnomorgana_key))

                introSegmentArrayList.add(getString(R.string.avalonintrosegment7_key))
            }
            else {
                introSegmentArrayList.add(getString(R.string.avalonintrosegment5nopercival_key))
            }
        }
        else {
            introSegmentArrayList.add(getString(R.string.avalonintrosegment3nomerlin_key))
        }

        val bundle = Bundle()
        bundle.putStringArrayList(getString(R.string.intro_segments_key), introSegmentArrayList)
        bundle.putBoolean(getString(R.string.is_started_from_avalon_key), true)

        val navController: NavController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.playIntroductionFragment, bundle)
    }

    private fun navigateToSettingsHomeFragment() {
        val navController: NavController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.settingsHomeFragment)
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private fun savePreferences() {
        if (mAvalonControlGroup == null) {
            throw RuntimeException("AvalonCharacterSelectionFragment::savePreferences(): mAvalonControlGroup is NULL")
        }

        if (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN).isChecked != mAvalonControlGroup!!.getCharacter(AvalonCharacterName.ASSASSIN).isChecked) {
            throw RuntimeException(
                "AvalonCharacterSelectionFragment::savePreferences(): " +
                    "Checked state of Assassin should be identical to that of Merlin")
        }

        viewModel?.mAvalonExpectedGoodTotal = mAvalonControlGroup!!.expectedGoodTotal
        viewModel?.mAvalonExpectedEvilTotal = mAvalonControlGroup!!.expectedEvilTotal
        viewModel?.mIsMerlinChecked = mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN).isChecked
        viewModel?.mIsPercivalChecked = mAvalonControlGroup!!.getCharacter(AvalonCharacterName.PERCIVAL).isChecked
        viewModel?.mIsMorganaChecked = mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORGANA).isChecked
        viewModel?.mIsMordredChecked = mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORDRED).isChecked
        viewModel?.mIsOberonChecked = mAvalonControlGroup!!.getCharacter(AvalonCharacterName.OBERON).isChecked
        viewModel?.savePreferences()
    }

    private fun applyPreferences() {
        if (mAvalonControlGroup == null) {
            throw RuntimeException("AvalonCharacterSelectionFragment::applyPreferences(): mAvalonControlGroup is NULL")
        }

        if ( (mAvalonControlGroup!!.characterImageButtonArray == null) || (mAvalonControlGroup!!.playerNumberButtonArray == null) ) {
            return
        }

        viewModel?.let {
            mAvalonControlGroup!!.selectPlayerNumberButton(playerNumber = it.mAvalonExpectedGoodTotal + it.mAvalonExpectedEvilTotal)

            // default: Merlin is checked
            // saved preferences: if Merlin is not checked
            if ( (!it.mIsMerlinChecked) && (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN).isChecked) ) {
                mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN).performClick()
            }

            // default: Percival is not checked
            // saved preferences: if Percival is checked
            if ( (it.mIsPercivalChecked) && (!mAvalonControlGroup!!.getCharacter(AvalonCharacterName.PERCIVAL).isChecked) ) {
                mAvalonControlGroup!!.getCharacter(AvalonCharacterName.PERCIVAL).performClick()
            }

            // default: Morgana is not checked
            // saved preferences: if Morgana is checked
            if ( (it.mIsMorganaChecked) && (!mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORGANA).isChecked) ) {
                mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORGANA).performClick()
            }

            // default: Mordred is not checked
            // saved preferences: if Mordred is checked
            if ( (it.mIsMordredChecked) && (!mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORDRED).isChecked) ) {
                mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MORDRED).performClick()
            }

            // default: Oberon is not checked
            // saved preferences: if Oberon is checked
            if ( (it.mIsOberonChecked) && (!mAvalonControlGroup!!.getCharacter(AvalonCharacterName.OBERON).isChecked) ) {
                mAvalonControlGroup!!.getCharacter(AvalonCharacterName.OBERON).performClick()
            }

            mAvalonControlGroup!!.checkPlayerComposition(callingFuncName = "AvalonCharacterSelectionFragment::applyPreferences()")

            if (mAvalonControlGroup!!.getCharacter(AvalonCharacterName.MERLIN).isChecked != mAvalonControlGroup!!.getCharacter(AvalonCharacterName.ASSASSIN).isChecked) {
                throw RuntimeException(
                    "AvalonCharacterSelectionFragment::applyPreferences(): " +
                        "Checked state of Assassin should be identical to that of Merlin")
            }
        }
    }

    private var mAvalonControlGroup: AvalonControlGroup? = null
}