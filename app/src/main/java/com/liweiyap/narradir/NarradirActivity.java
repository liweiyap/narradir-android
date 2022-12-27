package com.liweiyap.narradir;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.liweiyap.narradir.ui.FullScreenActivity;
import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;
import com.liweiyap.narradir.util.audio.ClickSoundGenerator;

public class NarradirActivity extends FullScreenActivity implements NarradirControl
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_narradir);

        mViewModel = new NarradirViewModel(this, getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE));

        mClickSoundGenerator = new ClickSoundGenerator(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mClickSoundGenerator != null)
        {
            mClickSoundGenerator.freeResources();
        }
    }

    @Override
    public NarradirViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void playClickSound()
    {
        if (mClickSoundGenerator != null)
        {
            mClickSoundGenerator.playClickSound();
        }
    }

    @Override
    public void navigateAwayFromApp() {
        // https://developer.android.com/guide/components/activities/tasks-and-back-stack#back-press-behavior
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R)
        {
            finish();
        }
        else
        {
            moveTaskToBack(true);
        }
    }

    private ClickSoundGenerator mClickSoundGenerator;
    private NarradirViewModel mViewModel;
}