package com.liweiyap.narradir.util.audio

import android.content.Context
import android.media.SoundPool
import android.os.Handler

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer
import com.google.android.exoplayer2.extractor.Extractor
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.metadata.MetadataOutput
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.SilenceMediaSource
import com.google.android.exoplayer2.text.TextOutput
import com.google.android.exoplayer2.video.VideoRendererEventListener

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

        val loadedBackgroundSound = loadBackgroundSound(context, backgroundSoundName)
            ?: return

        mBackgroundSoundId = loadedBackgroundSound
        mGeneralSoundPool?.let {
            it.setOnLoadCompleteListener { soundPool: SoundPool, sampleId: Int, status: Int ->
                if ((sampleId == mBackgroundSoundId) && (status == 0)) {
                    mBackgroundStreamId = soundPool.play(sampleId, backgroundSoundVolume, backgroundSoundVolume, 1, -1, 1f)
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

    private fun prepareExoPlayer(context: Context, introSegmentArrayList: ArrayList<String>, pauseDurationInMilliSecs: Long, narrationVolume: Float) {
        val audioOnlyRenderersFactory = RenderersFactory { handler: Handler?, _: VideoRendererEventListener?, audioListener: AudioRendererEventListener?, _: TextOutput?, _: MetadataOutput? ->
            arrayOf<Renderer>(MediaCodecAudioRenderer(context, MediaCodecSelector.DEFAULT, handler, audioListener))
        }

        val mp3ExtractorFactory = ExtractorsFactory {
            arrayOf<Extractor>(Mp3Extractor())
        }

        mIntroSegmentPlayer = ExoPlayer.Builder(context, audioOnlyRenderersFactory, DefaultMediaSourceFactory(context, ExtractorsFactory.EMPTY)).build()
        for (idx: Int in introSegmentArrayList.indices) {
            val segment: String = introSegmentArrayList[idx]

            val mediaSource: ProgressiveMediaSource = ExoPlayerMediaSourceCreator.createProgressiveMediaSourceFromRes(context, segment, mp3ExtractorFactory)
                ?: continue

            mIntroSegmentPlayer!!.addMediaSource(mediaSource)

            if (idx == introSegmentArrayList.size - 1) {
                break
            }

            val silence = SilenceMediaSource(
                if (IntroSegmentDictionary.canPauseManuallyAtEnd(context = context, resName = segment))
                    pauseDurationInMilliSecs * 1000
                else
                    sMinPauseDurationInMilliSecs * 1000
            )
            mIntroSegmentPlayer!!.addMediaSource(silence)
        }

        if (mIntroSegmentPlayer!!.mediaItemCount != 2 * introSegmentArrayList.size - 1) {
            throw RuntimeException(
                "IntroAudioPlayer::prepareExoPlayer(): " +
                    "Invalid no of MediaSources for ExoPlayer; ${introSegmentArrayList.size} segments but ${mIntroSegmentPlayer!!.mediaItemCount} media sources.")
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

        mBackgroundStreamId = mGeneralSoundPool!!.play(mBackgroundSoundId, mBackgroundSoundVolume, mBackgroundSoundVolume, 1, -1, 1f)
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
    }

    private var mIntroSegmentPlayer: ExoPlayer? = null

    private var mGeneralSoundPool: SoundPool? = null
    private var mBackgroundSoundId: Int = 0
    private var mBackgroundStreamId: Int = 0

    private var mIsPlaying: Boolean = true
    private var mIsFirstCall: Boolean = true

    companion object {
        const val sMinPauseDurationInMilliSecs: Long = 500
    }
}