package com.liweiyap.narradir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.RawRes;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayIntroductionActivity extends FullScreenPortraitActivity
{
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_introduction);

        Intent intent = getIntent();
        ArrayList<Integer> introSegmentArrayList = intent.getIntegerArrayListExtra("INTRO_SEGMENTS");
        if ( (introSegmentArrayList.size() != mExpectedIntroSegmentTotalWithPercival) &&
             !( (introSegmentArrayList.size() == mExpectedIntroSegmentTotalNoPercival) &&
                (introSegmentArrayList.get(mExpectedIntroSegmentTotalNoPercival-1) == R.raw.introsegment5nopercival) ) )
        {
            throw new RuntimeException(
                "PlayIntroductionActivity::onCreate(): " +
                    "Invalid input from intent");
        }

        mPauseDurationInMilliSecs = intent.getLongExtra("PAUSE_DURATION", mMinPauseDurationInMilliSecs);

        mCurrentDisplayedCharacterImageView = findViewById(R.id.currentDisplayedCharacterImageView);
        mCurrentDisplayedIntroSegmentTextView = findViewById(R.id.currentDisplayedIntroSegmentTextView);

        // https://stackoverflow.com/a/23856215/12367873
        final Iterator<Integer> iter = introSegmentArrayList.iterator();
        mIntroMediaPlayer = iter.hasNext() ?
            MediaPlayer.create(this, iter.next()) :
            null;
        if (mIntroMediaPlayer != null)
        {
            mIntroMediaPlayer.start();
            mCurrentDisplayedIntroSegmentTextView.setText(R.string.introsegment0_text);
            mIntroMediaPlayer.setOnCompletionListener(mediaPlayer -> {
                // https://medium.com/androiddevelopers/deep-dive-mediaplayer-best-practices-feb4d15a66f5
                mediaPlayer.reset();

                if (!iter.hasNext())
                {
                    finish();
                    return;
                }

                try
                {
                    // https://stackoverflow.com/a/20111291/12367873
                    mLastResId = iter.next();
                    AssetFileDescriptor afd = PlayIntroductionActivity.this.getResources().openRawResourceFd(mLastResId);
                    if (afd == null)
                    {
                        return;
                    }
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();

                    if (canPauseManuallyBeforeStarting(mLastResId))
                    {
                        if (mPauseDurationInMilliSecs != mMinPauseDurationInMilliSecs)
                        {
                            mCurrentDisplayedIntroSegmentTextView.setText("(PAUSE for " + mPauseDurationInMilliSecs/1000 + " seconds)");
                        }

                        mHandler.postDelayed(() -> switchCurrentDisplayedCharacterImage(mLastResId), mPauseDurationInMilliSecs);
                        mHandler.postDelayed(() -> switchCurrentDisplayedIntroSegmentTextView(mLastResId), mPauseDurationInMilliSecs);
                        mHandler.postDelayed(mediaPlayer::start, mPauseDurationInMilliSecs);
                    }
                    else
                    {
                        mHandler.postDelayed(() -> switchCurrentDisplayedCharacterImage(mLastResId), mMinPauseDurationInMilliSecs);
                        mHandler.postDelayed(() -> switchCurrentDisplayedIntroSegmentTextView(mLastResId), mMinPauseDurationInMilliSecs);
                        mHandler.postDelayed(mediaPlayer::start, mMinPauseDurationInMilliSecs);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
        }

        CustomTypefaceableObserverButton stopButton = findViewById(R.id.playIntroLayoutStopButton);
        stopButton.addOnClickObserver(this::finish);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mIntroMediaPlayer != null)
        {
            if (mWasPlaying)
            {
                mIntroMediaPlayer.seekTo(mIntroMediaPlayerCurrentLength);
                mIntroMediaPlayer.start();
            }
            else
            {
                if (canPauseManuallyBeforeStarting(mLastResId))
                {
                    mHandler.postDelayed(() -> switchCurrentDisplayedCharacterImage(mLastResId), mPauseDurationInMilliSecs);
                    mHandler.postDelayed(() -> switchCurrentDisplayedIntroSegmentTextView(mLastResId), mPauseDurationInMilliSecs);
                    mHandler.postDelayed(mIntroMediaPlayer::start, mPauseDurationInMilliSecs);
                }
                else
                {
                    mHandler.postDelayed(() -> switchCurrentDisplayedCharacterImage(mLastResId), mMinPauseDurationInMilliSecs);
                    mHandler.postDelayed(() -> switchCurrentDisplayedIntroSegmentTextView(mLastResId), mMinPauseDurationInMilliSecs);
                    mHandler.postDelayed(mIntroMediaPlayer::start, mMinPauseDurationInMilliSecs);
                }
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mIntroMediaPlayer != null)
        {
            if (mIntroMediaPlayer.isPlaying())
            {
                mIntroMediaPlayer.pause();
                mIntroMediaPlayerCurrentLength = mIntroMediaPlayer.getCurrentPosition();
                mWasPlaying = true;
            }
            else
            {
                mHandler.removeCallbacksAndMessages(null);
                mWasPlaying = false;
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mIntroMediaPlayer != null)
        {
            mIntroMediaPlayer.release();
            mIntroMediaPlayer = null;
        }

        mCurrentDisplayedCharacterImageView.setImageDrawable(null);
        mCurrentDisplayedIntroSegmentTextView.setText("");
    }

    @SuppressLint("NonConstantResourceId")
    private boolean canPauseManuallyBeforeStarting(@RawRes final int resId)
    {
        switch (resId)
        {
            case R.raw.introsegment2:
            case R.raw.introsegment4:
            case R.raw.introsegment6withpercivalnomorgana:
            case R.raw.introsegment6withpercivalwithmorgana:
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

    private MediaPlayer mIntroMediaPlayer;
    private int mIntroMediaPlayerCurrentLength;
    private int mLastResId;
    private final Handler mHandler = new Handler();
    private long mPauseDurationInMilliSecs = 5000;
    private final long mMinPauseDurationInMilliSecs = 500;
    private boolean mWasPlaying = false;
    private final int mExpectedIntroSegmentTotalNoPercival = 6;
    private final int mExpectedIntroSegmentTotalWithPercival = 8;
    private ImageView mCurrentDisplayedCharacterImageView;
    private CustomTypefaceableTextView mCurrentDisplayedIntroSegmentTextView;
}
