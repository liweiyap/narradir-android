package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.LinearLayout

import androidx.fragment.app.Fragment

import com.liweiyap.narradir.avalon.AvalonControlGroup
import com.liweiyap.narradir.ui.CheckableObserverImageButton

/**
 * Gets rid of RuntimeException("NarradirFragmentBase::onAttach(): Programming error: Context is not INarradirControl.")
 * due to original Fragment not being attached to Activity that implements INarradirControl interface.
 */
class MockAvalonCharacterSelectionFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_selection_avalon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val playerNumberSelectionLayout = view.findViewById<LinearLayout>(R.id.playerNumberSelectionLayout)

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
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mAvalonControlGroup?.destroy()
        mAvalonControlGroup = null
    }

    fun getCharacter(idx: Int): CheckableObserverImageButton {
        if (mAvalonControlGroup == null) {
            throw RuntimeException("MockAvalonCharacterSelectionFragment::getCharacter(): mAvalonControlGroup is NULL")
        }

        return mAvalonControlGroup!!.getCharacter(idx)
    }

    private var mAvalonControlGroup: AvalonControlGroup? = null
}