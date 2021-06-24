package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import com.google.android.exoplayer2.Player;
import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.ui.fonts.StrokedCustomTypefaceableTextView;
import com.liweiyap.narradir.util.audio.IntroAudioPlayer;
import com.liweiyap.narradir.util.audio.IntroSegmentDictionary;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayIntroductionActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_introduction);

        mCurrentDisplayedCharacterImageView = findViewById(R.id.currentDisplayedCharacterImageView);
        mCurrentDisplayedIntroSegmentTextView = findViewById(R.id.currentDisplayedIntroSegmentTextView);

        // ----------------------------------------------------------------------
        // receive data from previous Activity
        // ----------------------------------------------------------------------

        Intent intent = getIntent();

        final ArrayList<Integer> introSegmentArrayList = intent.getIntegerArrayListExtra(getString(R.string.intro_segments_key));
        mPauseDurationInMilliSecs = intent.getLongExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mBackgroundSoundRawResId = intent.getIntExtra(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        mBackgroundSoundVolume = intent.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mNarrationVolume = intent.getFloatExtra(getString(R.string.narration_volume_key), mNarrationVolume);

        boolean isStartedFromAvalon = intent.getBooleanExtra(getString(R.string.is_started_from_avalon_key), true);
        StrokedCustomTypefaceableTextView gameTitleAvalonTextView = findViewById(R.id.gameTitleAvalonTextView);
        StrokedCustomTypefaceableTextView gameTitleSecretHitlerTextView = findViewById(R.id.gameTitleSecretHitlerTextView);
        if (isStartedFromAvalon)
        {
            gameTitleAvalonTextView.setVisibility(View.VISIBLE);
            gameTitleSecretHitlerTextView.setVisibility(View.INVISIBLE);
        }
        else
        {
            gameTitleAvalonTextView.setVisibility(View.INVISIBLE);
            gameTitleSecretHitlerTextView.setVisibility(View.VISIBLE);
        }

        // handle edge case of mPauseDurationInMilliSecs passed in as 0
        mPauseDurationInMilliSecs = Math.max(mPauseDurationInMilliSecs, IntroAudioPlayer.sMinPauseDurationInMilliSecs);

        // ----------------------------------------------------------------------
        // initialise and prepare audio players
        // ----------------------------------------------------------------------

        mAudioPlayer = new IntroAudioPlayer(
            this,
            introSegmentArrayList, mPauseDurationInMilliSecs, mNarrationVolume,
            mBackgroundSoundRawResId, mBackgroundSoundVolume);

        mAudioPlayer.addExoPlayerListener(new Player.Listener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositionDiscontinuity(Player.@NotNull PositionInfo oldPosition, Player.@NotNull PositionInfo newPosition, @Player.DiscontinuityReason int reason)
            {
                if (reason != Player.DISCONTINUITY_REASON_AUTO_TRANSITION)
                {
                    return;
                }

                int newWindowIdx = mAudioPlayer.getExoPlayerCurrentWindowIndex();
                if (newWindowIdx % 2 == 0)
                {
                    switchCurrentDisplayedCharacterImage(introSegmentArrayList.get(newWindowIdx/2));
                    switchCurrentDisplayedIntroSegmentTextView(introSegmentArrayList.get(newWindowIdx/2));
                }
                else
                {
                    if ( (IntroSegmentDictionary.canPauseManuallyAtEnd(introSegmentArrayList.get(newWindowIdx/2))) &&
                        (mPauseDurationInMilliSecs != IntroAudioPlayer.sMinPauseDurationInMilliSecs) )
                    {
                        mCurrentDisplayedIntroSegmentTextView.setText(
                            (mPauseDurationInMilliSecs == 1000) ?
                                "(PAUSE for " + mPauseDurationInMilliSecs/1000 + " second)" :
                                "(PAUSE for " + mPauseDurationInMilliSecs/1000 + " seconds)");
                    }
                }
            }

            @Override
            public void onPlaybackStateChanged(@Player.State int playbackState)
            {
                if (playbackState == Player.STATE_ENDED)
                {
                    finish();
                }
            }
        });

        // ----------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ----------------------------------------------------------------------

        CustomTypefaceableObserverButton pauseButton = findViewById(R.id.playIntroLayoutPauseButton);
        pauseButton.addOnClickObserver(() -> {
            mAudioPlayer.playClickSound();

            pauseButton.setText(
                mAudioPlayer.isPlayingIntro() ?
                    R.string.pause_button_text_state_inactive :
                    R.string.pause_button_text_state_active);

            mAudioPlayer.toggle(mBackgroundSoundVolume);
        });

        CustomTypefaceableObserverButton stopButton = findViewById(R.id.playIntroLayoutStopButton);
        stopButton.addOnClickObserver(this::finish);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                stopButton.performClick();
            }
        });

        // ----------------------------------------------------------------------
        // miscellaneous UI initialisation
        // ----------------------------------------------------------------------

        switchCurrentDisplayedIntroSegmentTextView(introSegmentArrayList.get(0));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (mAudioPlayer == null)
        {
            return;
        }

        if (mAudioPlayer.isPlayingIntro())
        {
            mAudioPlayer.playIntro(mBackgroundSoundVolume);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mAudioPlayer == null)
        {
            return;
        }

        if (mAudioPlayer.isPlayingIntro())
        {
            mAudioPlayer.pauseIntro();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mAudioPlayer != null)
        {
            mAudioPlayer.freeResources();
        }

        mCurrentDisplayedCharacterImageView.setImageDrawable(null);
        mCurrentDisplayedIntroSegmentTextView.setText("");
    }

    @SuppressLint("NonConstantResourceId")
    private void switchCurrentDisplayedCharacterImage(@RawRes final int resId)
    {
        switch (resId)
        {
            case R.raw.avalonintrosegment1nooberon:
            case R.raw.avalonintrosegment1withoberon:
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.teamevil);
                return;
            case R.raw.avalonintrosegment3nomordred:
            case R.raw.avalonintrosegment3withmordred:
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.merlin_unchecked_unlabelled);
                return;
            case R.raw.avalonintrosegment5withpercivalnomorgana:
            case R.raw.avalonintrosegment5withpercivalwithmorgana:
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.percival_unchecked_unlabelled);
                return;
            case R.raw.secrethitlerintrosegment1small:
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.ic_teamfascists);
                mCurrentDisplayedCharacterImageView.setBackgroundResource(R.drawable.fascist_background);
                return;
            case R.raw.secrethitlerintrosegment1large:
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.ic_fascist);
                mCurrentDisplayedCharacterImageView.setBackgroundResource(R.drawable.fascist_background);
                return;
            case R.raw.avalonintrosegment3nomerlin:
            case R.raw.avalonintrosegment5nopercival:
            case R.raw.avalonintrosegment7:
            case R.raw.secrethitlerintrosegment3small:
            case R.raw.secrethitlerintrosegment4large:
                mCurrentDisplayedCharacterImageView.setImageDrawable(null);
                mCurrentDisplayedCharacterImageView.setBackgroundResource(0);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void switchCurrentDisplayedIntroSegmentTextView(@RawRes final int resId)
    {
        @StringRes final int subtitleId = IntroSegmentDictionary.getSubtitleResIdFromIntroSegmentResId(resId);

        if (subtitleId == 0)
        {
            throw new RuntimeException(
                "PlayIntroductionActivity::switchCurrentDisplayedIntroSegmentTextView(): " +
                "Invalid introduction segment resource ID " + resId);
        }

        mCurrentDisplayedIntroSegmentTextView.setText(subtitleId);
    }

    private IntroAudioPlayer mAudioPlayer;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;

    private ImageView mCurrentDisplayedCharacterImageView;
    private CustomTypefaceableTextView mCurrentDisplayedIntroSegmentTextView;
}