package com.liweiyap.narradir.avalon

import android.content.Context
import android.view.View
import android.widget.LinearLayout

import androidx.core.util.Pair

import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.CheckableObserverImageButton
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton
import com.liweiyap.narradir.util.CharacterSelectionControlGroup
import com.liweiyap.narradir.util.IObserver
import com.liweiyap.narradir.util.PlayerNumbers

class AvalonControlGroup(
    activityContext: Context,
    playerNumberSelectionLayout: LinearLayout,
    p5Button: CustomTypefaceableCheckableObserverButton,
    p6Button: CustomTypefaceableCheckableObserverButton,
    p7Button: CustomTypefaceableCheckableObserverButton,
    p8Button: CustomTypefaceableCheckableObserverButton,
    p9Button: CustomTypefaceableCheckableObserverButton,
    p10Button: CustomTypefaceableCheckableObserverButton,
    merlinButton: CheckableObserverImageButton,
    percivalButton: CheckableObserverImageButton,
    loyal0Button: CheckableObserverImageButton,
    loyal1Button: CheckableObserverImageButton,
    loyal2Button: CheckableObserverImageButton,
    loyal3Button: CheckableObserverImageButton,
    loyal4Button: CheckableObserverImageButton,
    loyal5Button: CheckableObserverImageButton,
    assassinButton: CheckableObserverImageButton,
    morganaButton: CheckableObserverImageButton,
    mordredButton: CheckableObserverImageButton,
    oberonButton: CheckableObserverImageButton,
    minion0Button: CheckableObserverImageButton,
    minion1Button: CheckableObserverImageButton,
    minion2Button: CheckableObserverImageButton,
    minion3Button: CheckableObserverImageButton,
): CharacterSelectionControlGroup(activityContext) {
    private var mCharacterSelectionRules: AvalonCharacterSelectionRules?

    init {
        mCharacterSelectionRules = AvalonCharacterSelectionRules(
            merlinButton, percivalButton, loyal0Button, loyal1Button,
            loyal2Button, loyal3Button, loyal4Button, loyal5Button,
            assassinButton, morganaButton, mordredButton, oberonButton,
            minion0Button, minion1Button, minion2Button, minion3Button)

        addSnackbarMessages()

        ViewGroupSingleTargetSelector.addSingleTargetSelection(viewGroup = playerNumberSelectionLayout)

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
                // new - old
                val playerNumberChange: Int = 5 - (expectedGoodTotal + expectedEvilTotal)

                if (playerNumberChange < 0) {  // new < old (decrease)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 5)
                }

                getCharacter(AvalonCharacterName.LOYAL3).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.LOYAL4).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.LOYAL5).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.MINION2).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.MINION3).visibility = View.INVISIBLE

                if (playerNumberChange > 0) {  // new > old (increase)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 5)
                }
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P6]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                // new - old
                val playerNumberChange: Int = 6 - (expectedGoodTotal + expectedEvilTotal)

                if (playerNumberChange < 0) {  // new < old (decrease)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 6)
                }

                getCharacter(AvalonCharacterName.LOYAL3).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL4).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.LOYAL5).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.MINION2).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.MINION3).visibility = View.INVISIBLE

                if (playerNumberChange > 0) {  // new > old (increase)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 6)
                }
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P7]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                // new - old
                val playerNumberChange: Int = 7 - (expectedGoodTotal + expectedEvilTotal)

                if (playerNumberChange < 0) {  // new < old (decrease)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 7)
                }

                getCharacter(AvalonCharacterName.LOYAL3).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL4).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.LOYAL5).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.MINION2).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.MINION3).visibility = View.INVISIBLE

                if (playerNumberChange > 0) {  // new > old (increase)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 7)
                }
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P8]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                // new - old
                val playerNumberChange: Int = 8 - (expectedGoodTotal + expectedEvilTotal)

                if (playerNumberChange < 0) {  // new < old (decrease)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 8)
                }

                getCharacter(AvalonCharacterName.LOYAL3).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL4).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL5).visibility = View.INVISIBLE
                getCharacter(AvalonCharacterName.MINION2).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.MINION3).visibility = View.INVISIBLE

                if (playerNumberChange > 0) {  // new > old (increase)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 8)
                }
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P9]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                // new - old
                val playerNumberChange: Int = 9 - (expectedGoodTotal + expectedEvilTotal)

                if (playerNumberChange < 0) {  // new < old (decrease)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 9)
                }

                getCharacter(AvalonCharacterName.LOYAL3).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL4).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL5).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.MINION2).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.MINION3).visibility = View.INVISIBLE

                if (playerNumberChange > 0) {  // new > old (increase)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 9)
                }
            }
        })

        mPlayerNumberButtonArray!![PlayerNumbers.P10]!!.addOnClickObserver(object: IObserver {
            override fun update() {
                // new - old
                val playerNumberChange: Int = 10 - (expectedGoodTotal + expectedEvilTotal)

                if (playerNumberChange < 0) {  // new < old (decrease)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 10)
                }

                getCharacter(AvalonCharacterName.LOYAL3).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL4).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.LOYAL5).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.MINION2).visibility = View.VISIBLE
                getCharacter(AvalonCharacterName.MINION3).visibility = View.VISIBLE

                if (playerNumberChange > 0) {  // new > old (increase)
                    playerNumberSelectionLayoutChecker(newPlayerNumber = 10)
                }
            }
        })

        // -----------------------------------------------------------------------------------------
        // MediaPlayer for character descriptions
        // -----------------------------------------------------------------------------------------

        addCharacterDescriptions()
    }

    override fun destroy() {
        super.destroy()

        mCharacterSelectionRules?.destroy()
        mCharacterSelectionRules = null
    }

    private fun playerNumberSelectionLayoutChecker(newPlayerNumber: Int) {
        if ((newPlayerNumber < 5) || (newPlayerNumber > 10)) {
            throw RuntimeException(
                "AvalonControlGroup::playerNumberSelectionLayoutChecker(): " +
                    "Invalid input $newPlayerNumber")
        }

        val newPlayerComposition: Pair<Int?, Int?> = getPlayerComposition(playerNumber = newPlayerNumber)
        if ( (newPlayerComposition.first == null) || (newPlayerComposition.second == null) ) {
            throw RuntimeException(
                "AvalonControlGroup::playerNumberSelectionLayoutChecker(): getPlayerComposition() returned a pair of NULL")
        }

        if (mCharacterSelectionRules == null) {
            throw RuntimeException(
                "AvalonControlGroup::playerNumberSelectionLayoutChecker(): mCharacterSelectionRules is NULL")
        }

        val newExpectedGoodTotal: Int = newPlayerComposition.first!!
        val newExpectedEvilTotal: Int = newPlayerComposition.second!!

        val expectedGoodChange: Int = newExpectedGoodTotal - expectedGoodTotal  // new - old
        val expectedEvilChange: Int = newExpectedEvilTotal - expectedEvilTotal  // new - old

        if (expectedGoodChange > 0) {  // new > old (increase)
            mCharacterSelectionRules!!.searchAndCheckNewCharacters(AvalonCharacterName.LOYAL0, AvalonCharacterName.LOYAL5, expectedGoodChange)
        }
        else {  // new < old (decrease)
            mCharacterSelectionRules!!.searchAndUncheckOldCharacters(AvalonCharacterName.LOYAL5, AvalonCharacterName.LOYAL0, -expectedGoodChange)
        }

        if (expectedEvilChange > 0) {  // new > old (increase)
            mCharacterSelectionRules!!.searchAndCheckNewCharacters(AvalonCharacterName.MINION0, AvalonCharacterName.MINION3, expectedEvilChange)
        }
        else {  // new < old (decrease)
            mCharacterSelectionRules!!.searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORGANA, -expectedEvilChange)
        }

        // update old values to new values
        mCharacterSelectionRules!!.expectedGoodTotal = newExpectedGoodTotal
        mCharacterSelectionRules!!.expectedEvilTotal = newExpectedEvilTotal

        // callback for externally-driven selection rules
        mCharacterSelectionRules!!.onPlayerNumberChange()
    }

    private fun getPlayerComposition(playerNumber: Int): Pair<Int?, Int?> {
        if ((playerNumber < 5) || (playerNumber > 10)) {
            throw RuntimeException(
                "AvalonControlGroup::getPlayerComposition(): " +
                    "Invalid input $playerNumber")
        }

        return when (playerNumber) {
            5 -> Pair(3, 2)
            6 -> Pair(4, 2)
            7 -> Pair(4, 3)
            8 -> Pair(5, 3)
            9 -> Pair(6, 3)
            10 -> Pair(6, 4)
            else -> Pair(null, null)
        }
    }

    private fun addCharacterDescriptions() {
        if (mActivityContext == null) {
            return
        }

        try {
            getCharacter(AvalonCharacterName.MERLIN).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.merlindescription_key)) } })
            getCharacter(AvalonCharacterName.PERCIVAL).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.percivaldescription_key)) } })
            getCharacter(AvalonCharacterName.LOYAL0).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.loyaldescription_key)) } })
            getCharacter(AvalonCharacterName.LOYAL1).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.loyaldescription_key)) } })
            getCharacter(AvalonCharacterName.LOYAL2).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.loyaldescription_key)) } })
            getCharacter(AvalonCharacterName.LOYAL3).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.loyaldescription_key)) } })
            getCharacter(AvalonCharacterName.LOYAL4).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.loyaldescription_key)) } })
            getCharacter(AvalonCharacterName.LOYAL5).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.loyaldescription_key)) } })
            getCharacter(AvalonCharacterName.ASSASSIN).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.assassindescription_key)) } })
            getCharacter(AvalonCharacterName.MORGANA).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.morganadescription_key)) } })
            getCharacter(AvalonCharacterName.MORDRED).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.mordreddescription_key)) } })
            getCharacter(AvalonCharacterName.OBERON).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.oberondescription_key)) } })
            getCharacter(AvalonCharacterName.MINION0).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.miniondescription_key)) } })
            getCharacter(AvalonCharacterName.MINION1).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.miniondescription_key)) } })
            getCharacter(AvalonCharacterName.MINION2).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.miniondescription_key)) } })
            getCharacter(AvalonCharacterName.MINION3).addOnLongClickObserver(object: IObserver { override fun update() { startCharacterDescriptionMediaPlayer(mActivityContext!!.getString(R.string.miniondescription_key)) } })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addSnackbarMessages() {
        if (mCharacterSelectionRules == null) {
            throw RuntimeException("AvalonControlGroup::addSnackbarMessages(): mCharacterSelectionRules is NULL")
        }

        if (mActivityContext == null) {
            return
        }

        for (idx in AvalonCharacterName.LOYAL0 .. AvalonCharacterName.LOYAL5) {
            getCharacter(idx).addOnClickObserver(object: IObserver { override fun update() { showSnackbar(mActivityContext!!.getString(R.string.avalon_loyal_notification)) } })
        }

        for (idx in AvalonCharacterName.MINION0 .. AvalonCharacterName.MINION3) {
            getCharacter(idx).addOnClickObserver(object: IObserver { override fun update() { showSnackbar(mActivityContext!!.getString(R.string.avalon_minion_notification)) } })
        }
    }

    fun getCharacter(idx: Int): CheckableObserverImageButton {
        if (mCharacterSelectionRules == null) {
            throw RuntimeException("AvalonControlGroup::getCharacter(): mCharacterSelectionRules is NULL")
        }

        return mCharacterSelectionRules!!.getCharacter(idx)
    }

    val characterImageButtonArray: Array<CheckableObserverImageButton?>?
        get() {
            if (mCharacterSelectionRules == null) {
                throw RuntimeException("AvalonControlGroup::getCharacterImageButtonArray(): mCharacterSelectionRules is NULL")
            }

            return mCharacterSelectionRules!!.characterImageButtonArray
        }

    val expectedGoodTotal: Int
        get() {
            if (mCharacterSelectionRules == null) {
                throw RuntimeException("AvalonControlGroup::getExpectedGoodTotal(): mCharacterSelectionRules is NULL")
            }

            return mCharacterSelectionRules!!.expectedGoodTotal
        }

    val expectedEvilTotal: Int
        get() {
            if (mCharacterSelectionRules == null) {
                throw RuntimeException("AvalonControlGroup::getExpectedEvilTotal(): mCharacterSelectionRules is NULL")
            }

            return mCharacterSelectionRules!!.expectedEvilTotal
        }

    fun checkPlayerComposition(callingFuncName: String?) {
        if (mCharacterSelectionRules == null) {
            throw RuntimeException("AvalonControlGroup::checkPlayerComposition(): mCharacterSelectionRules is NULL")
        }

        mCharacterSelectionRules!!.checkPlayerComposition(callingFuncName)
    }
}