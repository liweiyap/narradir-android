package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.ui.NarradirFragmentBase;
import com.liweiyap.narradir.ui.ObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.NarradirViewModel;

public class SettingsNarrationFragment extends NarradirFragmentBase
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_settings_narration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mVolumeControlLayoutValueTextView = view.findViewById(R.id.updownControlLayoutValue);
        ObserverButton volumeIncreaseButton = view.findViewById(R.id.upControlButton);
        ObserverButton volumeDecreaseButton = view.findViewById(R.id.downControlButton);
        CustomTypefaceableObserverButton generalBackButton = view.findViewById(R.id.generalBackButton);
        CustomTypefaceableObserverButton mainButton = view.findViewById(R.id.mainButton);
        CustomTypefaceableTextView settingsTitle = view.findViewById(R.id.settingsTitleTextView);
        CustomTypefaceableTextView timeControlLayoutLabelTextView = view.findViewById(R.id.updownControlLayoutLabel);

        settingsTitle.setText(R.string.settings_title_narration);
        timeControlLayoutLabelTextView.setText(R.string.volume_control_layout_label);

        // ----------------------------------------------------------------------
        // volume control
        // ----------------------------------------------------------------------

        displayVolume();

        volumeIncreaseButton.addOnClickObserver(this::increaseVolume);
        volumeDecreaseButton.addOnClickObserver(this::decreaseVolume);

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(generalBackButton);
        addSoundToPlayOnButtonClick(mainButton);
        addSoundToPlayOnButtonClick(volumeIncreaseButton);
        addSoundToPlayOnButtonClick(volumeDecreaseButton);

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

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        mVolumeControlLayoutValueTextView = null;
    }

    private void displayVolume()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        if (mVolumeControlLayoutValueTextView == null)
        {
            return;
        }

        mVolumeControlLayoutValueTextView.setText(String.valueOf(Math.round(viewModel.getNarrationVolume() * 10)));
    }

    private void increaseVolume()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setNarrationVolume(Math.min(1f, viewModel.getNarrationVolume() + 0.1f));
        displayVolume();
    }

    private void decreaseVolume()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setNarrationVolume(Math.max(0f, viewModel.getNarrationVolume() - 0.1f));
        displayVolume();
    }

    private CustomTypefaceableTextView mVolumeControlLayoutValueTextView;
}