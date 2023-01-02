package com.liweiyap.narradir.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.liweiyap.narradir.util.INarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;

public abstract class NarradirFragmentBase extends Fragment
{
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (!(context instanceof INarradirControl))
        {
            throw new RuntimeException(
                "NarradirFragmentBase::onAttach(): " +
                    "Programming error: Context is not NarradirControl."
            );
        }

        mNarradirControl = (INarradirControl) context;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mNarradirControl = null;
    }

    protected INarradirControl getNarradirControl()
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

    protected void addSoundToPlayOnButtonClick(final IObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            INarradirControl narradirControl = getNarradirControl();
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

    private INarradirControl mNarradirControl;
}