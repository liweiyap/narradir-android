package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.ui.NarradirFragmentBase;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;

public class HelpFragment extends NarradirFragmentBase
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        CustomTypefaceableObserverButton generalBackButton = view.findViewById(R.id.generalBackButton);
        CustomTypefaceableObserverButton mainButton = view.findViewById(R.id.mainButton);

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(generalBackButton);
        addSoundToPlayOnButtonClick(mainButton);

        // ----------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // ----------------------------------------------------------------------

        generalBackButton.addOnClickObserver(() -> navigateUp(1));
        mainButton.addOnClickObserver(() -> navigateUp(2));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                generalBackButton.performClick();
            }
        });
    }
}