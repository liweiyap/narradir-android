package com.liweiyap.narradir.util

import android.content.Context
import android.content.SharedPreferences

import com.liweiyap.narradir.R
import com.liweiyap.narradir.avalon.AvalonCharacterName
import com.liweiyap.narradir.secrethitler.SecretHitlerCharacterName

class NarradirViewModel(context: Context, sharedPref: SharedPreferences) {
    var pauseDurationInMilliSecs: Long = 5000
    var backgroundSoundVolume: Float = 1f
    var narrationVolume: Float = 1f
    var backgroundSoundName: String
    private var mDoHideBackgroundSoundHint: Boolean = false

    var avalonExpectedGoodTotal: Int = AvalonCharacterName.getDefaultNumberOfGoodCharacters()
    var avalonExpectedEvilTotal: Int = AvalonCharacterName.getDefaultNumberOfEvilCharacters()
    var isMerlinChecked: Boolean = true
    var isPercivalChecked: Boolean = false
    var isMorganaChecked: Boolean = false
    var isMordredChecked: Boolean = false
    var isOberonChecked: Boolean = false

    var secretHitlerExpectedGoodTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfGoodCharacters()
    var secretHitlerExpectedEvilTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfEvilCharacters()

    private var mContext: Context?
    private var mSharedPref: SharedPreferences?

    init {
        mContext = context
        mSharedPref = sharedPref
        pauseDurationInMilliSecs = sharedPref.getLong(context.getString(R.string.pause_duration_key), pauseDurationInMilliSecs)
        backgroundSoundVolume = sharedPref.getFloat(context.getString(R.string.background_volume_key), backgroundSoundVolume)
        narrationVolume = sharedPref.getFloat(context.getString(R.string.narration_volume_key), narrationVolume)
        backgroundSoundName = sharedPref.getString(context.getString(R.string.background_sound_name_key), context.getString(R.string.backgroundsound_none))!!
        mDoHideBackgroundSoundHint = sharedPref.getBoolean(context.getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint)
        avalonExpectedGoodTotal = sharedPref.getInt(context.getString(R.string.good_player_number_avalon_key), avalonExpectedGoodTotal)
        avalonExpectedEvilTotal = sharedPref.getInt(context.getString(R.string.evil_player_number_avalon_key), avalonExpectedEvilTotal)
        isMerlinChecked = sharedPref.getBoolean(context.getString(R.string.is_merlin_checked_key), isMerlinChecked)
        isPercivalChecked = sharedPref.getBoolean(context.getString(R.string.is_percival_checked_key), isPercivalChecked)
        isMorganaChecked = sharedPref.getBoolean(context.getString(R.string.is_morgana_checked_key), isMorganaChecked)
        isMordredChecked = sharedPref.getBoolean(context.getString(R.string.is_mordred_checked_key), isMordredChecked)
        isOberonChecked = sharedPref.getBoolean(context.getString(R.string.is_oberon_checked_key), isOberonChecked)
        secretHitlerExpectedGoodTotal = sharedPref.getInt(context.getString(R.string.good_player_number_secrethitler_key), secretHitlerExpectedGoodTotal)
        secretHitlerExpectedEvilTotal = sharedPref.getInt(context.getString(R.string.evil_player_number_secrethitler_key), secretHitlerExpectedEvilTotal)
    }

    @Synchronized
    fun destroy() {
        mContext = null
        mSharedPref = null
    }

    fun savePreferences() {
        if (mContext == null || mSharedPref == null) {
            return
        }

        val sharedPrefEditor: SharedPreferences.Editor = mSharedPref!!.edit()
        sharedPrefEditor.remove(mContext!!.getString(R.string.background_sound_key))  // this line may be removed in future
        sharedPrefEditor.putLong(mContext!!.getString(R.string.pause_duration_key), pauseDurationInMilliSecs)
        sharedPrefEditor.putString(mContext!!.getString(R.string.background_sound_name_key), backgroundSoundName)
        sharedPrefEditor.putFloat(mContext!!.getString(R.string.background_volume_key), backgroundSoundVolume)
        sharedPrefEditor.putFloat(mContext!!.getString(R.string.narration_volume_key), narrationVolume)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.good_player_number_avalon_key), avalonExpectedGoodTotal)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.evil_player_number_avalon_key), avalonExpectedEvilTotal)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_merlin_checked_key), isMerlinChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_percival_checked_key), isPercivalChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_morgana_checked_key), isMorganaChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_mordred_checked_key), isMordredChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_oberon_checked_key), isOberonChecked)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.good_player_number_secrethitler_key), secretHitlerExpectedGoodTotal)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.evil_player_number_secrethitler_key), secretHitlerExpectedEvilTotal)
        sharedPrefEditor.apply()
    }

    val isAvalonLastSelected: Boolean
        get() = (loadLastSelectedGame() == Constants.GAME_AVALON)

    val isSecretHitlerLastSelected: Boolean
        get() = (loadLastSelectedGame() == Constants.GAME_SECRETHITLER)

    fun loadLastSelectedGame(): Int {
        return if ((mContext == null) || (mSharedPref == null))
            Constants.GAME_AVALON
        else
            mSharedPref!!.getInt(mContext!!.getString(R.string.last_selected_game_key), Constants.GAME_AVALON)
    }

    fun saveAvalonLastSelected() {
        saveLastSelectedGame(gameId = Constants.GAME_AVALON)
    }

    fun saveSecretHitlerLastSelected() {
        saveLastSelectedGame(gameId = Constants.GAME_SECRETHITLER)
    }

    fun saveLastSelectedGame(gameId: Int) {
        if (mContext == null || mSharedPref == null) {
            return
        }

        val sharedPrefEditor: SharedPreferences.Editor = mSharedPref!!.edit()
        sharedPrefEditor.putInt(mContext!!.getString(R.string.last_selected_game_key), gameId)
        sharedPrefEditor.apply()
    }

    fun doHideBackgroundSoundHint(): Boolean {
        return mDoHideBackgroundSoundHint
    }

    fun setHideBackgroundSoundHint(doHide: Boolean) {
        mDoHideBackgroundSoundHint = doHide
    }
}