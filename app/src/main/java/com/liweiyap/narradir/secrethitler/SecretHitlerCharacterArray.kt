package com.liweiyap.narradir.secrethitler

import android.view.View

import com.liweiyap.narradir.ui.ObserverImageButton

class SecretHitlerCharacterArray(
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
) {
    var characterImageButtonArray: Array<ObserverImageButton?>? = arrayOfNulls(SecretHitlerCharacterName.getNumberOfCharacters())
        private set

    var expectedGoodTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfGoodCharacters()
    var expectedEvilTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfEvilCharacters()

    init {
        characterImageButtonArray!![SecretHitlerCharacterName.LIBERAL0] = liberal0Button
        characterImageButtonArray!![SecretHitlerCharacterName.LIBERAL1] = liberal1Button
        characterImageButtonArray!![SecretHitlerCharacterName.LIBERAL2] = liberal2Button
        characterImageButtonArray!![SecretHitlerCharacterName.LIBERAL3] = liberal3Button
        characterImageButtonArray!![SecretHitlerCharacterName.LIBERAL4] = liberal4Button
        characterImageButtonArray!![SecretHitlerCharacterName.LIBERAL5] = liberal5Button
        characterImageButtonArray!![SecretHitlerCharacterName.HITLER] = hitlerButton
        characterImageButtonArray!![SecretHitlerCharacterName.FASCIST0] = fascist0Button
        characterImageButtonArray!![SecretHitlerCharacterName.FASCIST1] = fascist1Button
        characterImageButtonArray!![SecretHitlerCharacterName.FASCIST2] = fascist2Button
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

    fun getCharacter(idx: Int): ObserverImageButton? {
        if ( (idx < SecretHitlerCharacterName.LIBERAL0) || (idx > SecretHitlerCharacterName.FASCIST2) ) {
            throw RuntimeException("SecretHitlerCharacterArray::getCharacter(): Invalid index $idx")
        }

        if (characterImageButtonArray == null) {
            throw RuntimeException("SecretHitlerCharacterArray::getCharacter(): mCharacterImageButtonArray is NULL")
        }

        return characterImageButtonArray!![idx]
    }

    val actualGoodTotal: Int
        get() {
            if (characterImageButtonArray == null) {
                throw RuntimeException(
                    "SecretHitlerCharacterArray::getActualGoodTotal(): " +
                        "mCharacterImageButtonArray is NULL"
                )
            }

            var good = 0
            for (idx: Int in SecretHitlerCharacterName.LIBERAL0 .. SecretHitlerCharacterName.LIBERAL5) {
                characterImageButtonArray!![idx]?.let {
                    if (it.visibility == View.VISIBLE) {
                        ++good
                    }
                }
            }

            return good
        }

    val actualEvilTotal: Int
        get() {
            if (characterImageButtonArray == null) {
                throw RuntimeException(
                    "SecretHitlerCharacterArray::getActualEvilTotal(): " +
                        "mCharacterImageButtonArray is NULL"
                )
            }

            var evil = 0
            for (idx: Int in SecretHitlerCharacterName.HITLER .. SecretHitlerCharacterName.FASCIST2) {
                characterImageButtonArray!![idx]?.let {
                    if (it.visibility == View.VISIBLE) {
                        ++evil
                    }
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
                callingFuncName +
                    ": expected good player total is " + expectedGoodTotal +
                    " but actual good player total is " + good
            )
        }

        val evil: Int = actualEvilTotal
        if (evil != expectedEvilTotal) {
            throw RuntimeException(
                callingFuncName +
                    ": expected evil player total is " + expectedEvilTotal +
                    " but actual evil player total is " + evil
            )
        }
    }
}