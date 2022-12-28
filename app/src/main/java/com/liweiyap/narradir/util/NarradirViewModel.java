package com.liweiyap.narradir.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.liweiyap.narradir.R;
import com.liweiyap.narradir.avalon.AvalonCharacterName;
import com.liweiyap.narradir.secrethitler.SecretHitlerCharacterName;

public class NarradirViewModel
{
    public NarradirViewModel(@NonNull final Context context, @NonNull final SharedPreferences sharedPref)
    {
        mContext = context;
        mSharedPref = sharedPref;
        mPauseDurationInMilliSecs = sharedPref.getLong(context.getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mBackgroundSoundVolume = sharedPref.getFloat(context.getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mNarrationVolume = sharedPref.getFloat(context.getString(R.string.narration_volume_key), mNarrationVolume);
        mBackgroundSoundName = sharedPref.getString(context.getString(R.string.background_sound_name_key), context.getString(R.string.backgroundsound_none));
        mDoHideBackgroundSoundHint = sharedPref.getBoolean(context.getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint);
        mAvalonExpectedGoodTotal = sharedPref.getInt(context.getString(R.string.good_player_number_avalon_key), mAvalonExpectedGoodTotal);
        mAvalonExpectedEvilTotal = sharedPref.getInt(context.getString(R.string.evil_player_number_avalon_key), mAvalonExpectedEvilTotal);
        mIsMerlinChecked = sharedPref.getBoolean(context.getString(R.string.is_merlin_checked_key), mIsMerlinChecked);
        mIsPercivalChecked = sharedPref.getBoolean(context.getString(R.string.is_percival_checked_key), mIsPercivalChecked);
        mIsMorganaChecked = sharedPref.getBoolean(context.getString(R.string.is_morgana_checked_key), mIsMorganaChecked);
        mIsMordredChecked = sharedPref.getBoolean(context.getString(R.string.is_mordred_checked_key), mIsMordredChecked);
        mIsOberonChecked = sharedPref.getBoolean(context.getString(R.string.is_oberon_checked_key), mIsOberonChecked);
        mSecretHitlerExpectedGoodTotal = sharedPref.getInt(context.getString(R.string.good_player_number_secrethitler_key), mSecretHitlerExpectedGoodTotal);
        mSecretHitlerExpectedEvilTotal = sharedPref.getInt(context.getString(R.string.evil_player_number_secrethitler_key), mSecretHitlerExpectedEvilTotal);
    }

    public void savePreferences()
    {
        if ( (mContext == null) || (mSharedPref == null) )
        {
            return;
        }

        SharedPreferences.Editor sharedPrefEditor = mSharedPref.edit();
        sharedPrefEditor.remove(mContext.getString(R.string.background_sound_key));  // this line may be removed in future
        sharedPrefEditor.putLong(mContext.getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        sharedPrefEditor.putString(mContext.getString(R.string.background_sound_name_key), mBackgroundSoundName);
        sharedPrefEditor.putFloat(mContext.getString(R.string.background_volume_key), mBackgroundSoundVolume);
        sharedPrefEditor.putFloat(mContext.getString(R.string.narration_volume_key), mNarrationVolume);
        sharedPrefEditor.putBoolean(mContext.getString(R.string.do_hide_background_sound_hint_key), mDoHideBackgroundSoundHint);
        sharedPrefEditor.putInt(mContext.getString(R.string.good_player_number_avalon_key), mAvalonExpectedGoodTotal);
        sharedPrefEditor.putInt(mContext.getString(R.string.evil_player_number_avalon_key), mAvalonExpectedEvilTotal);
        sharedPrefEditor.putBoolean(mContext.getString(R.string.is_merlin_checked_key), mIsMerlinChecked);
        sharedPrefEditor.putBoolean(mContext.getString(R.string.is_percival_checked_key), mIsPercivalChecked);
        sharedPrefEditor.putBoolean(mContext.getString(R.string.is_morgana_checked_key), mIsMorganaChecked);
        sharedPrefEditor.putBoolean(mContext.getString(R.string.is_mordred_checked_key), mIsMordredChecked);
        sharedPrefEditor.putBoolean(mContext.getString(R.string.is_oberon_checked_key), mIsOberonChecked);
        sharedPrefEditor.putInt(mContext.getString(R.string.good_player_number_secrethitler_key), mSecretHitlerExpectedGoodTotal);
        sharedPrefEditor.putInt(mContext.getString(R.string.evil_player_number_secrethitler_key), mSecretHitlerExpectedEvilTotal);
        sharedPrefEditor.apply();
    }

    public boolean isAvalonLastSelected()
    {
        return (loadLastSelectedGame() == Constants.GAME_AVALON);
    }

    public boolean isSecretHitlerLastSelected()
    {
        return (loadLastSelectedGame() == Constants.GAME_SECRETHITLER);
    }

    public int loadLastSelectedGame()
    {
        if ( (mContext == null) || (mSharedPref == null) )
        {
            return Constants.GAME_AVALON;
        }

        return mSharedPref.getInt(mContext.getString(R.string.last_selected_game_key), Constants.GAME_AVALON);
    }

    public void saveAvalonLastSelected()
    {
        saveLastSelectedGame(Constants.GAME_AVALON);
    }

    public void saveSecretHitlerLastSelected()
    {
        saveLastSelectedGame(Constants.GAME_SECRETHITLER);
    }

    public void saveLastSelectedGame(final int gameId)
    {
        if ( (mContext == null) || (mSharedPref == null) )
        {
            return;
        }

        SharedPreferences.Editor sharedPrefEditor = mSharedPref.edit();
        sharedPrefEditor.putInt(mContext.getString(R.string.last_selected_game_key), gameId);
        sharedPrefEditor.apply();
    }

    public long getPauseDurationInMilliSecs()
    {
        return mPauseDurationInMilliSecs;
    }

    public float getBackgroundSoundVolume()
    {
        return mBackgroundSoundVolume;
    }

    public float getNarrationVolume()
    {
        return mNarrationVolume;
    }

    @NonNull
    public String getBackgroundSoundName()
    {
        return mBackgroundSoundName;
    }

    public boolean doHideBackgroundSoundHint()
    {
        return mDoHideBackgroundSoundHint;
    }

    public void setPauseDurationInMilliSecs(final long msec)
    {
        mPauseDurationInMilliSecs = msec;
    }

    public void setBackgroundSoundVolume(final float volume)
    {
        mBackgroundSoundVolume = volume;
    }

    public void setNarrationVolume(final float volume)
    {
        mNarrationVolume = volume;
    }

    public void setBackgroundSoundName(@NonNull final String sound)
    {
        mBackgroundSoundName = sound;
    }

    public void setHideBackgroundSoundHint(final boolean doHide)
    {
        mDoHideBackgroundSoundHint = doHide;
    }

    public int getAvalonExpectedGoodTotal()
    {
        return mAvalonExpectedGoodTotal;
    }

    public int getAvalonExpectedEvilTotal()
    {
        return mAvalonExpectedEvilTotal;
    }

    public boolean isMerlinChecked()
    {
        return mIsMerlinChecked;
    }

    public boolean isPercivalChecked()
    {
        return mIsPercivalChecked;
    }

    public boolean isMorganaChecked()
    {
        return mIsMorganaChecked;
    }

    public boolean isMordredChecked()
    {
        return mIsMordredChecked;
    }

    public boolean isOberonChecked()
    {
        return mIsOberonChecked;
    }

    public void setAvalonExpectedGoodTotal(final int expectedGoodTotal)
    {
        mAvalonExpectedGoodTotal = expectedGoodTotal;
    }

    public void setAvalonExpectedEvilTotal(final int expectedEvilTotal)
    {
        mAvalonExpectedEvilTotal = expectedEvilTotal;
    }

    public void setMerlinChecked(final boolean isMerlinChecked)
    {
        mIsMerlinChecked = isMerlinChecked;
    }

    public void setPercivalChecked(final boolean isPercivalChecked)
    {
        mIsPercivalChecked = isPercivalChecked;
    }

    public void setMorganaChecked(final boolean isMorganaChecked)
    {
        mIsMorganaChecked = isMorganaChecked;
    }

    public void setMordredChecked(final boolean isMordredChecked)
    {
        mIsMordredChecked = isMordredChecked;
    }

    public void setOberonChecked(final boolean isOberonChecked)
    {
        mIsOberonChecked = isOberonChecked;
    }

    public int getSecretHitlerExpectedGoodTotal()
    {
        return mSecretHitlerExpectedGoodTotal;
    }

    public int getSecretHitlerExpectedEvilTotal()
    {
        return mSecretHitlerExpectedEvilTotal;
    }

    public void setSecretHitlerExpectedGoodTotal(final int expectedGoodTotal)
    {
        mSecretHitlerExpectedGoodTotal = expectedGoodTotal;
    }

    public void setSecretHitlerExpectedEvilTotal(final int expectedEvilTotal)
    {
        mSecretHitlerExpectedEvilTotal = expectedEvilTotal;
    }

    private long mPauseDurationInMilliSecs = 5000;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;
    @NonNull private String mBackgroundSoundName;
    private boolean mDoHideBackgroundSoundHint = false;

    private int mAvalonExpectedGoodTotal = AvalonCharacterName.getDefaultNumberOfGoodCharacters();
    private int mAvalonExpectedEvilTotal = AvalonCharacterName.getDefaultNumberOfEvilCharacters();
    private boolean mIsMerlinChecked = true;
    private boolean mIsPercivalChecked = false;
    private boolean mIsMorganaChecked = false;
    private boolean mIsMordredChecked = false;
    private boolean mIsOberonChecked = false;

    private int mSecretHitlerExpectedGoodTotal = SecretHitlerCharacterName.getDefaultNumberOfGoodCharacters();
    private int mSecretHitlerExpectedEvilTotal = SecretHitlerCharacterName.getDefaultNumberOfEvilCharacters();

    private final Context mContext;
    private final SharedPreferences mSharedPref;
}