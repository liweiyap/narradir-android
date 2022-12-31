package com.liweiyap.narradir.avalon

import android.view.View

import com.liweiyap.narradir.ui.CheckableObserverImageButton
import com.liweiyap.narradir.util.IObserver

class AvalonCharacterSelectionRules(
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
) {
    var characterImageButtonArray: Array<CheckableObserverImageButton?>? = arrayOfNulls(AvalonCharacterName.getNumberOfCharacters())
        private set

    var expectedGoodTotal: Int = AvalonCharacterName.getDefaultNumberOfGoodCharacters()
    var expectedEvilTotal: Int = AvalonCharacterName.getDefaultNumberOfEvilCharacters()

    init {
        characterImageButtonArray!![AvalonCharacterName.MERLIN] = merlinButton
        characterImageButtonArray!![AvalonCharacterName.PERCIVAL] = percivalButton
        characterImageButtonArray!![AvalonCharacterName.LOYAL0] = loyal0Button
        characterImageButtonArray!![AvalonCharacterName.LOYAL1] = loyal1Button
        characterImageButtonArray!![AvalonCharacterName.LOYAL2] = loyal2Button
        characterImageButtonArray!![AvalonCharacterName.LOYAL3] = loyal3Button
        characterImageButtonArray!![AvalonCharacterName.LOYAL4] = loyal4Button
        characterImageButtonArray!![AvalonCharacterName.LOYAL5] = loyal5Button
        characterImageButtonArray!![AvalonCharacterName.ASSASSIN] = assassinButton
        characterImageButtonArray!![AvalonCharacterName.MORGANA] = morganaButton
        characterImageButtonArray!![AvalonCharacterName.MORDRED] = mordredButton
        characterImageButtonArray!![AvalonCharacterName.OBERON] = oberonButton
        characterImageButtonArray!![AvalonCharacterName.MINION0] = minion0Button
        characterImageButtonArray!![AvalonCharacterName.MINION1] = minion1Button
        characterImageButtonArray!![AvalonCharacterName.MINION2] = minion2Button
        characterImageButtonArray!![AvalonCharacterName.MINION3] = minion3Button

        characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.addOnClickObserver(object: IObserver { override fun update() { runMerlinSelectionRules() } })
        characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.addOnClickObserver(object: IObserver { override fun update() { runPercivalSelectionRules() } })
        characterImageButtonArray!![AvalonCharacterName.ASSASSIN]!!.addOnClickObserver(object: IObserver { override fun update() { runMerlinSelectionRules() } })
        characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.addOnClickObserver(object: IObserver { override fun update() { runMorganaSelectionRules() } })
        characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.addOnClickObserver(object: IObserver { override fun update() { runMordredSelectionRules() } })
        characterImageButtonArray!![AvalonCharacterName.OBERON]!!.addOnClickObserver(object: IObserver { override fun update() { runOberonSelectionRules() } })
    }

    fun destroy() {
        characterImageButtonArray?.let {
            for (idx: Int in it.indices) {
                it[idx]?.destroy()
                it[idx] = null
            }
        }

        characterImageButtonArray = null
    }

    fun getCharacter(idx: Int): CheckableObserverImageButton {
        if ( (idx < AvalonCharacterName.MERLIN) || (idx > AvalonCharacterName.MINION3) ) {
            throw RuntimeException("AvalonCharacterSelectionRules::getCharacter(): Invalid index $idx")
        }

        if (characterImageButtonArray == null) {
            throw RuntimeException("AvalonCharacterSelectionRules::getCharacter(): mCharacterImageButtonArray is NULL")
        }

        return characterImageButtonArray!![idx]!!
    }

    /**
     * If Merlin is selected, then Assassin is auto-selected, and vice versa.
     * In addition, one of the LOYAL is auto-deselected, and one of the MINIONS is auto-deselected.
     *
     * If Merlin is deselected, then Assassin is auto-deselected, and vice versa.
     * In addition, one of the LOYAL is auto-selected, and one of the MINIONS is auto-selected.
     * If Percival, Morgana, or Mordred are already selected, they should be replaced by MINIONS.
     */
    private fun runMerlinSelectionRules() {
        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::runMerlinSelectionRules(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        if (characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.isChecked) {
            if (characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) {
                characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.performClick()
            }

            if (characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.isChecked) {
                characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.performClick()
            }

            characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.isChecked = false
            characterImageButtonArray!![AvalonCharacterName.ASSASSIN]!!.isChecked = false
            checkNewLoyal()
            checkNewMinion()
        }
        else {
            characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.isChecked = true
            characterImageButtonArray!![AvalonCharacterName.ASSASSIN]!!.isChecked = true
            uncheckOldLoyal()
            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.OBERON, 1)
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runMerlinSelectionRules()")
    }

    /**
     * TRIANGLE amongst Percival, Morgana, and Mordred:
     * You can have Percival without Morgana but you cannot have Morgana without Percival.
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * You can have Percival without Mordred and you can also have Mordred without Percival.
     *
     * If Percival is selected:
     * - If Merlin is not already selected, then Merlin is auto-selected.
     * - In a 5-player game, Morgana is auto-selected.
     * - If, prior to this, Morgana was not already selected, then one of the Evil players is auto-deselected.
     * - One of the LOYAL is auto-deselected.
     *
     * If Percival is deselected:
     * - Morgana is auto-deselected.
     * - If, prior to this, Morgana was already selected, then one of the MINIONS is auto-selected.
     * - One of the LOYAL is auto-selected.
     */
    private fun runPercivalSelectionRules() {
        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::runPercivalSelectionRules(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        if (characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) {
            characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked = false
            checkNewLoyal()

            if (characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked) {
                checkNewMinion()
            }
            characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked = false
        }
        else {
            characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked = true
            uncheckOldLoyal()

            if (!characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.isChecked) {
                characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.performClick()
            }

            if ( (expectedGoodTotal + expectedEvilTotal == 5) &&
                 (!characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.isChecked) )
            {
                if (!characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked) {
                    searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORDRED, 1)
                }
                characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked = true
            }
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runPercivalSelectionRules()")
    }

    /**
     * TRIANGLE amongst Percival, Morgana, and Mordred:
     * You can have Percival without Morgana but you cannot have Morgana without Percival.
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * You can have Percival without Mordred and you can also have Mordred without Percival.
     *
     * If Morgana is selected:
     * - If Percival is not already selected, then Percival is auto-selected and one of the LOYAL is auto-deselected.
     * - One of the Evil players is auto-deselected.
     *
     * If Morgana is deselected:
     * - If Percival is not already selected, then throw error.
     * - In a 5-player game, if Percival is already selected, then Mordred is auto-selected.
     * - If, prior to this, Mordred was already selected, then one of the MINIONS is auto-selected.
     * - In a non 5-player game, one of the MINIONS is auto-selected.
     */
    private fun runMorganaSelectionRules() {
        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::runMorganaSelectionRules(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        if (characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked) {
            if (!characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) {
                throw RuntimeException(
                    "AvalonCharacterSelectionRules::runMorganaSelectionRules(): " +
                        "Morgana was active in the absence of Percival")
            }

            characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked = false

            if ( (expectedGoodTotal + expectedEvilTotal == 5) &&
                 (characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) )
            {
                if (characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.isChecked) {
                    checkNewMinion()
                }
                characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.isChecked = true
            }
            else {
                checkNewMinion()
            }
        }
        else {
            characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked = true
            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORDRED, 1)

            if (!characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) {
                characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.performClick()
            }
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runMorganaSelectionRules()")
    }

    /**
     * TRIANGLE amongst Percival, Morgana, and Mordred:
     * You can have Percival without Morgana but you cannot have Morgana without Percival.
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * You can have Percival without Mordred and you can also have Mordred without Percival.
     *
     * You can have Merlin without Mordred but you cannot have Mordred without Merlin.
     *
     * If Mordred is selected:
     * - If Merlin is not already selected, then Merlin is auto-selected and one of the LOYAL is auto-deselected.
     * - In a 5-player game, if Percival is not already selected, then Percival is auto-selected.
     * - One of the Evil players is auto-deselected.
     *
     * If Mordred is deselected:
     * - If Merlin is not already selected, then throw error.
     * - In a 5-player game, if Percival is already selected, then Morgana is auto-selected.
     * - If, prior to this, Morgana was already selected, then one of the MINIONS is auto-selected.
     * - In a non 5-player game, one of the MINIONS is auto-selected.
     */
    private fun runMordredSelectionRules() {
        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::runMordredSelectionRules(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        if (characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.isChecked) {
            if (!characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.isChecked) {
                throw RuntimeException(
                    "AvalonCharacterSelectionRules::runMordredSelectionRules(): " +
                        "Mordred was active in the absence of Merlin")
            }

            characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.isChecked = false

            if ( (expectedGoodTotal + expectedEvilTotal == 5) &&
                 (characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) )
            {
                if (characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked) {
                    checkNewMinion()
                }
                characterImageButtonArray!![AvalonCharacterName.MORGANA]!!.isChecked = true
            }
            else {
                checkNewMinion()
            }
        }
        else {
            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORGANA, 1)
            characterImageButtonArray!![AvalonCharacterName.MORDRED]!!.isChecked = true

            if (!characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.isChecked) {
                characterImageButtonArray!![AvalonCharacterName.MERLIN]!!.performClick()
            }

            if ( (expectedGoodTotal + expectedEvilTotal == 5) &&
                 (!characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) )
            {
                characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.performClick()
            }
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runMordredSelectionRules()")
    }

    /**
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * This means that, in 5-player mode, you can have either Percival or Oberon but never both at the same time.
     * This could happen accidentally if we select Percival in 5-player mode, and then select Oberon immediately,
     * which causes Morgana/Mordred to be deselected, but Percival is still selected.
     *
     * This could also happen accidentally if Percival and Oberon are both already selected in non-5 player mode,
     * and then the player number is changed externally.
     * For this, we rely on a separate function (called onPlayerNumberChange()).
     *
     * If Oberon is selected:
     * - In a 5-player game, if Percival is already selected, then Percival is auto-deselected.
     */
    private fun runOberonSelectionRules() {
        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::runOberonSelectionRules(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        if (characterImageButtonArray!![AvalonCharacterName.OBERON]!!.isChecked) {
            characterImageButtonArray!![AvalonCharacterName.OBERON]!!.isChecked = false
            searchAndCheckNewCharacters(AvalonCharacterName.MINION0, AvalonCharacterName.MINION3, 1)
        }
        else {
            if ( (expectedGoodTotal + expectedEvilTotal == 5) &&
                 (characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) )
            {
                characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.performClick()
            }

            searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MORGANA, 1)
            characterImageButtonArray!![AvalonCharacterName.OBERON]!!.isChecked = true
        }

        checkPlayerComposition("AvalonCharacterSelectionRules::runOberonSelectionRules()")
    }

    /**
     * For games of 5, add either Morgana or Mordred when playing with Percival.
     * This means that, in 5-player mode, you can have either Percival or Oberon but never both at the same time.
     * This could happen accidentally if Percival and Oberon are both already selected in non-5 player mode,
     * and then the player number is changed externally.
     *
     * If Oberon is selected:
     * - In a 5-player game, if Percival is already selected, then Percival is auto-deselected.
     */
    fun onPlayerNumberChange() {
        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::onPlayerNumberChange(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        if ( (expectedGoodTotal + expectedEvilTotal == 5) &&
             (characterImageButtonArray!![AvalonCharacterName.PERCIVAL]!!.isChecked) &&
             (characterImageButtonArray!![AvalonCharacterName.OBERON]!!.isChecked) )
        {
            characterImageButtonArray!![AvalonCharacterName.OBERON]!!.performClick()
        }
    }

    /**
     * Find the first LOYAL who is VISIBLE and not checked. Then, check him/her.
     */
    private fun checkNewLoyal() {
        searchAndCheckNewCharacters(AvalonCharacterName.LOYAL0, AvalonCharacterName.LOYAL5, 1)
    }

    /**
     * Find the first MINION who is VISIBLE and not checked. Then, check him/her.
     */
    private fun checkNewMinion() {
        searchAndCheckNewCharacters(AvalonCharacterName.MINION0, AvalonCharacterName.MINION3, 1)
    }

    /**
     * Find the first X characters between startIdx and endIdx who are VISIBLE and not checked. Then, check them.
     * Pre-condition: startIdx <= endIdx
     */
    fun searchAndCheckNewCharacters(startIdx: Int, endIdx: Int, X: Int) {
        if (startIdx > endIdx) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::searchAndCheckNewCharacters(): " +
                    "startIdx must be <= endIdx")
        }

        if (X < 0) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::searchAndCheckNewCharacters(): " +
                    "X must be >= 0")
        }

        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::searchAndCheckNewCharacters(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        var currIdx: Int = startIdx
        var charactersToCheck: Int = X
        while ( (currIdx <= endIdx) &&
                (characterImageButtonArray!![currIdx]!!.visibility == View.VISIBLE) &&
                (charactersToCheck > 0) )
        {
            if (!characterImageButtonArray!![currIdx]!!.isChecked) {
                characterImageButtonArray!![currIdx]!!.isChecked = true
                --charactersToCheck
            }

            ++currIdx
        }
    }

    /**
     * Find the last LOYAL who is VISIBLE and checked. Then, uncheck him/her.
     */
    private fun uncheckOldLoyal() {
        searchAndUncheckOldCharacters(AvalonCharacterName.LOYAL5, AvalonCharacterName.LOYAL0, 1)
    }

    /**
     * Find the last MINION who is VISIBLE and checked. Then, uncheck him/her.
     */
    private fun uncheckOldMinion() {
        searchAndUncheckOldCharacters(AvalonCharacterName.MINION3, AvalonCharacterName.MINION0, 1)
    }

    /**
     * Find the last X characters between startIdx and endIdx who are VISIBLE and checked. Then, uncheck them.
     * Pre-condition: startIdx >= endIdx
     */
    fun searchAndUncheckOldCharacters(startIdx: Int, endIdx: Int, X: Int) {
        if (startIdx < endIdx) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::searchAndUncheckOldCharacters(): " +
                    "startIdx must be >= endIdx")
        }

        if (X < 0) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::searchAndUncheckOldCharacters(): " +
                    "X must be >= 0")
        }

        if (characterImageButtonArray == null) {
            throw RuntimeException(
                "AvalonCharacterSelectionRules::searchAndUncheckOldCharacters(): " +
                    "mCharacterImageButtonArray is NULL")
        }

        var currIdx: Int = startIdx
        var charactersToUncheck: Int = X
        while ((currIdx >= endIdx) && (charactersToUncheck > 0)) {
            if (characterImageButtonArray!![currIdx]!!.visibility == View.INVISIBLE) {
                --currIdx
                continue
            }

            if (characterImageButtonArray!![currIdx]!!.isChecked) {
                characterImageButtonArray!![currIdx]!!.isChecked = false
                --charactersToUncheck
            }

            --currIdx
        }
    }

    val actualGoodTotal: Int
        get() {
            if (characterImageButtonArray == null) {
                throw RuntimeException(
                    "AvalonCharacterSelectionRules::getActualGoodTotal(): " +
                        "mCharacterImageButtonArray is NULL")
            }

            var good = 0
            for (idx: Int in AvalonCharacterName.MERLIN .. AvalonCharacterName.LOYAL5) {
                if (characterImageButtonArray!![idx]!!.isChecked) {
                    ++good
                }
            }
            return good
        }

    val actualEvilTotal: Int
        get() {
            if (characterImageButtonArray == null) {
                throw RuntimeException(
                    "AvalonCharacterSelectionRules::getActualEvilTotal(): " +
                        "mCharacterImageButtonArray is NULL")
            }

            var evil = 0
            for (idx: Int in AvalonCharacterName.ASSASSIN .. AvalonCharacterName.MINION3) {
                if (characterImageButtonArray!![idx]!!.isChecked) {
                    ++evil
                }
            }

            return evil
        }

    fun checkPlayerComposition(callingFuncName: String?) {
        if (callingFuncName == null) {
            return
        }

        val good: Int = actualGoodTotal
        if (good != expectedGoodTotal) {
            throw RuntimeException(
                "$callingFuncName: expected good player total is $expectedGoodTotal" +
                    " but actual good player total is $good")
        }

        val evil: Int = actualEvilTotal
        if (evil != expectedEvilTotal) {
            throw RuntimeException(
                "$callingFuncName: expected evil player total is $expectedEvilTotal" +
                    " but actual evil player total is $evil")
        }
    }
}