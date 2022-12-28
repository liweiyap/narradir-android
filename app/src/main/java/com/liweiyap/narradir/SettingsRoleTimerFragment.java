package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.liweiyap.narradir.ui.ControlFragment;
import com.liweiyap.narradir.ui.ObserverButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;

public class SettingsRoleTimerFragment extends ControlFragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_settings_roletimer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mPauseControlLayoutValueTextView = view.findViewById(R.id.updownControlLayoutValue);
        ObserverButton pauseIncreaseButton = view.findViewById(R.id.upControlButton);
        ObserverButton pauseDecreaseButton = view.findViewById(R.id.downControlButton);
        CustomTypefaceableObserverButton generalBackButton = view.findViewById(R.id.generalBackButton);
        CustomTypefaceableObserverButton mainButton = view.findViewById(R.id.mainButton);
        CustomTypefaceableTextView settingsTitle = view.findViewById(R.id.settingsTitleTextView);
        CustomTypefaceableTextView timeControlLayoutLabelTextView = view.findViewById(R.id.updownControlLayoutLabel);

        settingsTitle.setText(R.string.settings_title_roletimer);
        timeControlLayoutLabelTextView.setText(R.string.time_control_layout_label);

        // ----------------------------------------------------------------------
        // pause duration control
        // ----------------------------------------------------------------------

        displayPauseDuration();

        pauseIncreaseButton.addOnClickObserver(this::increasePauseDuration);
        pauseDecreaseButton.addOnClickObserver(this::decreasePauseDuration);

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(generalBackButton);
        addSoundToPlayOnButtonClick(mainButton);
        addSoundToPlayOnButtonClick(pauseIncreaseButton);
        addSoundToPlayOnButtonClick(pauseDecreaseButton);

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

        mPauseControlLayoutValueTextView = null;
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener btn)
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

    private void displayPauseDuration()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        if (mPauseControlLayoutValueTextView == null)
        {
            return;
        }

        mPauseControlLayoutValueTextView.setText(String.valueOf(viewModel.getPauseDurationInMilliSecs()/1000));
    }

    private void increasePauseDuration()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setPauseDurationInMilliSecs(Math.min(mMaxPauseDurationInMilliSecs, viewModel.getPauseDurationInMilliSecs() + 1000));
        displayPauseDuration();
    }

    private void decreasePauseDuration()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setPauseDurationInMilliSecs(Math.max(mMinPauseDurationInMilliSecs, viewModel.getPauseDurationInMilliSecs() - 1000));
        displayPauseDuration();
    }

    private void navigateUp(final int steps)
    {
        NavController navController = NavHostFragment.findNavController(this);
        for (int step = 0; step < steps; ++step)
        {
            navController.navigateUp();
        }
    }

    private final long mMaxPauseDurationInMilliSecs = 10000;
    private final long mMinPauseDurationInMilliSecs = 0;

    private CustomTypefaceableTextView mPauseControlLayoutValueTextView;
}