package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import androidx.activity.OnBackPressedCallback

import com.liweiyap.narradir.secrethitler.SecretHitlerControlGroup
import com.liweiyap.narradir.ui.IObserverListener
import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.ObserverImageButton
import com.liweiyap.narradir.ui.TextViewAutosizeHelper
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton

import java.util.ArrayList

class SecretHitlerCharacterSelectionFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_selection_secrethitler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val playerNumberSelectionLayout = view.findViewById<LinearLayout>(R.id.playerNumberSelectionLayout)
        val gameSwitcherButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.characterSelectionLayoutGameSwitcherButton)
        val playButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.characterSelectionLayoutPlayButton)
        val settingsButton = view.findViewById<ObserverImageButton>(R.id.characterSelectionLayoutSettingsButton)

        // -----------------------------------------------------------------------------------------
        // character image button array, player number selection layout, character selection layouts
        // -----------------------------------------------------------------------------------------

        mSecretHitlerControlGroup = SecretHitlerControlGroup(
            requireActivity(),
            playerNumberSelectionLayout,
            view.findViewById(R.id.p5Button), view.findViewById(R.id.p6Button), view.findViewById(R.id.p7Button),
            view.findViewById(R.id.p8Button), view.findViewById(R.id.p9Button), view.findViewById(R.id.p10Button),
            view.findViewById(R.id.liberal0Button), view.findViewById(R.id.liberal1Button), view.findViewById(R.id.liberal2Button), view.findViewById(R.id.liberal3Button),
            view.findViewById(R.id.liberal4Button), view.findViewById(R.id.liberal5Button),
            view.findViewById(R.id.hitlerButton), view.findViewById(R.id.fascist0Button), view.findViewById(R.id.fascist1Button), view.findViewById(R.id.fascist2Button))

        applyPreferences()

        // -----------------------------------------------------------------------------------------
        // sounds
        // -----------------------------------------------------------------------------------------

        for (childIdx: Int in 0 until playerNumberSelectionLayout.childCount) {
            val btn: CustomTypefaceableCheckableObserverButton = playerNumberSelectionLayout.getChildAt(childIdx) as CustomTypefaceableCheckableObserverButton
            addSoundToPlayOnButtonClick(btn)
        }

        addSoundToPlayOnButtonClick(gameSwitcherButton)
        addSoundToPlayOnButtonClick(playButton)
        addSoundToPlayOnButtonClick(settingsButton)

        // -----------------------------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // -----------------------------------------------------------------------------------------

        gameSwitcherButton.text = getString(R.string.game_switcher_button_avalon)
        gameSwitcherButton.addOnClickObserver { navigateToAvalonCharacterSelectionFragment() }

        playButton.addOnClickObserver { navigateToPlayIntroductionFragment() }
        settingsButton.addOnClickObserver { navigateToFragment(resId = R.id.actionSecretHitlerToSettingsHome) }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mNarradirControl?.navigateAwayFromApp()
            }
        })

        // -----------------------------------------------------------------------------------------
        // auto-sizing TextViews
        // -----------------------------------------------------------------------------------------

        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(gameSwitcherButton)
    }

    override fun onResume() {
        super.onResume()
        mSecretHitlerControlGroup?.resumeCharacterDescriptionMediaPlayer()
    }

    override fun onPause() {
        super.onPause()

        savePreferences()  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873

        mSecretHitlerControlGroup?.pauseCharacterDescriptionMediaPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mSecretHitlerControlGroup?.destroy()
        mSecretHitlerControlGroup = null
    }

    override fun addSoundToPlayOnButtonClick(btn: IObserverListener?) {
        btn?.addOnClickObserver {
            mSecretHitlerControlGroup?.stopCharacterDescriptionMediaPlayer()
            mNarradirControl?.playClickSound()
        }
    }

    private fun navigateToAvalonCharacterSelectionFragment() {
        viewModel?.saveAvalonLastSelected()

        navigateToFragment(resId = R.id.actionSecretHitlerToAvalon)
    }

    // Since we have a ViewModel, we could actually navigate to PlayIntroductionFragment without a Bundle,
    // just like how we navigate to other Fragments without one.
    // However, using a Bundle is just a trick to prevent the app from starting from the background with PlayIntroductionFragment,
    // which I as the developer personally dislike, with the audio already playing from the get-go.
    // See PlayIntroductionFragment::onViewCreated() for more details.
    private fun navigateToPlayIntroductionFragment() {
        if (mSecretHitlerControlGroup == null) {
            throw RuntimeException("SecretHitlerCharacterSelectionFragment::navigateToPlayIntroductionFragment(): mSecretHitlerControlGroup is NULL")
        }

        val introSegmentArrayList = ArrayList<String>()

        if (mSecretHitlerControlGroup!!.expectedGoodTotal + mSecretHitlerControlGroup!!.expectedEvilTotal < 7) {
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment0small_key))
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment1small_key))
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment2small_key))
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment3small_key))
        }
        else {
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment0large_key))
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment1large_key))
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment2large_key))
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment3large_key))
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment4large_key))
        }

        val bundle = Bundle()
        bundle.putStringArrayList(getString(R.string.intro_segments_key), introSegmentArrayList)
        bundle.putBoolean(getString(R.string.is_started_from_avalon_key), false)

        navigateToFragment(resId = R.id.actionSecretHitlerToPlayIntro, args = bundle)
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private fun savePreferences() {
        if (mSecretHitlerControlGroup == null) {
            throw RuntimeException("SecretHitlerCharacterSelectionFragment::savePreferences(): mSecretHitlerControlGroup is NULL")
        }

        viewModel?.mSecretHitlerExpectedGoodTotal = mSecretHitlerControlGroup!!.expectedGoodTotal
        viewModel?.mSecretHitlerExpectedEvilTotal = mSecretHitlerControlGroup!!.expectedEvilTotal
        viewModel?.savePreferences()
    }

    private fun applyPreferences() {
        if (mSecretHitlerControlGroup == null) {
            throw RuntimeException("SecretHitlerCharacterSelectionFragment::applyPreferences(): mSecretHitlerControlGroup is NULL")
        }

        viewModel?.let {
            mSecretHitlerControlGroup!!.selectPlayerNumberButton(playerNumber = it.mSecretHitlerExpectedGoodTotal + it.mSecretHitlerExpectedEvilTotal)
        }
    }

    private var mSecretHitlerControlGroup: SecretHitlerControlGroup? = null
}