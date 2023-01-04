package com.liweiyap.narradir.util

import android.content.Context
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.snackbar.BaseTransientBottomBar

import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.SnackbarWrapper
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton
import com.liweiyap.narradir.util.audio.CharacterDescriptionMediaPlayer

import java.util.EnumSet

abstract class CharacterSelectionControlGroup(protected var mActivityContext: Context?) {
    protected var mPlayerNumberButtonArray: Array<CustomTypefaceableCheckableObserverButton?>? = arrayOfNulls(PlayerNumbers.getNumberOfPlayerNumbers())
    protected var mCharacterDescriptionMediaPlayer: CharacterDescriptionMediaPlayer?
    protected var mSnackbar: SnackbarWrapper?

    init {
        mSnackbar = SnackbarWrapper(mActivityContext!!)
        mCharacterDescriptionMediaPlayer = CharacterDescriptionMediaPlayer(mActivityContext!!)
    }

    open fun destroy() {
        mActivityContext = null

        mSnackbar?.destroy()
        mSnackbar = null

        mCharacterDescriptionMediaPlayer?.destroy()
        mCharacterDescriptionMediaPlayer = null

        mPlayerNumberButtonArray?.let {
            for (idx: Int in it.indices) {
                it[idx]?.destroy()
                it[idx] = null
            }
        }
        mPlayerNumberButtonArray = null
    }

    val playerNumberButtonArray: Array<CustomTypefaceableCheckableObserverButton?>?
        get() {
            return mPlayerNumberButtonArray
        }

    fun selectPlayerNumberButton(playerNumber: Int) {
        if (mPlayerNumberButtonArray == null) {
            throw RuntimeException("CharacterSelectionControlGroup::selectPlayerNumberButton(): mPlayerNumberButtonArray is NULL")
        }

        val selectorButton: CustomTypefaceableCheckableObserverButton? = when (playerNumber) {
            5 -> mPlayerNumberButtonArray!![PlayerNumbers.P5]
            6 -> mPlayerNumberButtonArray!![PlayerNumbers.P6]
            7 -> mPlayerNumberButtonArray!![PlayerNumbers.P7]
            8 -> mPlayerNumberButtonArray!![PlayerNumbers.P8]
            9 -> mPlayerNumberButtonArray!![PlayerNumbers.P9]
            10 -> mPlayerNumberButtonArray!![PlayerNumbers.P10]
            else -> throw RuntimeException("CharacterSelectionControlGroup::selectPlayerNumberButton(): Invalid no of players: $playerNumber")
        }

        selectorButton?.performClick()
    }

    protected fun startCharacterDescriptionMediaPlayer(description: String) {
        if ( (mActivityContext !is AppCompatActivity) || (mCharacterDescriptionMediaPlayer == null) ) {
            return
        }

        mCharacterDescriptionMediaPlayer!!.play(res = description, volume = 1F) {
            (mActivityContext as AppCompatActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        (mActivityContext as AppCompatActivity).window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun resumeCharacterDescriptionMediaPlayer() {
        mCharacterDescriptionMediaPlayer?.resume()
    }

    fun pauseCharacterDescriptionMediaPlayer() {
        mCharacterDescriptionMediaPlayer?.pause()
    }

    fun stopCharacterDescriptionMediaPlayer() {
        if ( (mActivityContext !is AppCompatActivity) || (mCharacterDescriptionMediaPlayer == null) ) {
            return
        }

        mCharacterDescriptionMediaPlayer!!.stop()
        (mActivityContext as AppCompatActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun freeCharacterDescriptionMediaPlayer() {
        mCharacterDescriptionMediaPlayer?.free()
    }

    protected fun showSnackbar(message: String) {
        if (mActivityContext !is AppCompatActivity) {
            return
        }

        try {
            mSnackbar?.show(
                view = (mActivityContext as AppCompatActivity).findViewById(R.id.characterSelectionLayoutNavBar),
                message = message,
                duration = BaseTransientBottomBar.LENGTH_SHORT,
                actionMessage = mActivityContext?.getString(R.string.positive_button_text),
                actionCallback = null,
                flags = EnumSet.of(SnackbarBuilderFlag.SHOW_ABOVE_XY, SnackbarBuilderFlag.ACTION_DISMISSABLE))
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}