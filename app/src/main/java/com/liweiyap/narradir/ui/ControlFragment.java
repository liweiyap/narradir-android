package com.liweiyap.narradir.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;

public class ControlFragment extends Fragment
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
}
