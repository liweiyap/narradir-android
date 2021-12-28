package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import com.google.android.exoplayer2.Player;
import com.liweiyap.narradir.ui.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.IntentHelper;
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

        mBackgroundSoundName = getString(R.string.backgroundsound_none);

        mCurrentDisplayedCharacterImageView = findViewById(R.id.currentDisplayedCharacterImageView);
        mCurrentDisplayedIntroSegmentTextView = findViewById(R.id.currentDisplayedIntroSegmentTextView);

        // ----------------------------------------------------------------------
        // receive data from previous Activity
        // ----------------------------------------------------------------------

        Intent intent = getIntent();

        final ArrayList<String> introSegmentArrayList = intent.getStringArrayListExtra(getString(R.string.intro_segments_key));
        mPauseDurationInMilliSecs = intent.getLongExtra(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mBackgroundSoundVolume = intent.getFloatExtra(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mNarrationVolume = intent.getFloatExtra(getString(R.string.narration_volume_key), mNarrationVolume);
        mBackgroundSoundName = IntentHelper.getStringExtra(intent, getString(R.string.background_sound_name_key), getString(R.string.backgroundsound_none));

        boolean isStartedFromAvalon = intent.getBooleanExtra(getString(R.string.is_started_from_avalon_key), true);
        findViewById(R.id.gameTitleAvalonTextView).setVisibility(isStartedFromAvalon ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.gameTitleSecretHitlerTextView).setVisibility(isStartedFromAvalon ? View.INVISIBLE : View.VISIBLE);

        // handle edge case of mPauseDurationInMilliSecs passed in as 0
        mPauseDurationInMilliSecs = Math.max(mPauseDurationInMilliSecs, IntroAudioPlayer.sMinPauseDurationInMilliSecs);

        // ----------------------------------------------------------------------
        // initialise and prepare audio players
        // ----------------------------------------------------------------------

        mAudioPlayer = new IntroAudioPlayer(
            this,
            introSegmentArrayList, mPauseDurationInMilliSecs, mNarrationVolume,
            mBackgroundSoundName, mBackgroundSoundVolume);

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

                int newWindowIdx = mAudioPlayer.getExoPlayerCurrentMediaItemIndex();
                if (newWindowIdx % 2 == 0)
                {
                    switchCurrentDisplayedCharacterImage(introSegmentArrayList.get(newWindowIdx/2));
                    switchCurrentDisplayedIntroSegmentTextView(introSegmentArrayList.get(newWindowIdx/2));
                }
                else
                {
                    if ( (IntroSegmentDictionary.canPauseManuallyAtEnd(PlayIntroductionActivity.this, introSegmentArrayList.get(newWindowIdx/2))) &&
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

        if (mCurrentDisplayedCharacterImageView != null)
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
        }

        if (mCurrentDisplayedIntroSegmentTextView != null)
        {
            mCurrentDisplayedIntroSegmentTextView.setText("");
        }
    }

    private void switchCurrentDisplayedCharacterImage(final @NonNull String resName)
    {
        if (mCurrentDisplayedCharacterImageView == null)
        {
            return;
        }

        // does not look elegant but it's safe because resource ID integers are non-constant from Gradle version >4.
        if (resName.equals(getString(R.string.avalonintrosegment1nooberon_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.teamevil);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment1withoberon_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.teamevil);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment3nomordred_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.merlin_unchecked_unlabelled);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment3withmordred_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.merlin_unchecked_unlabelled);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment5withpercivalnomorgana_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.percival_unchecked_unlabelled);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment5withpercivalwithmorgana_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.percival_unchecked_unlabelled);
        }
        else if (resName.equals(getString(R.string.secrethitlerintrosegment1small_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.ic_teamfascists);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(R.drawable.fascist_background);
        }
        else if (resName.equals(getString(R.string.secrethitlerintrosegment1large_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.ic_fascist);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(R.drawable.fascist_background);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment3nomerlin_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(0);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment5nopercival_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(0);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment7_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(0);
        }
        else if (resName.equals(getString(R.string.secrethitlerintrosegment3small_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(0);
        }
        else if (resName.equals(getString(R.string.secrethitlerintrosegment4large_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(0);
        }
    }

    private void switchCurrentDisplayedIntroSegmentTextView(final @NonNull String resName)
    {
        if (mCurrentDisplayedIntroSegmentTextView == null)
        {
            return;
        }

        final String subtitle = IntroSegmentDictionary.getSubtitleFromIntroSegmentRes(this, resName);

        if (subtitle == null)
        {
            throw new RuntimeException(
                "PlayIntroductionActivity::switchCurrentDisplayedIntroSegmentTextView(): " +
                "Invalid introduction segment resource name " + resName);
        }

        mCurrentDisplayedIntroSegmentTextView.setText(subtitle);
    }

    private IntroAudioPlayer mAudioPlayer;

    private long mPauseDurationInMilliSecs = 5000;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;
    private String mBackgroundSoundName;

    private ImageView mCurrentDisplayedCharacterImageView;
    private CustomTypefaceableTextView mCurrentDisplayedIntroSegmentTextView;
}