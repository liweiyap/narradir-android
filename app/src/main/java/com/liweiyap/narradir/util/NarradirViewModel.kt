package com.liweiyap.narradir.util

import android.content.Context
import android.content.SharedPreferences

import com.liweiyap.narradir.R
import com.liweiyap.narradir.avalon.AvalonCharacterName
import com.liweiyap.narradir.secrethitler.SecretHitlerCharacterName

class NarradirViewModel(context: Context, sharedPref: SharedPreferences) {
    var mPauseDurationInMilliSecs: Long = 5000L
    var mBackgroundSoundVolume: Float = 1F
    var mNarrationVolume: Float = 1F
    var mBackgroundSoundName: String
    private var mDoHideBackgroundSoundHint: Boolean = false

    var mAvalonExpectedGoodTotal: Int = AvalonCharacterName.getDefaultNumberOfGoodCharacters()
    var mAvalonExpectedEvilTotal: Int = AvalonCharacterName.getDefaultNumberOfEvilCharacters()
    var mIsMerlinChecked: Boolean = true
    var mIsPercivalChecked: Boolean = false
    var mIsMorganaChecked: Boolean = false
    var mIsMordredChecked: Boolean = false
    var mIsOberonChecked: Boolean = false

    var mSecretHitlerExpectedGoodTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfGoodCharacters()
    var mSecretHitlerExpectedEvilTotal: Int = SecretHitlerCharacterName.getDefaultNumberOfEvilCharacters()

    private var mContext: Context?
    private var mSharedPref: SharedPreferences?

    init {
        mContext = context
        mSharedPref = sharedPref
        mPauseDurationInMilliSecs = sharedPref.getLong(context.getString(R.string.pause_duration_key), mPauseDurationInMilliSecs)
        mBackgroundSoundVolume = sharedPref.getFloat(context.getString(R.string.background_volume_key), mBackgroundSoundVolume)
        mNarrationVolume = sharedPref.getFloat(context.getString(R.string.narration_volume_key), mNarrationVolume)
        mBackgroundSoundName = sharedPref.getString(context.getString(R.string.background_sound_name_key), context.getString(R.string.backgroundsound_none))!!
        mDoHideBackgroundSoundHint = sharedPref.getBoolean(context.getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint)
        mAvalonExpectedGoodTotal = sharedPref.getInt(context.getString(R.string.good_player_number_avalon_key), mAvalonExpectedGoodTotal)
        mAvalonExpectedEvilTotal = sharedPref.getInt(context.getString(R.string.evil_player_number_avalon_key), mAvalonExpectedEvilTotal)
        mIsMerlinChecked = sharedPref.getBoolean(context.getString(R.string.is_merlin_checked_key), mIsMerlinChecked)
        mIsPercivalChecked = sharedPref.getBoolean(context.getString(R.string.is_percival_checked_key), mIsPercivalChecked)
        mIsMorganaChecked = sharedPref.getBoolean(context.getString(R.string.is_morgana_checked_key), mIsMorganaChecked)
        mIsMordredChecked = sharedPref.getBoolean(context.getString(R.string.is_mordred_checked_key), mIsMordredChecked)
        mIsOberonChecked = sharedPref.getBoolean(context.getString(R.string.is_oberon_checked_key), mIsOberonChecked)
        mSecretHitlerExpectedGoodTotal = sharedPref.getInt(context.getString(R.string.good_player_number_secrethitler_key), mSecretHitlerExpectedGoodTotal)
        mSecretHitlerExpectedEvilTotal = sharedPref.getInt(context.getString(R.string.evil_player_number_secrethitler_key), mSecretHitlerExpectedEvilTotal)
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
        sharedPrefEditor.putLong(mContext!!.getString(R.string.pause_duration_key), mPauseDurationInMilliSecs)
        sharedPrefEditor.putString(mContext!!.getString(R.string.background_sound_name_key), mBackgroundSoundName)
        sharedPrefEditor.putFloat(mContext!!.getString(R.string.background_volume_key), mBackgroundSoundVolume)
        sharedPrefEditor.putFloat(mContext!!.getString(R.string.narration_volume_key), mNarrationVolume)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.good_player_number_avalon_key), mAvalonExpectedGoodTotal)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.evil_player_number_avalon_key), mAvalonExpectedEvilTotal)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_merlin_checked_key), mIsMerlinChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_percival_checked_key), mIsPercivalChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_morgana_checked_key), mIsMorganaChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_mordred_checked_key), mIsMordredChecked)
        sharedPrefEditor.putBoolean(mContext!!.getString(R.string.is_oberon_checked_key), mIsOberonChecked)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.good_player_number_secrethitler_key), mSecretHitlerExpectedGoodTotal)
        sharedPrefEditor.putInt(mContext!!.getString(R.string.evil_player_number_secrethitler_key), mSecretHitlerExpectedEvilTotal)
        sharedPrefEditor.apply()
    }

    val isAvalonLastSelected: Boolean
        get() = (loadLastSelectedGame() == GAMEID_AVALON)

    val isSecretHitlerLastSelected: Boolean
        get() = (loadLastSelectedGame() == GAMEID_SECRETHITLER)

    fun loadLastSelectedGame(): Int {
        return if ((mContext == null) || (mSharedPref == null))
            GAMEID_AVALON
        else
            mSharedPref!!.getInt(mContext!!.getString(R.string.last_selected_game_key), GAMEID_AVALON)
    }

    fun saveAvalonLastSelected() {
        saveLastSelectedGame(gameId = GAMEID_AVALON)
    }

    fun saveSecretHitlerLastSelected() {
        saveLastSelectedGame(gameId = GAMEID_SECRETHITLER)
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

    companion object {
        const val GAMEID_AVALON: Int = 0
        const val GAMEID_SECRETHITLER: Int = 1
    }
}