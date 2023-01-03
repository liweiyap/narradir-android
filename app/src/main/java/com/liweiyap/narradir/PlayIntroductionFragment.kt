package com.liweiyap.narradir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView

import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat

import com.google.android.exoplayer2.Player

import com.liweiyap.narradir.ui.NarradirFragmentBase
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView
import com.liweiyap.narradir.util.IObserver
import com.liweiyap.narradir.util.TimeDisplay
import com.liweiyap.narradir.util.audio.IntroAudioPlayer
import com.liweiyap.narradir.util.audio.IntroSegmentDictionary

class PlayIntroductionFragment: NarradirFragmentBase() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mIsDestroying = false
        return inflater.inflate(R.layout.fragment_play_introduction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mCurrentDisplayedCharacterImageView = view.findViewById(R.id.currentDisplayedCharacterImageView)
        mCurrentDisplayedIntroSegmentTextView = view.findViewById(R.id.currentDisplayedIntroSegmentTextView)

        // ----------------------------------------------------------------------
        // receive data
        // ----------------------------------------------------------------------

        var introSegmentArrayList: ArrayList<String>? = null
        var isStartedFromAvalon = true
        if (savedInstanceState == null) {
            val bundle: Bundle? = arguments
            if (bundle != null) {
                introSegmentArrayList = bundle.getStringArrayList(getString(R.string.intro_segments_key))
                isStartedFromAvalon = bundle.getBoolean(getString(R.string.is_started_from_avalon_key), true)
            }
        }
        else {
            introSegmentArrayList = savedInstanceState.getStringArrayList(getString(R.string.intro_segments_key))
            isStartedFromAvalon = savedInstanceState.getBoolean(getString(R.string.is_started_from_avalon_key), true)
        }

        if (introSegmentArrayList == null) {
            destroy()
        }

        view.findViewById<View>(R.id.gameTitleAvalonTextView).visibility = if (isStartedFromAvalon) View.VISIBLE else View.INVISIBLE
        view.findViewById<View>(R.id.gameTitleSecretHitlerTextView).visibility = if (isStartedFromAvalon) View.INVISIBLE else View.VISIBLE

        // ----------------------------------------------------------------------
        // initialise and prepare audio players
        // ----------------------------------------------------------------------

        if (viewModel == null) {
            destroy()
        }
        else {
            mAudioPlayer = IntroAudioPlayer(
                requireContext(),
                introSegmentArrayList!!, viewModel!!.pauseDurationInMilliSecs, viewModel!!.narrationVolume,
                viewModel!!.backgroundSoundName, viewModel!!.backgroundSoundVolume)

            mAudioPlayer!!.addExoPlayerListener(object: Player.Listener {
                override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: @Player.DiscontinuityReason Int) {
                    if (reason != Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
                        return
                    }

                    val newWindowIdx = mAudioPlayer!!.getExoPlayerCurrentMediaItemIndex()
                    if ((newWindowIdx and 1) == 0) {  // if even
                        switchCurrentDisplayedCharacterImage(resName = introSegmentArrayList[newWindowIdx / 2])
                        switchCurrentDisplayedIntroSegmentTextView(resName = introSegmentArrayList[newWindowIdx / 2])
                    }
                    else {  // if odd
                        if ( (IntroSegmentDictionary.canPauseManuallyAtEnd(context = requireContext(), resName = introSegmentArrayList[newWindowIdx / 2])) &&
                             (viewModel!!.pauseDurationInMilliSecs > IntroAudioPlayer.sMinPauseDurationInMilliSecs) ) {
                            mCurrentDisplayedIntroSegmentTextView?.text = TimeDisplay.longFormat(resources = resources, msec = viewModel!!.pauseDurationInMilliSecs / 1000)
                        }
                    }
                }

                override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        destroy()
                    }
                }
            })
        }

        // ----------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // ----------------------------------------------------------------------

        val pauseButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.playIntroLayoutPauseButton)
        pauseButton.addOnClickObserver(object: IObserver {
            override fun update() {
                narradirControl?.playClickSound()

                pauseButton.setText(
                    if (mAudioPlayer!!.isPlayingIntro())
                        R.string.pause_button_text_state_inactive
                    else
                        R.string.pause_button_text_state_active)

                mAudioPlayer?.toggle()
            }
        })

        val stopButton = view.findViewById<CustomTypefaceableObserverButton>(R.id.playIntroLayoutStopButton)
        stopButton.addOnClickObserver(object: IObserver { override fun update() { destroy() } })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                stopButton.performClick()
            }
        })

        // ----------------------------------------------------------------------
        // miscellaneous UI initialisation
        // ----------------------------------------------------------------------

        switchCurrentDisplayedIntroSegmentTextView(resName = introSegmentArrayList!![0])
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()

        mAudioPlayer?.let {
            if (it.isPlayingIntro()) {
                it.playIntro()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        mAudioPlayer?.let {
            if (it.isPlayingIntro()) {
                it.pauseIntro()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mAudioPlayer?.freeResources()
        mAudioPlayer = null

        mCurrentDisplayedCharacterImageView?.setImageDrawable(null)
        mCurrentDisplayedCharacterImageView = null

        mCurrentDisplayedIntroSegmentTextView?.text = ""
        mCurrentDisplayedIntroSegmentTextView = null

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @Synchronized
    private fun setDestroying(): Boolean {
        val wasDestroying: Boolean = mIsDestroying
        mIsDestroying = true
        return wasDestroying
    }

    @Synchronized
    private fun destroy() {
        if (setDestroying()) {
            return
        }

        navigateUp(steps = 1)
    }

    private fun switchCurrentDisplayedCharacterImage(resName: String) {
        mCurrentDisplayedCharacterImageView?.let {
            // does not look elegant but it's safe because resource ID integers are non-constant from Gradle version >4.
            when (resName) {
                getString(R.string.avalonintrosegment1nooberon_key),
                getString(R.string.avalonintrosegment1withoberon_key) -> it.setImageResource(R.drawable.teamevil)
                getString(R.string.avalonintrosegment3nomordred_key),
                getString(R.string.avalonintrosegment3withmordred_key) -> it.setImageResource(R.drawable.merlin_unchecked_unlabelled)
                getString(R.string.avalonintrosegment5withpercivalnomorgana_key),
                getString(R.string.avalonintrosegment5withpercivalwithmorgana_key) -> it.setImageResource(R.drawable.percival_unchecked_unlabelled)
                getString(R.string.secrethitlerintrosegment1small_key) -> {
                    it.setImageResource(R.drawable.ic_teamfascists)
                    it.setBackgroundResource(R.drawable.fascist_background)
                }
                getString(R.string.secrethitlerintrosegment1large_key) -> {
                    it.setImageResource(R.drawable.ic_fascist)
                    it.setBackgroundResource(R.drawable.fascist_background)
                }
                getString(R.string.avalonintrosegment3nomerlin_key),
                getString(R.string.avalonintrosegment5nopercival_key),
                getString(R.string.avalonintrosegment7_key),
                getString(R.string.secrethitlerintrosegment3small_key),
                getString(R.string.secrethitlerintrosegment4large_key) -> {
                    it.setImageDrawable(null)
                    it.setBackgroundResource(ResourcesCompat.ID_NULL)
                }
            }
        }
    }

    private fun switchCurrentDisplayedIntroSegmentTextView(resName: String) {
        if (mCurrentDisplayedIntroSegmentTextView == null) {
            return
        }

        val subtitle: String = IntroSegmentDictionary.getSubtitleFromIntroSegmentRes(requireContext(), resName)
            ?: throw RuntimeException(
                "PlayIntroductionFragment::switchCurrentDisplayedIntroSegmentTextView(): " +
                    "Invalid introduction segment resource name $resName")

        mCurrentDisplayedIntroSegmentTextView!!.text = subtitle
    }

    private var mAudioPlayer: IntroAudioPlayer? = null

    private var mCurrentDisplayedCharacterImageView: ImageView? = null
    private var mCurrentDisplayedIntroSegmentTextView: CustomTypefaceableTextView? = null

    private var mIsDestroying: Boolean = false
}