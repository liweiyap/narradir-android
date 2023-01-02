package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.liweiyap.narradir.ui.NarradirFragmentBase;
import com.liweiyap.narradir.ui.ObserverButton;
import com.liweiyap.narradir.ui.IObserverListener;
import com.liweiyap.narradir.ui.SnackbarWrapper;
import com.liweiyap.narradir.ui.ViewGroupSingleTargetSelector;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView;
import com.liweiyap.narradir.util.INarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;
import com.liweiyap.narradir.util.SnackbarBuilderFlag;
import com.liweiyap.narradir.util.audio.BackgroundSoundTestMediaPlayer;

import java.util.EnumSet;

public class SettingsBackgroundFragment extends NarradirFragmentBase
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_settings_background, container, false);
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
        CustomTypefaceableTextView volumeControlLayoutLabelTextView = view.findViewById(R.id.updownControlLayoutLabel);
        LinearLayout backgroundSoundSelectionLayout = view.findViewById(R.id.backgroundSoundSelectionLayout);
        CustomTypefaceableCheckableObserverButton backgroundSoundNoneButton = view.findViewById(R.id.backgroundSoundNoneButton);
        CustomTypefaceableCheckableObserverButton backgroundSoundCardsButton = view.findViewById(R.id.backgroundSoundCardsButton);
        CustomTypefaceableCheckableObserverButton backgroundSoundCricketsButton = view.findViewById(R.id.backgroundSoundCricketsButton);
        CustomTypefaceableCheckableObserverButton backgroundSoundFireplaceButton = view.findViewById(R.id.backgroundSoundFireplaceButton);
        CustomTypefaceableCheckableObserverButton backgroundSoundRainButton = view.findViewById(R.id.backgroundSoundRainButton);
        CustomTypefaceableCheckableObserverButton backgroundSoundRainforestButton = view.findViewById(R.id.backgroundSoundRainforestButton);
        CustomTypefaceableCheckableObserverButton backgroundSoundRainstormButton = view.findViewById(R.id.backgroundSoundRainstormButton);
        CustomTypefaceableCheckableObserverButton backgroundSoundWolvesButton = view.findViewById(R.id.backgroundSoundWolvesButton);
        mNavBar = view.findViewById(R.id.nonStartingActivityLayoutNavBar);

        settingsTitle.setText(R.string.settings_title_background);
        volumeControlLayoutLabelTextView.setText(R.string.volume_control_layout_label);

        ViewGroupSingleTargetSelector.addSingleTargetSelection(backgroundSoundSelectionLayout);

        mSnackbar = new SnackbarWrapper(requireContext());
        mBackgroundSoundTestMediaPlayer = new BackgroundSoundTestMediaPlayer(requireContext());

        // ----------------------------------------------------------------------
        // init background sounds and background sound setters
        // ----------------------------------------------------------------------

        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            backgroundSoundNoneButton.performClick();
        }
        else
        {
            @NonNull String backgroundSoundName = viewModel.getBackgroundSoundName();

            // does not look elegant but it's safe because resource ID integers are non-constant from Gradle version >4.
            if (backgroundSoundName.equals(getString(R.string.backgroundsound_cards)))
            {
                backgroundSoundCardsButton.performClick();
            }
            else if (backgroundSoundName.equals(getString(R.string.backgroundsound_crickets)))
            {
                backgroundSoundCricketsButton.performClick();
            }
            else if (backgroundSoundName.equals(getString(R.string.backgroundsound_fireplace)))
            {
                backgroundSoundFireplaceButton.performClick();
            }
            else if (backgroundSoundName.equals(getString(R.string.backgroundsound_rain)))
            {
                backgroundSoundRainButton.performClick();
            }
            else if (backgroundSoundName.equals(getString(R.string.backgroundsound_rainforest)))
            {
                backgroundSoundRainforestButton.performClick();
            }
            else if (backgroundSoundName.equals(getString(R.string.backgroundsound_rainstorm)))
            {
                backgroundSoundRainstormButton.performClick();
            }
            else if (backgroundSoundName.equals(getString(R.string.backgroundsound_wolves)))
            {
                backgroundSoundWolvesButton.performClick();
            }
            else
            {
                backgroundSoundNoneButton.performClick();
            }
        }

        backgroundSoundNoneButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_none)));
        backgroundSoundCardsButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_cards)));
        backgroundSoundCricketsButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_crickets)));
        backgroundSoundFireplaceButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_fireplace)));
        backgroundSoundRainButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_rain)));
        backgroundSoundRainforestButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_rainforest)));
        backgroundSoundRainstormButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_rainstorm)));
        backgroundSoundWolvesButton.addOnClickObserver(() -> selectBackgroundSound(getString(R.string.backgroundsound_wolves)));

        backgroundSoundCardsButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_cards)));
        backgroundSoundCricketsButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_crickets)));
        backgroundSoundFireplaceButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_fireplace)));
        backgroundSoundRainButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_rain)));
        backgroundSoundRainforestButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_rainforest)));
        backgroundSoundRainstormButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_rainstorm)));
        backgroundSoundWolvesButton.addOnLongClickObserver(() -> playBackgroundSound(getString(R.string.backgroundsound_wolves)));
        backgroundSoundNoneButton.addOnLongClickObserver(this::stopBackgroundSound);

        // ----------------------------------------------------------------------
        // volume control
        // ----------------------------------------------------------------------

        displayVolume();

        volumeIncreaseButton.addOnClickObserver(this::increaseVolume);
        volumeDecreaseButton.addOnClickObserver(this::decreaseVolume);

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        for (int childIdx = 0; childIdx < backgroundSoundSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) backgroundSoundSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn, false);
        }

        addSoundToPlayOnButtonClick(generalBackButton, false);
        addSoundToPlayOnButtonClick(mainButton, false);
        addSoundToPlayOnButtonClick(volumeIncreaseButton, true);
        addSoundToPlayOnButtonClick(volumeDecreaseButton, true);

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
    public void onResume()
    {
        super.onResume();

        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }
        mBackgroundSoundTestMediaPlayer.resume();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }
        mBackgroundSoundTestMediaPlayer.pause();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (mSnackbar != null)
        {
            mSnackbar.destroy();
            mSnackbar = null;
        }

        if (mBackgroundSoundTestMediaPlayer != null)
        {
            mBackgroundSoundTestMediaPlayer.destroy();
            mBackgroundSoundTestMediaPlayer = null;
        }

        mVolumeControlLayoutValueTextView = null;
        mNavBar = null;
    }

    private void addSoundToPlayOnButtonClick(final IObserverListener btn, final boolean isVolumeControl)
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            if (mBackgroundSoundTestMediaPlayer != null)
            {
                if (isVolumeControl)
                {
                    mBackgroundSoundTestMediaPlayer.setVolume(viewModel.getBackgroundSoundVolume());
                }
                else
                {
                    stopBackgroundSound();
                }
            }

            INarradirControl narradirControl = getNarradirControl();
            if (narradirControl != null)
            {
                narradirControl.playClickSound();
            }
        });
    }

    private void selectBackgroundSound(final @NonNull String sound)
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setBackgroundSoundName(sound);
    }

    private void playBackgroundSound(final @NonNull String sound)
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }

        mBackgroundSoundTestMediaPlayer.play(sound, viewModel.getBackgroundSoundVolume(), null);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showSnackbar();
    }

    private void stopBackgroundSound()
    {
        if (mBackgroundSoundTestMediaPlayer == null)
        {
            return;
        }

        mBackgroundSoundTestMediaPlayer.stop();
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dismissSnackbar();
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

        mVolumeControlLayoutValueTextView.setText(String.valueOf(Math.round(viewModel.getBackgroundSoundVolume() * 10)));
    }

    private void increaseVolume()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setBackgroundSoundVolume(Math.min(1f, viewModel.getBackgroundSoundVolume() + 0.1f));
        displayVolume();
    }

    private void decreaseVolume()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setBackgroundSoundVolume(Math.max(0f, viewModel.getBackgroundSoundVolume() - 0.1f));
        displayVolume();
    }

    private void showSnackbar()
    {
        NarradirViewModel viewModel = getViewModel();
        if ( (viewModel == null) || (viewModel.doHideBackgroundSoundHint()) )
        {
            return;
        }

        try
        {
            mSnackbar.show(
                mNavBar,
                getString(R.string.backgroundsound_mute_notification),
                BaseTransientBottomBar.LENGTH_SHORT,
                getString(R.string.acknowledge_button_text),
                () -> viewModel.setHideBackgroundSoundHint(true),
                EnumSet.of(SnackbarBuilderFlag.SHOW_ABOVE_XY, SnackbarBuilderFlag.ACTION_DISMISSABLE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void dismissSnackbar()
    {
        try
        {
            mSnackbar.dismissOldSnackbar();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private SnackbarWrapper mSnackbar;
    private BackgroundSoundTestMediaPlayer mBackgroundSoundTestMediaPlayer;

    private CustomTypefaceableTextView mVolumeControlLayoutValueTextView;
    private LinearLayout mNavBar;
}