package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.RawRes;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SilenceMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableTextView;

import java.util.ArrayList;

public class PlayIntroductionActivity extends FullScreenPortraitActivity
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

        mIntroSegmentArrayList = intent.getIntegerArrayListExtra("INTRO_SEGMENTS");
        mPauseDurationInMilliSecs = intent.getLongExtra("PAUSE_DURATION", mMinPauseDurationInMilliSecs);
        mBackgroundSoundRawResId = intent.getIntExtra("BACKGROUND_SOUND", 0);

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

            ProgressiveMediaSource mediaSource = createMediaSourceFromId(segment);
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
                        mCurrentDisplayedIntroSegmentTextView.setText("(PAUSE for " + mPauseDurationInMilliSecs/1000 + " seconds)");
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

        mIntroSegmentPlayer.prepare();

        // ----------------------------------------------------------------------
        // initialise SoundPool for background noise
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
                    mBackgroundStreamId = soundPool.play(sampleId, 1f, 1f, 1, -1, 1f);
                }
            });
        }

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
        pause();

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
        mBackgroundStreamId = mGeneralSoundPool.play(mBackgroundSoundId, 1f, 1f, 1, -1, 1f);
    }

    private void pause()
    {
        mIntroSegmentPlayer.setPlayWhenReady(false);

        if (mBackgroundStreamId != 0)
        {
            mGeneralSoundPool.stop(mBackgroundStreamId);
        }
    }

    private ProgressiveMediaSource createMediaSourceFromId(@RawRes int resId)
    {
        try
        {
            RawResourceDataSource dataSource = new RawResourceDataSource(this);
            DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(resId));
            dataSource.open(dataSpec);

            Assertions.checkNotNull(dataSource.getUri());
            MediaItem mediaItem = MediaItem.fromUri(dataSource.getUri());
            MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(this, mMp3ExtractorFactory);
            return (ProgressiveMediaSource) mediaSourceFactory.createMediaSource(mediaItem);
        }
        catch (RawResourceDataSource.RawResourceDataSourceException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressLint("NonConstantResourceId")
    private boolean canPauseManuallyAtEnd(@RawRes final int resId)
    {
        switch (resId)
        {
            case R.raw.introsegment1nooberon:
            case R.raw.introsegment1withoberon:
            case R.raw.introsegment3nomordred:
            case R.raw.introsegment3withmordred:
            case R.raw.introsegment5withpercivalnomorgana:
            case R.raw.introsegment5withpercivalwithmorgana:
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
            case R.raw.introsegment1nooberon:
            case R.raw.introsegment1withoberon:
                // TODO: Scan image of evil insignia
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.minion0_unchecked_unlabelled);
                return;
            case R.raw.introsegment3nomordred:
            case R.raw.introsegment3withmordred:
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.merlin_unchecked_unlabelled);
                return;
            case R.raw.introsegment5withpercivalnomorgana:
            case R.raw.introsegment5withpercivalwithmorgana:
                mCurrentDisplayedCharacterImageView.setImageResource(R.drawable.percival_unchecked_unlabelled);
                return;
            case R.raw.introsegment3nomerlin:
            case R.raw.introsegment5nopercival:
            case R.raw.introsegment7:
                mCurrentDisplayedCharacterImageView.setImageDrawable(null);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void switchCurrentDisplayedIntroSegmentTextView(@RawRes final int resId)
    {
        switch (resId)
        {
            case R.raw.introsegment0:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment0_text);
                return;
            case R.raw.introsegment1nooberon:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment1nooberon_text);
                return;
            case R.raw.introsegment1withoberon:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment1withoberon_text);
                return;
            case R.raw.introsegment2:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment2_text);
                return;
            case R.raw.introsegment3nomerlin:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment3nomerlin_text);
                return;
            case R.raw.introsegment3nomordred:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment3nomordred_text);
                return;
            case R.raw.introsegment3withmordred:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment3withmordred_text);
                return;
            case R.raw.introsegment4:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment4_text);
                return;
            case R.raw.introsegment5nopercival:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment5nopercival_text);
                return;
            case R.raw.introsegment5withpercivalnomorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment5withpercivalnomorgana_text);
                return;
            case R.raw.introsegment5withpercivalwithmorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment5withpercivalwithmorgana_text);
                return;
            case R.raw.introsegment6withpercivalnomorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment6withpercivalnomorgana_text);
                return;
            case R.raw.introsegment6withpercivalwithmorgana:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment6withpercivalwithmorgana_text);
                return;
            case R.raw.introsegment7:
                mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment7_text);
        }
    }

    private long mPauseDurationInMilliSecs = 5000;
    private final long mMinPauseDurationInMilliSecs = 500;

    private ArrayList<Integer> mIntroSegmentArrayList;
    private SimpleExoPlayer mIntroSegmentPlayer;
    private final ExtractorsFactory mMp3ExtractorFactory = () -> new Extractor[] {
        new Mp3Extractor()
    };

    private SoundPool mGeneralSoundPool;
    private @RawRes int mBackgroundSoundRawResId = 0;
    private int mBackgroundSoundId;
    private int mBackgroundStreamId = 0;
    private int mClickSoundId;

    private boolean mIsPlaying;

    private ImageView mCurrentDisplayedCharacterImageView;
    private CustomTypefaceableTextView mCurrentDisplayedIntroSegmentTextView;
}
