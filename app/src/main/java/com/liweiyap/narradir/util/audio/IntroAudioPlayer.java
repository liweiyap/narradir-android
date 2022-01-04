package com.liweiyap.narradir.util.audio;

import android.content.Context;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SilenceMediaSource;
import com.liweiyap.narradir.R;

import java.util.ArrayList;

public class IntroAudioPlayer
{
    public IntroAudioPlayer(
        final @NonNull Context context,
        final ArrayList<String> introSegmentArrayList,
        final long pauseDurationInMilliSecs,
        final float narrationVolume,
        final String backgroundSoundName,
        final float backgroundSoundVolume)
    {
        allocateResources(
            context,
            introSegmentArrayList, pauseDurationInMilliSecs, narrationVolume,
            backgroundSoundName, backgroundSoundVolume);
    }

    public void allocateResources(
        final @NonNull Context context,
        final ArrayList<String> introSegmentArrayList,
        final long pauseDurationInMilliSecs,
        final float narrationVolume,
        final String backgroundSoundName,
        final float backgroundSoundVolume)
    {
        // initialise SoundPool for background noise and click sound
        initSoundPool(context, backgroundSoundName, backgroundSoundVolume);

        // initialise and prepare ExoPlayer for intro segments
        prepareExoPlayer(context, introSegmentArrayList, Math.max(pauseDurationInMilliSecs, IntroAudioPlayer.sMinPauseDurationInMilliSecs), narrationVolume);
    }

    private void initSoundPool(final @NonNull Context context, final @NonNull String backgroundSoundName, final float backgroundSoundVolume)
    {
        mGeneralSoundPool = new SoundPool.Builder()
            .setMaxStreams(2)
            .build();

        mClickSoundId = mGeneralSoundPool.load(context, R.raw.clicksound, 1);

        final Integer loadedBackgroundSound = loadBackgroundSound(context, backgroundSoundName);
        if (loadedBackgroundSound == null)
        {
            return;
        }

        mBackgroundSoundId = loadedBackgroundSound;
        mGeneralSoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if ((sampleId == mBackgroundSoundId) && (status == 0))
            {
                mBackgroundStreamId = soundPool.play(sampleId, backgroundSoundVolume, backgroundSoundVolume, 1, -1, 1f);
            }
        });
    }

    private @Nullable Integer loadBackgroundSound(final @NonNull Context context, final @NonNull String backgroundSoundName)
    {
        if (mGeneralSoundPool == null)
        {
            return null;
        }

        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // the sound ID that is itself loaded and returned should still be constant; there's no indication otherwise in the Android documentation.
        if (backgroundSoundName.equals(context.getString(R.string.backgroundsound_cards)))
        {
            return mGeneralSoundPool.load(context, R.raw.backgroundcards, 1);
        }
        else if (backgroundSoundName.equals(context.getString(R.string.backgroundsound_crickets)))
        {
            return mGeneralSoundPool.load(context, R.raw.backgroundcrickets, 1);
        }
        else if (backgroundSoundName.equals(context.getString(R.string.backgroundsound_fireplace)))
        {
            return mGeneralSoundPool.load(context, R.raw.backgroundfireplace, 1);
        }
        else if (backgroundSoundName.equals(context.getString(R.string.backgroundsound_rain)))
        {
            return mGeneralSoundPool.load(context, R.raw.backgroundrain, 1);
        }
        else if (backgroundSoundName.equals(context.getString(R.string.backgroundsound_rainforest)))
        {
            return mGeneralSoundPool.load(context, R.raw.backgroundrainforest, 1);
        }
        else if (backgroundSoundName.equals(context.getString(R.string.backgroundsound_rainstorm)))
        {
            return mGeneralSoundPool.load(context, R.raw.backgroundrainstorm, 1);
        }
        else if (backgroundSoundName.equals(context.getString(R.string.backgroundsound_wolves)))
        {
            return mGeneralSoundPool.load(context, R.raw.backgroundwolves, 1);
        }

        return null;
    }

    private void prepareExoPlayer(final @NonNull Context context, final @NonNull ArrayList<String> introSegmentArrayList, final long pauseDurationInMilliSecs, final float narrationVolume)
    {
        final RenderersFactory audioOnlyRenderersFactory = (handler, videoListener, audioListener, textOutput, metadataOutput) -> new Renderer[] {
            new MediaCodecAudioRenderer(context, MediaCodecSelector.DEFAULT, handler, audioListener)
        };

        final ExtractorsFactory mp3ExtractorFactory = () -> new Extractor[] {
            new Mp3Extractor()
        };

        mIntroSegmentPlayer = new ExoPlayer.Builder(context, audioOnlyRenderersFactory, new DefaultMediaSourceFactory(context, ExtractorsFactory.EMPTY)).build();
        for (int idx = 0; idx < introSegmentArrayList.size(); ++idx)
        {
            final String segment = introSegmentArrayList.get(idx);

            ProgressiveMediaSource mediaSource = ExoPlayerMediaSourceCreator.createProgressiveMediaSourceFromRes(context, segment, mp3ExtractorFactory);
            if (mediaSource == null)
            {
                continue;
            }

            mIntroSegmentPlayer.addMediaSource(mediaSource);

            if (idx == introSegmentArrayList.size() - 1)
            {
                break;
            }

            SilenceMediaSource silence = new SilenceMediaSource(
                IntroSegmentDictionary.canPauseManuallyAtEnd(context, segment) ?
                    pauseDurationInMilliSecs * 1000 :
                    sMinPauseDurationInMilliSecs * 1000);
            mIntroSegmentPlayer.addMediaSource(silence);
        }

        if (mIntroSegmentPlayer.getMediaItemCount() != 2 * introSegmentArrayList.size() - 1)
        {
            throw new RuntimeException(
                "IntroAudioPlayer::prepareExoPlayer(): " +
                    "Invalid no of MediaSources for ExoPlayer; " +
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

    public int getExoPlayerCurrentMediaItemIndex()
    {
        if (mIntroSegmentPlayer == null)
        {
            return 0;
        }

        return mIntroSegmentPlayer.getCurrentMediaItemIndex();
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

    private ExoPlayer mIntroSegmentPlayer;

    private SoundPool mGeneralSoundPool;
    private int mBackgroundSoundId;
    private int mBackgroundStreamId = 0;
    private int mClickSoundId;

    private boolean mIsPlaying = true;
    private boolean mIsFirstCall = true;
}