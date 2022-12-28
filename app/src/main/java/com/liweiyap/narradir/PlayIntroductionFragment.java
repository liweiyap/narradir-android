package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.exoplayer2.Player;
import com.liweiyap.narradir.ui.NarradirFragmentBase;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;
import com.liweiyap.narradir.util.audio.IntroAudioPlayer;
import com.liweiyap.narradir.util.audio.IntroSegmentDictionary;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class PlayIntroductionFragment extends NarradirFragmentBase
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mIsDestroying = false;
        return inflater.inflate(R.layout.fragment_play_introduction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mCurrentDisplayedCharacterImageView = view.findViewById(R.id.currentDisplayedCharacterImageView);
        mCurrentDisplayedIntroSegmentTextView = view.findViewById(R.id.currentDisplayedIntroSegmentTextView);

        // ----------------------------------------------------------------------
        // receive data
        // ----------------------------------------------------------------------

        ArrayList<String> tmp = null;
        boolean isStartedFromAvalon = true;
        if (savedInstanceState == null)
        {
            final Bundle bundle = getArguments();
            if (bundle != null)
            {
                tmp = bundle.getStringArrayList(getString(R.string.intro_segments_key));
                isStartedFromAvalon = bundle.getBoolean(getString(R.string.is_started_from_avalon_key), true);
            }
        }
        else
        {
            tmp = savedInstanceState.getStringArrayList(getString(R.string.intro_segments_key));
            isStartedFromAvalon = savedInstanceState.getBoolean(getString(R.string.is_started_from_avalon_key), true);
        }

        if (tmp == null)
        {
            destroy();
        }

        @NonNull final ArrayList<String> introSegmentArrayList = Objects.requireNonNull(tmp);

        view.findViewById(R.id.gameTitleAvalonTextView).setVisibility(isStartedFromAvalon ? View.VISIBLE : View.INVISIBLE);
        view.findViewById(R.id.gameTitleSecretHitlerTextView).setVisibility(isStartedFromAvalon ? View.INVISIBLE : View.VISIBLE);

        // ----------------------------------------------------------------------
        // initialise and prepare audio players
        // ----------------------------------------------------------------------

        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            destroy();
        }
        else
        {
            mAudioPlayer = new IntroAudioPlayer(
                requireContext(),
                introSegmentArrayList, viewModel.getPauseDurationInMilliSecs(), viewModel.getNarrationVolume(),
                viewModel.getBackgroundSoundName(), viewModel.getBackgroundSoundVolume());

            mAudioPlayer.addExoPlayerListener(new Player.Listener()
            {
                @Override
                public void onPositionDiscontinuity(Player.@NotNull PositionInfo oldPosition, Player.@NotNull PositionInfo newPosition, @Player.DiscontinuityReason int reason)
                {
                    if (reason != Player.DISCONTINUITY_REASON_AUTO_TRANSITION)
                    {
                        return;
                    }

                    int newWindowIdx = mAudioPlayer.getExoPlayerCurrentMediaItemIndex();
                    if ((newWindowIdx & 1) == 0)  // if even
                    {
                        switchCurrentDisplayedCharacterImage(introSegmentArrayList.get(newWindowIdx/2));
                        switchCurrentDisplayedIntroSegmentTextView(introSegmentArrayList.get(newWindowIdx/2));
                    }
                    else  // if odd
                    {
                        if ( (IntroSegmentDictionary.canPauseManuallyAtEnd(requireContext(), introSegmentArrayList.get(newWindowIdx/2))) &&
                             (viewModel.getPauseDurationInMilliSecs() > IntroAudioPlayer.sMinPauseDurationInMilliSecs) )
                        {
                            // this long to int conversion is safe for the current (values/1000) that we have but this may change in future
                            mCurrentDisplayedIntroSegmentTextView.setText(getResources().getQuantityString(R.plurals.pauseduration_text, (int) (viewModel.getPauseDurationInMilliSecs()/1000), viewModel.getPauseDurationInMilliSecs()/1000));
                        }
                    }
                }

                @Override
                public void onPlaybackStateChanged(@Player.State int playbackState)
                {
                    if (playbackState == Player.STATE_ENDED)
                    {
                        destroy();
                    }
                }
            });
        }

        // ----------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // ----------------------------------------------------------------------

        CustomTypefaceableObserverButton pauseButton = view.findViewById(R.id.playIntroLayoutPauseButton);
        pauseButton.addOnClickObserver(() -> {
            NarradirControl narradirControl = getNarradirControl();
            if (narradirControl != null)
            {
                narradirControl.playClickSound();
            }

            pauseButton.setText(
                mAudioPlayer.isPlayingIntro() ?
                    R.string.pause_button_text_state_inactive :
                    R.string.pause_button_text_state_active);

            mAudioPlayer.toggle();
        });

        CustomTypefaceableObserverButton stopButton = view.findViewById(R.id.playIntroLayoutStopButton);
        stopButton.addOnClickObserver(this::destroy);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
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
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mAudioPlayer == null)
        {
            return;
        }

        if (mAudioPlayer.isPlayingIntro())
        {
            mAudioPlayer.playIntro();
        }
    }

    @Override
    public void onPause()
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
    public void onDestroyView()
    {
        super.onDestroyView();

        if (mAudioPlayer != null)
        {
            mAudioPlayer.freeResources();
            mAudioPlayer = null;
        }

        if (mCurrentDisplayedCharacterImageView != null)
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView = null;
        }

        if (mCurrentDisplayedIntroSegmentTextView != null)
        {
            mCurrentDisplayedIntroSegmentTextView.setText("");
            mCurrentDisplayedIntroSegmentTextView = null;
        }

        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private synchronized boolean setDestroying()
    {
        boolean wasDestroying = mIsDestroying;
        mIsDestroying = true;
        return wasDestroying;
    }

    private synchronized void destroy()
    {
        if (setDestroying())
        {
            return;
        }

        navigateUp(1);
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
            mCurrentDisplayedCharacterImageView.setBackgroundResource(ResourcesCompat.ID_NULL);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment5nopercival_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(ResourcesCompat.ID_NULL);
        }
        else if (resName.equals(getString(R.string.avalonintrosegment7_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(ResourcesCompat.ID_NULL);
        }
        else if (resName.equals(getString(R.string.secrethitlerintrosegment3small_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(ResourcesCompat.ID_NULL);
        }
        else if (resName.equals(getString(R.string.secrethitlerintrosegment4large_key)))
        {
            mCurrentDisplayedCharacterImageView.setImageDrawable(null);
            mCurrentDisplayedCharacterImageView.setBackgroundResource(ResourcesCompat.ID_NULL);
        }
    }

    private void switchCurrentDisplayedIntroSegmentTextView(final @NonNull String resName)
    {
        if (mCurrentDisplayedIntroSegmentTextView == null)
        {
            return;
        }

        final String subtitle = IntroSegmentDictionary.getSubtitleFromIntroSegmentRes(requireContext(), resName);

        if (subtitle == null)
        {
            throw new RuntimeException(
                "PlayIntroductionFragment::switchCurrentDisplayedIntroSegmentTextView(): " +
                    "Invalid introduction segment resource name " + resName);
        }

        mCurrentDisplayedIntroSegmentTextView.setText(subtitle);
    }

    private IntroAudioPlayer mAudioPlayer;

    private ImageView mCurrentDisplayedCharacterImageView;
    private CustomTypefaceableTextView mCurrentDisplayedIntroSegmentTextView;

    private boolean mIsDestroying;
}