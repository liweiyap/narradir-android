package com.liweiyap.narradir.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;

public abstract class NarradirFragmentBase extends Fragment
{
    private NarradirControl mNarradirControl;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (!(context instanceof NarradirControl))
        {
            throw new RuntimeException(
                "ControlFragment::onAttach(): " +
                    "Programming error: Context is not NarradirControl."
            );
        }

        mNarradirControl = (NarradirControl) context;
    }

    protected NarradirControl getNarradirControl()
    {
        return mNarradirControl;
    }

    @Nullable
    protected NarradirViewModel getViewModel()
    {
        if (mNarradirControl == null)
        {
            return null;
        }

        return mNarradirControl.getViewModel();
    }

    protected void addSoundToPlayOnButtonClick(final ObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            NarradirControl narradirControl = getNarradirControl();
            if (narradirControl != null)
            {
                narradirControl.playClickSound();
            }
        });
    }

    protected void navigateUp(final int steps)
    {
        NavController navController = NavHostFragment.findNavController(this);
        for (int step = 0; step < steps; ++step)
        {
            navController.navigateUp();
        }
    }
}