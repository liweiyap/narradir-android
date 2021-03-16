package com.liweiyap.narradir;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayIntroductionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_introduction);

        Intent intent = getIntent();
        ArrayList<Integer> introSegmentArrayList = intent.getIntegerArrayListExtra("INTRO_SEGMENTS");
        if ( (introSegmentArrayList.size() != mExpectedIntroSegmentTotalWithPercival) &&
             !((introSegmentArrayList.size() == mExpectedIntroSegmentTotalNoPercival) && (introSegmentArrayList.get(5) == R.raw.introsegment5nopercival)) )
        {
            throw new RuntimeException(
                "PlayIntroductionActivity::onCreate(): " +
                    "Invalid input from intent");
        }

        mPauseDurationInMilliSecs = intent.getLongExtra("PAUSE_DURATION", 5000);

        // https://stackoverflow.com/a/23856215/12367873
        final Iterator<Integer> iter = introSegmentArrayList.iterator();
        mIntroMediaPlayer = iter.hasNext() ?
            MediaPlayer.create(this, iter.next()) :
            null;
        if (mIntroMediaPlayer != null)
        {
            mIntroMediaPlayer.start();
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
                    AssetFileDescriptor afd = PlayIntroductionActivity.this.getResources().openRawResourceFd(iter.next());
                    if (afd == null)
                    {
                        return;
                    }
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();

                    mHandler.postDelayed(mediaPlayer::start, mPauseDurationInMilliSecs);
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
                mHandler.postDelayed(mIntroMediaPlayer::start, mPauseDurationInMilliSecs);
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
    }

    private MediaPlayer mIntroMediaPlayer;
    private int mIntroMediaPlayerCurrentLength;
    private final Handler mHandler = new Handler();
    private long mPauseDurationInMilliSecs = 5000;
    private boolean mWasPlaying = false;
    private final int mExpectedIntroSegmentTotalNoPercival = 6;
    private final int mExpectedIntroSegmentTotalWithPercival = 8;
}
