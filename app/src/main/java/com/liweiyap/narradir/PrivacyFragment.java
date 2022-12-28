package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.ui.HtmlHelper;
import com.liweiyap.narradir.ui.NarradirFragmentBase;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;

public class PrivacyFragment extends NarradirFragmentBase
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading0_subsubheading0_list0), getString(R.string.privacy_subheading0_subsubheading0_list0));
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading2_subsubheading0_list0), getString(R.string.privacy_subheading2_subsubheading0_list0));
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading3_subsubheading0_list0), getString(R.string.privacy_subheading3_subsubheading0_list0));
        HtmlHelper.setText(view.findViewById(R.id.pvcy_subheading5_para0), getString(R.string.privacy_subheading5_para0));

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