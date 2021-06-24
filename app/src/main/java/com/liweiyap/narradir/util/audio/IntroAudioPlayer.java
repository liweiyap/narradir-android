package com.liweiyap.narradir.util.audio;

import android.content.Context;
import android.media.SoundPool;

import androidx.annotation.NonNull;
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
import com.liweiyap.narradir.R;

import java.util.ArrayList;

public class IntroAudioPlayer
{
    public IntroAudioPlayer(
        final @NonNull Context context,
        final ArrayList<Integer> introSegmentArrayList,
        final long pauseDurationInMilliSecs,
        final float narrationVolume,
        final @RawRes int backgroundSoundRawResId,
        final float backgroundSoundVolume)
    {
        allocateResources(
            context,
            introSegmentArrayList, Math.max(pauseDurationInMilliSecs, IntroAudioPlayer.sMinPauseDurationInMilliSecs), narrationVolume,
            backgroundSoundRawResId, backgroundSoundVolume);
    }

    public void allocateResources(
        final @NonNull Context context,
        final ArrayList<Integer> introSegmentArrayList,
        final long pauseDurationInMilliSecs,
        final float narrationVolume,
        final @RawRes int backgroundSoundRawResId,
        final float backgroundSoundVolume)
    {
        // initialise SoundPool for background noise and click sound
        initSoundPool(context, backgroundSoundRawResId, backgroundSoundVolume);

        // initialise and prepare ExoPlayer for intro segments
        prepareExoPlayer(context, introSegmentArrayList, pauseDurationInMilliSecs, narrationVolume);
    }

    private void initSoundPool(final @NonNull Context context, final @RawRes int backgroundSoundRawResId, final float backgroundSoundVolume)
    {
        mGeneralSoundPool = new SoundPool.Builder()
            .setMaxStreams(2)
            .build();

        mClickSoundId = mGeneralSoundPool.load(context, R.raw.clicksound, 1);

        if (backgroundSoundRawResId != 0)
        {
            mBackgroundSoundId = mGeneralSoundPool.load(context, backgroundSoundRawResId, 1);
            mGeneralSoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
                if ((sampleId == mBackgroundSoundId) && (status == 0))
                {
                    mBackgroundStreamId = soundPool.play(sampleId, backgroundSoundVolume, backgroundSoundVolume, 1, -1, 1f);
                }
            });
        }
    }

    private void prepareExoPlayer(final @NonNull Context context, final ArrayList<Integer> introSegmentArrayList, final long pauseDurationInMilliSecs, final float narrationVolume)
    {
        final RenderersFactory audioOnlyRenderersFactory = (handler, videoListener, audioListener, textOutput, metadataOutput) -> new Renderer[] {
            new MediaCodecAudioRenderer(context, MediaCodecSelector.DEFAULT, handler, audioListener)
        };

        final ExtractorsFactory mp3ExtractorFactory = () -> new Extractor[] {
            new Mp3Extractor()
        };

        mIntroSegmentPlayer = new SimpleExoPlayer.Builder(context, audioOnlyRenderersFactory, ExtractorsFactory.EMPTY).build();
        for (int idx = 0; idx < introSegmentArrayList.size(); ++idx)
        {
            @RawRes int segment = introSegmentArrayList.get(idx);

            ProgressiveMediaSource mediaSource = ExoPlayerMediaSourceCreator.createProgressiveMediaSourceFromResId(context, segment, mp3ExtractorFactory);
            if (mediaSource != null)
            {
                mIntroSegmentPlayer.addMediaSource(mediaSource);

                if (idx == introSegmentArrayList.size() - 1)
                {
                    break;
                }

                SilenceMediaSource silence = new SilenceMediaSource(
                    IntroSegmentDictionary.canPauseManuallyAtEnd(segment) ?
                        pauseDurationInMilliSecs * 1000 :
                        sMinPauseDurationInMilliSecs * 1000);
                mIntroSegmentPlayer.addMediaSource(silence);
            }
        }

        if (mIntroSegmentPlayer.getMediaItemCount() != 2 * introSegmentArrayList.size() - 1)
        {
            throw new RuntimeException(
                "IntroAudioPlayer::prepareExoPlayer(): " +
                    "Invalid no of MediaSources for SimpleExoPlayer; " +
                    introSegmentArrayList.size() + " segments but " +
                    mIntroSegmentPlayer.getMediaItemCount() + " media sources.");
        }

        mIntroSegmentPlayer.setVolume(narrationVolume);
        mIntroSegmentPlayer.prepare();
    }

    public void addExoPlayerListener(Player.Listener listener)
    {
        if (mIntroSegmentPlayer == null)
        {
            return;
        }

        mIntroSegmentPlayer.addListener(listener);
    }

    public int getExoPlayerCurrentWindowIndex()
    {
        if (mIntroSegmentPlayer == null)
        {
            return 0;
        }

        return mIntroSegmentPlayer.getCurrentWindowIndex();
    }

    public void playClickSound()
    {
        if (mGeneralSoundPool == null)
        {
            return;
        }

        mGeneralSoundPool.play(mClickSoundId, 1f, 1f, 2, 0, 1f);
    }

    public void toggle(final float backgroundSoundVolume)
    {
        if (mIsPlaying)
        {
            mIsPlaying = false;
            pauseIntro();
        }
        else
        {
            mIsPlaying = true;
            playIntro(backgroundSoundVolume);
        }
    }

    public boolean isPlayingIntro()
    {
        return mIsPlaying;
    }

    public void playIntro(final float backgroundSoundVolume)
    {
        if ( (mIntroSegmentPlayer == null) || (mGeneralSoundPool == null) )
        {
            return;
        }

        mIntroSegmentPlayer.setPlayWhenReady(true);

        if (mIsFirstCall)
        {
            mIsFirstCall = false;
            return;
        }

        mBackgroundStreamId = mGeneralSoundPool.play(mBackgroundSoundId, backgroundSoundVolume, backgroundSoundVolume, 1, -1, 1f);
    }

    public void pauseIntro()
    {
        if (mIntroSegmentPlayer != null)
        {
            mIntroSegmentPlayer.setPlayWhenReady(false);
        }

        if ( (mGeneralSoundPool != null) && (mBackgroundStreamId != 0) )
        {
            mGeneralSoundPool.stop(mBackgroundStreamId);
        }
    }

    public void freeResources()
    {
        if (mIntroSegmentPlayer != null)
        {
            mIntroSegmentPlayer.release();
            mIntroSegmentPlayer = null;
        }

        if (mGeneralSoundPool != null)
        {
            mGeneralSoundPool.release();
            mGeneralSoundPool = null;
        }
    }

    public static final long sMinPauseDurationInMilliSecs = 500;

    private SimpleExoPlayer mIntroSegmentPlayer;

    private SoundPool mGeneralSoundPool;
    private int mBackgroundSoundId;
    private int mBackgroundStreamId = 0;
    private int mClickSoundId;

    private boolean mIsPlaying = true;
    private boolean mIsFirstCall = true;
}