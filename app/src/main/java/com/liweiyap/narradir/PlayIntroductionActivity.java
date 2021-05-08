package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RawRes;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SilenceMediaSource;
import com.liweiyap.narradir.utils.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.utils.MediaSourceCreator;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.utils.fonts.StrokedCustomTypefaceableTextView;

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

        mIntroSegmentArrayList = intent.getIntegerArrayListExtra(getString(R.string.intro_segments_key));
        mPauseDurationInMilliSecs = intent.getLongExtra(getString(R.string.pause_duration_key), mMinPauseDurationInMilliSecs);
        mBackgroundSoundRawResId = intent.getIntExtra(getString(R.string.background_sound_key), 0);
        mBackgroundSoundVolume = intent.getFloatExtra(getString(R.string.background_volume_key), 1f);
        mNarrationVolume = intent.getFloatExtra(getString(R.string.narration_volume_key), 1f);

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
        mPauseDurationInMilliSecs = Math.max(mPauseDurationInMilliSecs, mMinPauseDurationInMilliSecs);

        // ----------------------------------------------------------------------
        // initialise SoundPool for background noise and click sound
        // ----------------------------------------------------------------------

        mGeneralSoundPool = new SoundPool.Builder()
            .setMaxStreams(2)
            .build();

        mClickSoundId = mGeneralSoundPool.load(this, R.raw.clicksound, 1);

        if (mBackgroundSoundRawResId != 0)
        {
            mBackgroundSoundId = mGeneralSoundPool.load(this, mBackgroundSoundRawResId, 1);
            mGeneralSoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
                if ((sampleId == mBackgroundSoundId) && (status == 0))
                {
                    mBackgroundStreamId = soundPool.play(sampleId, mBackgroundSoundVolume, mBackgroundSoundVolume, 1, -1, 1f);
                }
            });
        }

        // ----------------------------------------------------------------------
        // initialise and prepare ExoPlayer for intro segments
        // ----------------------------------------------------------------------

        RenderersFactory audioOnlyRenderersFactory = (handler, videoListener, audioListener, textOutput, metadataOutput) -> new Renderer[] {
            new MediaCodecAudioRenderer(this, MediaCodecSelector.DEFAULT, handler, audioListener)
        };

        mIntroSegmentPlayer = new SimpleExoPlayer.Builder(this, audioOnlyRenderersFactory, ExtractorsFactory.EMPTY).build();
        for (int idx = 0; idx < mIntroSegmentArrayList.size(); ++idx)
        {
            @RawRes int segment = mIntroSegmentArrayList.get(idx);

            ProgressiveMediaSource mediaSource = MediaSourceCreator.createProgressiveMediaSourceFromResId(this, segment, mMp3ExtractorFactory);
            if (mediaSource != null)
            {
                mIntroSegmentPlayer.addMediaSource(mediaSource);

                if (idx == mIntroSegmentArrayList.size() - 1)
                {
                    break;
                }

                SilenceMediaSource silence = new SilenceMediaSource(
                    canPauseManuallyAtEnd(segment) ?
                        mPauseDurationInMilliSecs * 1000 :
                        mMinPauseDurationInMilliSecs * 1000);
                mIntroSegmentPlayer.addMediaSource(silence);
            }
        }

        if (mIntroSegmentPlayer.getMediaItemCount() != 2 * mIntroSegmentArrayList.size() - 1)
        {
            throw new RuntimeException(
                "PlayIntroductionActivity::onCreate(): " +
                    "Invalid no of MediaSources for introduction segment ExoPlayer; " +
                    mIntroSegmentArrayList.size() + " segments but " +
                    mIntroSegmentPlayer.getMediaItemCount() + " media sources.");
        }

        mIntroSegmentPlayer.addListener(new Player.EventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason)
            {
                if (reason != Player.DISCONTINUITY_REASON_PERIOD_TRANSITION)
                {
                    return;
                }

                int newWindowIdx = mIntroSegmentPlayer.getCurrentWindowIndex();
                if (newWindowIdx % 2 == 0)
                {
                    switchCurrentDisplayedCharacterImage(mIntroSegmentArrayList.get(newWindowIdx/2));
                    switchCurrentDisplayedIntroSegmentTextView(mIntroSegmentArrayList.get(newWindowIdx/2));
                }
                else
                {
                    if ( (canPauseManuallyAtEnd(mIntroSegmentArrayList.get(newWindowIdx/2))) &&
                         (mPauseDurationInMilliSecs != mMinPauseDurationInMilliSecs) )
                    {
                        String text;

                        if (mPauseDurationInMilliSecs == 1000)
                        {
                            text = "(PAUSE for " + mPauseDurationInMilliSecs/1000 + " second)";
                        }
                        else
                        {
                            text = "(PAUSE for " + mPauseDurationInMilliSecs/1000 + " seconds)";
                        }

                        mCurrentDisplayedIntroSegmentTextView.setText(text);
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

        mIntroSegmentPlayer.setVolume(mNarrationVolume);
        mIntroSegmentPlayer.prepare();

        // ----------------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ----------------------------------------------------------------------

        CustomTypefaceableObserverButton pauseButton = findViewById(R.id.playIntroLayoutPauseButton);
        pauseButton.addOnClickObserver(() -> {
            mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 2, 0, 1f);

            if (mIsPlaying)
            {
                mIsPlaying = false;
                pause();
                pauseButton.setText(R.string.pause_button_text_state_inactive);
            }
            else
            {
                mIsPlaying = true;
                play();
                pauseButton.setText(R.string.pause_button_text_state_active);
            }
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

        switchCurrentDisplayedIntroSegmentTextView(mIntroSegmentArrayList.get(0));
        mIsPlaying = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        play();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        pause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mIntroSegmentPlayer.release();
        mIntroSegmentPlayer = null;

        mGeneralSoundPool.release();
        mGeneralSoundPool = null;

        mCurrentDisplayedCharacterImageView.setImageDrawable(null);
        mCurrentDisplayedIntroSegmentTextView.setText("");
    }

    private void play()
    {
        mIntroSegmentPlayer.setPlayWhenReady(true);
        mBackgroundStreamId = mGeneralSoundPool.play(mBackgroundSoundId, mBackgroundSoundVolume, mBackgroundSoundVolume, 1, -1, 1f);
    }

    private void pause()
    {
        mIntroSegmentPlayer.setPlayWhenReady(false);

        if (mBackgroundStreamId != 0)
        {
            mGeneralSoundPool.stop(mBackgroundStreamId);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private boolean canPauseManuallyAtEnd(@RawRes final int resId)
    {
        switch (resId)
        {
            case R.raw.avalonintrosegment1nooberon:
            case R.raw.avalonintrosegment1withoberon:
            case R.raw.avalonintrosegment3nomordred:
            case R.raw.avalonintrosegment3withmordred:
            case R.raw.avalonintrosegment5withpercivalnomorgana:
            case R.raw.avalonintrosegment5withpercivalwithmorgana:
            case R.raw.secrethitlerintrosegment1small:
            case R.raw.secrethitlerintrosegment1large:
            case R.raw.secrethitlerintrosegment2large:
                return true;
            default:
                return false;
        }
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
        switch (resId)
        {
            case R.raw.avalonintrosegment0:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment0_text);
                return;
            case R.raw.avalonintrosegment1nooberon:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment1nooberon_text);
                return;
            case R.raw.avalonintrosegment1withoberon:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment1withoberon_text);
                return;
            case R.raw.avalonintrosegment2:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment2_text);
                return;
            case R.raw.avalonintrosegment3nomerlin:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment3nomerlin_text);
                return;
            case R.raw.avalonintrosegment3nomordred:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment3nomordred_text);
                return;
            case R.raw.avalonintrosegment3withmordred:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment3withmordred_text);
                return;
            case R.raw.avalonintrosegment4:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment4_text);
                return;
            case R.raw.avalonintrosegment5nopercival:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment5nopercival_text);
                return;
            case R.raw.avalonintrosegment5withpercivalnomorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment5withpercivalnomorgana_text);
                return;
            case R.raw.avalonintrosegment5withpercivalwithmorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment5withpercivalwithmorgana_text);
                return;
            case R.raw.avalonintrosegment6withpercivalnomorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment6withpercivalnomorgana_text);
                return;
            case R.raw.avalonintrosegment6withpercivalwithmorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment6withpercivalwithmorgana_text);
                return;
            case R.raw.avalonintrosegment7:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.avalonintrosegment7_text);
                return;
            case R.raw.secrethitlerintrosegment0small:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment0small_text);
                return;
            case R.raw.secrethitlerintrosegment1small:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment1small_text);
                return;
            case R.raw.secrethitlerintrosegment2small:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment2small_text);
                return;
            case R.raw.secrethitlerintrosegment3small:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment3small_text);
                return;
            case R.raw.secrethitlerintrosegment0large:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment0large_text);
                return;
            case R.raw.secrethitlerintrosegment1large:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment1large_text);
                return;
            case R.raw.secrethitlerintrosegment2large:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment2large_text);
                return;
            case R.raw.secrethitlerintrosegment3large:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment3large_text);
                return;
            case R.raw.secrethitlerintrosegment4large:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.secrethitlerintrosegment4large_text);
        }
    }

    private long mPauseDurationInMilliSecs = 5000;
    private final long mMinPauseDurationInMilliSecs = 500;

    private ArrayList<Integer> mIntroSegmentArrayList;
    private SimpleExoPlayer mIntroSegmentPlayer;
    private final ExtractorsFactory mMp3ExtractorFactory = () -> new Extractor[] {
        new Mp3Extractor()
    };
    private float mNarrationVolume = 1f;

    private SoundPool mGeneralSoundPool;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private int mBackgroundSoundId;
    private int mBackgroundStreamId = 0;
    private float mBackgroundSoundVolume = 1f;
    private int mClickSoundId;

    private boolean mIsPlaying;

    private ImageView mCurrentDisplayedCharacterImageView;
    private CustomTypefaceableTextView mCurrentDisplayedIntroSegmentTextView;
}