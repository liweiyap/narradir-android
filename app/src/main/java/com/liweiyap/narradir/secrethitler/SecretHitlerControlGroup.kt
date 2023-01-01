package com.liweiyap.narradir.secrethitler

import android.content.Context
import android.view.View
import android.widget.LinearLayout

import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.ObserverImageButton
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton
import com.liweiyap.narradir.util.CharacterSelectionControlGroup
import com.liweiyap.narradir.util.IObserver
import com.liweiyap.narradir.util.PlayerNumbers

class SecretHitlerControlGroup(
    activityContext: Context,
    playerNumberSelectionLayout: LinearLayout,
    p5Button: CustomTypefaceableCheckableObserverButton,
    p6Button: CustomTypefaceableCheckableObserverButton,
    p7Button: CustomTypefaceableCheckableObserverButton,
    p8Button: CustomTypefaceableCheckableObserverButton,
    p9Button: CustomTypefaceableCheckableObserverButton,
    p10Button: CustomTypefaceableCheckableObserverButton,
    liberal0Button: ObserverImageButton,
    liberal1Button: ObserverImageButton,
    liberal2Button: ObserverImageButton,
    liberal3Button: ObserverImageButton,
    liberal4Button: ObserverImageButton,
    liberal5Button: ObserverImageButton,
    hitlerButton: ObserverImageButton,
    fascist0Button: ObserverImageButton,
    fascist1Button: ObserverImageButton,
    fascist2Button: ObserverImageButton,
): CharacterSelectionControlGroup(activityContext) {
    private var mCharacterArray: SecretHitlerCharacterArray?

    init {
        mCharacterArray = SecretHitlerCharacterArray(
            liberal0Button, liberal1Button, liberal2Button, liberal3Button,
            liberal4Button, liberal5Button,
            hitlerButton, fascist0Button, fascist1Button, fascist2Button)

        addSnackbarMessages()

        ViewGroupSingleTargetSelector.addSingleTargetSelection(playerNumberSelectionLayout)

        // -----------------------------------------------------------------------------------------
        // adapt available characters according to player number
        // -----------------------------------------------------------------------------------------

        mPlayerNumberButtonArray!![PlayerNumbers.P5] = p5Button
        mPlayerNumberButtonArray!![PlayerNumbers.P6] = p6Button
        mPlayerNumberButtonArray!![PlayerNumbers.P7] = p7Button
        mPlayerNumberButtonArray!![PlayerNumbers.P8] = p8Button
        mPlayerNumberButtonArray!![PlayerNumbers.P9] = p9Button
        mPlayerNumberButtonArray!![PlayerNumbers.P10] = p10Button

        mPlayerNumberButtonArray!![PlayerNumbers.P5]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                mCharacterArray!!.expectedGoodTotal = 3
                mCharacterArray!!.expectedEvilTotal = 2

                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL3).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL4).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL5).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST1).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST2).visibility = View.INVISIBLE

                mCharacterArray!!.checkPlayerComposition("SecretHitlerControlGroup::p5Button.onClick()")
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P6]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                mCharacterArray!!.expectedGoodTotal = 4
                mCharacterArray!!.expectedEvilTotal = 2

                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL3).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL4).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL5).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST1).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST2).visibility = View.INVISIBLE

                mCharacterArray!!.checkPlayerComposition("SecretHitlerControlGroup::p6Button.onClick()")
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P7]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                mCharacterArray!!.expectedGoodTotal = 4
                mCharacterArray!!.expectedEvilTotal = 3

                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL3).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL4).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL5).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST1).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST2).visibility = View.INVISIBLE

                mCharacterArray!!.checkPlayerComposition("SecretHitlerControlGroup::p7Button.onClick()")
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P8]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                mCharacterArray!!.expectedGoodTotal = 5
                mCharacterArray!!.expectedEvilTotal = 3

                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL3).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL4).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL5).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST1).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST2).visibility = View.INVISIBLE

                mCharacterArray!!.checkPlayerComposition("SecretHitlerControlGroup::p8Button.onClick()")
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P9]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                mCharacterArray!!.expectedGoodTotal = 5
                mCharacterArray!!.expectedEvilTotal = 4

                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL3).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL4).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL5).visibility = View.INVISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST1).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST2).visibility = View.VISIBLE

                mCharacterArray!!.checkPlayerComposition("SecretHitlerControlGroup::p9Button.onClick()")
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P10]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                mCharacterArray!!.expectedGoodTotal = 6
                mCharacterArray!!.expectedEvilTotal = 4

                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL3).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL4).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL5).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST1).visibility = View.VISIBLE
                mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST2).visibility = View.VISIBLE

                mCharacterArray!!.checkPlayerComposition("SecretHitlerControlGroup::p10Button.onClick()")
            }
        })

        // -----------------------------------------------------------------------------------------
        // MediaPlayer for character descriptions
        // -----------------------------------------------------------------------------------------

        addCharacterDescriptions()
    }

    override fun destroy() {
        super.destroy()

        mCharacterArray?.destroy()
        mCharacterArray = null
    }

    private fun addCharacterDescriptions() {
        if (mCharacterArray == null) {
            throw RuntimeException("SecretHitlerControlGroup::addCharacterDescriptions(): mCharacterArray is NULL")
        }

        if (mActivityContext == null) {
            return
        }

        try {
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL0).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(
                R.string.liberaldescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL1).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.liberaldescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL2).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.liberaldescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL3).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.liberaldescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL4).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.liberaldescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.LIBERAL5).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.liberaldescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.HITLER).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.hitlerdescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST0).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.fascistdescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST1).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.fascistdescription_key)) } })
            mCharacterArray!!.getCharacter(SecretHitlerCharacterName.FASCIST2).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.fascistdescription_key)) } })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addSnackbarMessages() {
        if (mCharacterArray == null) {
            throw RuntimeException("SecretHitlerControlGroup::addSnackbarMessages(): mCharacterArray is NULL")
        }

        mCharacterArray!!.characterImageButtonArray?.let {
            for (btn: ObserverImageButton? in it) {
                btn?.addOnClickObserver(object: IObserver { override fun update() { showSnackbar(mActivityContext!!.getString(R.string.secrethitler_all_notification)) } })
            }
        }
    }

    val expectedGoodTotal: Int
        get() {
            if (mCharacterArray == null) {
                throw RuntimeException("SecretHitlerControlGroup::getExpectedGoodTotal(): mCharacterArray is NULL")
            }

            return mCharacterArray!!.expectedGoodTotal
        }

    val expectedEvilTotal: Int
        get() {
            if (mCharacterArray == null) {
                throw RuntimeException("SecretHitlerControlGroup::getExpectedEvilTotal(): mCharacterArray is NULL")
            }

            return mCharacterArray!!.expectedEvilTotal
        }
}