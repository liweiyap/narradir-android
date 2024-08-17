package com.liweiyap.narradir.util.audio

import android.content.Context
import android.media.SoundPool
import android.os.Handler

import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.extractor.Extractor
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.exoplayer.Renderer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.AudioRendererEventListener
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.metadata.MetadataOutput
import androidx.media3.extractor.mp3.Mp3Extractor
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.source.SilenceMediaSource
import androidx.media3.exoplayer.text.TextOutput
import androidx.media3.exoplayer.video.VideoRendererEventListener

import com.liweiyap.narradir.R

class IntroAudioPlayer(
    context: Context,
    introSegmentArrayList: ArrayList<String>,
    pauseDurationInMilliSecs: Long,
    narrationVolume: Float,
    backgroundSoundName: String,
    private val mBackgroundSoundVolume: Float,
) {
    init {
        allocateResources(
            context,
            introSegmentArrayList, pauseDurationInMilliSecs, narrationVolume,
            backgroundSoundName, mBackgroundSoundVolume)
    }

    fun allocateResources(
        context: Context,
        introSegmentArrayList: ArrayList<String>,
        pauseDurationInMilliSecs: Long,
        narrationVolume: Float,
        backgroundSoundName: String,
        backgroundSoundVolume: Float,
    ) {
        // initialise SoundPool for background noise and click sound
        initSoundPool(context, backgroundSoundName, backgroundSoundVolume)

        // initialise and prepare ExoPlayer for intro segments
        prepareExoPlayer(context, introSegmentArrayList, pauseDurationInMilliSecs.coerceAtLeast(sMinPauseDurationInMilliSecs), narrationVolume)
    }

    private fun initSoundPool(context: Context, backgroundSoundName: String, backgroundSoundVolume: Float) {
        mGeneralSoundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        val loadedBackgroundSound: Int = loadBackgroundSound(context, backgroundSoundName)
            ?: return

        mBackgroundSoundId = loadedBackgroundSound
        mGeneralSoundPool?.let {
            it.setOnLoadCompleteListener { soundPool: SoundPool, sampleId: Int, status: Int ->
                if ((sampleId == mBackgroundSoundId) && (status == 0)) {
                    mBackgroundStreamId = soundPool.play(sampleId, backgroundSoundVolume, backgroundSoundVolume, 1, -1, 1F)
                }
            }
        }
    }

    private fun loadBackgroundSound(context: Context, backgroundSoundName: String): Int? {
        if (mGeneralSoundPool == null) {
            return null
        }

        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // the sound ID that is itself loaded and returned should still be constant; there's no indication otherwise in the Android documentation.
        return when (backgroundSoundName) {
            context.getString(R.string.backgroundsound_cards) -> mGeneralSoundPool!!.load(context, R.raw.backgroundcards, 1)
            context.getString(R.string.backgroundsound_crickets) -> mGeneralSoundPool!!.load(context, R.raw.backgroundcrickets, 1)
            context.getString(R.string.backgroundsound_fireplace) -> mGeneralSoundPool!!.load(context, R.raw.backgroundfireplace, 1)
            context.getString(R.string.backgroundsound_rain) ->  mGeneralSoundPool!!.load(context, R.raw.backgroundrain, 1)
            context.getString(R.string.backgroundsound_rainforest) -> mGeneralSoundPool!!.load(context, R.raw.backgroundrainforest, 1)
            context.getString(R.string.backgroundsound_rainstorm) -> mGeneralSoundPool!!.load(context, R.raw.backgroundrainstorm, 1)
            context.getString(R.string.backgroundsound_wolves) -> mGeneralSoundPool!!.load(context, R.raw.backgroundwolves, 1)
            else -> null
        }
    }

    @OptIn(UnstableApi::class)
    private fun prepareExoPlayer(context: Context, introSegmentArrayList: ArrayList<String>, pauseDurationInMilliSecs: Long, narrationVolume: Float) {
        val audioOnlyRenderersFactory = RenderersFactory { handler: Handler?, _: VideoRendererEventListener?, audioListener: AudioRendererEventListener?, _: TextOutput?, _: MetadataOutput? ->
            arrayOf<Renderer>(MediaCodecAudioRenderer(context, MediaCodecSelector.DEFAULT, handler, audioListener))
        }

        val mp3ExtractorFactory = ExtractorsFactory {
            arrayOf<Extractor>(Mp3Extractor())
        }

        mIntroSegmentPlayer = ExoPlayer.Builder(context, audioOnlyRenderersFactory, DefaultMediaSourceFactory(context, ExtractorsFactory.EMPTY)).build()
        mIntroSegmentTypeArrayList = arrayListOf()
        for (idx: Int in introSegmentArrayList.indices) {
            val segment: String = introSegmentArrayList[idx]

            val mediaSource: ProgressiveMediaSource = ExoPlayerMediaSourceCreator.createProgressiveMediaSourceFromRes(context, segment, mp3ExtractorFactory)
                ?: continue

            mIntroSegmentPlayer!!.addMediaSource(mediaSource)
            mIntroSegmentTypeArrayList!!.add(IntroAudioPlayerSegment.Narration(mIntroSegmentIndex = idx))

            if (idx == introSegmentArrayList.size - 1) {
                break
            }

            if ( (IntroSegmentDictionary.canPauseManuallyAtEnd(context = context, resName = segment)) &&
                 (pauseDurationInMilliSecs > sMinPauseDurationInMilliSecs) )
            {
                val silence = SilenceMediaSource(sPauseInterval * 1000)

                for (duration: Long in pauseDurationInMilliSecs downTo 1000L step sPauseInterval) {
                    mIntroSegmentPlayer!!.addMediaSource(silence)
                    mIntroSegmentTypeArrayList!!.add(IntroAudioPlayerSegment.Pause(mRemainingDurationInMilliSecs = duration))
                }
            }
            else {
                mIntroSegmentPlayer!!.addMediaSource(SilenceMediaSource(sMinPauseDurationInMilliSecs * 1000))
                mIntroSegmentTypeArrayList!!.add(IntroAudioPlayerSegment.Pause(mRemainingDurationInMilliSecs = sMinPauseDurationInMilliSecs))
            }
        }

        if (mIntroSegmentPlayer!!.mediaItemCount != mIntroSegmentTypeArrayList!!.size) {
            throw RuntimeException(
                "IntroAudioPlayer::prepareExoPlayer(): " +
                    "Invalid no of MediaSources for ExoPlayer; ${mIntroSegmentTypeArrayList!!.size} segments but ${mIntroSegmentPlayer!!.mediaItemCount} media sources.")
        }

        mIntroSegmentPlayer!!.volume = narrationVolume
        mIntroSegmentPlayer!!.prepare()
    }

    fun addExoPlayerListener(listener: Player.Listener) {
        mIntroSegmentPlayer?.addListener(listener)
    }

    fun getExoPlayerCurrentMediaItemIndex(): Int {
        return if (mIntroSegmentPlayer == null) 0 else mIntroSegmentPlayer!!.currentMediaItemIndex
    }

    fun getExoPlayerCurrentMediaItemType(): IntroAudioPlayerSegment {
        val idx: Int = getExoPlayerCurrentMediaItemIndex()
        if ( (mIntroSegmentTypeArrayList == null) ||
             (idx < 0) ||
             (idx >= mIntroSegmentTypeArrayList!!.size) )
        {
            return IntroAudioPlayerSegment.Error
        }

        return mIntroSegmentTypeArrayList!![idx]
    }

    fun toggle() {
        if (mIsPlaying) {
            mIsPlaying = false
            pauseIntro()
        }
        else {
            mIsPlaying = true
            playIntro()
        }
    }

    fun isPlayingIntro(): Boolean {
        return mIsPlaying
    }

    fun playIntro() {
        if ( (mIntroSegmentPlayer == null) || (mGeneralSoundPool == null) ) {
            return
        }

        mIntroSegmentPlayer!!.playWhenReady = true

        if (mIsFirstCall) {
            mIsFirstCall = false
            return
        }

        mBackgroundStreamId = mGeneralSoundPool!!.play(mBackgroundSoundId, mBackgroundSoundVolume, mBackgroundSoundVolume, 1, -1, 1F)
    }

    fun pauseIntro() {
        mIntroSegmentPlayer?.playWhenReady = false

        if (mBackgroundStreamId != 0) {
            mGeneralSoundPool?.stop(mBackgroundStreamId)
        }
    }

    fun freeResources() {
        mIntroSegmentPlayer?.release()
        mIntroSegmentPlayer = null

        mGeneralSoundPool?.release()
        mGeneralSoundPool = null

        mIntroSegmentTypeArrayList?.clear()
        mIntroSegmentTypeArrayList = null
    }

    private var mIntroSegmentPlayer: ExoPlayer? = null
    private var mIntroSegmentTypeArrayList: ArrayList<IntroAudioPlayerSegment>? = null

    private var mGeneralSoundPool: SoundPool? = null
    private var mBackgroundSoundId: Int = 0
    private var mBackgroundStreamId: Int = 0

    private var mIsPlaying: Boolean = true
    private var mIsFirstCall: Boolean = true

    companion object {
        const val sMinPauseDurationInMilliSecs: Long = 500L
        const val sPauseInterval: Long = 1000L
    }
}