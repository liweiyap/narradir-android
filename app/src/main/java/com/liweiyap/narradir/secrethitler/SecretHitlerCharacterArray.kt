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
    var mCharacterImageButtonArray: Array<ObserverImageButton?>? = arrayOfNulls(SecretHitlerCharacterName.getNumberOfCharacters())
        private set

    var mExpectedGoodTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfGoodCharacters()
    var mExpectedEvilTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfEvilCharacters()

    init {
        mCharacterImageButtonArray!![SecretHitlerCharacterName.LIBERAL0] = liberal0Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.LIBERAL1] = liberal1Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.LIBERAL2] = liberal2Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.LIBERAL3] = liberal3Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.LIBERAL4] = liberal4Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.LIBERAL5] = liberal5Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.HITLER] = hitlerButton
        mCharacterImageButtonArray!![SecretHitlerCharacterName.FASCIST0] = fascist0Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.FASCIST1] = fascist1Button
        mCharacterImageButtonArray!![SecretHitlerCharacterName.FASCIST2] = fascist2Button
    }

    fun destroy() {
        mCharacterImageButtonArray?.let {
            for (idx: Int in it.indices) {
                it[idx]?.destroy()
                it[idx] = null
            }
        }

        mCharacterImageButtonArray = null
    }

    fun getCharacter(idx: Int): ObserverImageButton {
        if ( (idx < SecretHitlerCharacterName.LIBERAL0) || (idx > SecretHitlerCharacterName.FASCIST2) ) {
            throw RuntimeException("SecretHitlerCharacterArray::getCharacter(): Invalid index $idx")
        }

        if (mCharacterImageButtonArray == null) {
            throw RuntimeException("SecretHitlerCharacterArray::getCharacter(): mCharacterImageButtonArray is NULL")
        }

        return mCharacterImageButtonArray!![idx]!!
    }

    val actualGoodTotal: Int
        get() {
            var good = 0
            for (idx: Int in SecretHitlerCharacterName.LIBERAL0 .. SecretHitlerCharacterName.LIBERAL5) {
                if (getCharacter(idx).visibility == View.VISIBLE) {
                    ++good
                }
            }

            return good
        }

    val actualEvilTotal: Int
        get() {
            var evil = 0
            for (idx: Int in SecretHitlerCharacterName.HITLER .. SecretHitlerCharacterName.FASCIST2) {
                if (getCharacter(idx).visibility == View.VISIBLE) {
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
        if (good != mExpectedGoodTotal) {
            throw RuntimeException(
                "$callingFuncName: expected good player total is $mExpectedGoodTotal" +
                    " but actual good player total is $good")
        }

        val evil: Int = actualEvilTotal
        if (evil != mExpectedEvilTotal) {
            throw RuntimeException(
                "$callingFuncName: expected evil player total is $mExpectedEvilTotal" +
                    " but actual evil player total is $evil")
        }
    }
}