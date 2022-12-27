package com.liweiyap.narradir;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.liweiyap.narradir.ui.ControlFragment;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.SettingsLayout;
import com.liweiyap.narradir.ui.TextViewAutosizeHelper;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;
import com.liweiyap.narradir.util.TimeDisplay;

public class SettingsHomeFragment extends ControlFragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_settings_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        mNarrationSettingsLayout = view.findViewById(R.id.narrationSettingsLayout);
        mBackgroundSettingsLayout = view.findViewById(R.id.backgroundSettingsLayout);
        mRoleTimerSettingsLayout = view.findViewById(R.id.roleTimerSettingsLayout);
        CustomTypefaceableObserverButton privacyButton = view.findViewById(R.id.privacyButton);
        CustomTypefaceableObserverButton backButton = view.findViewById(R.id.settingsHomeLayoutBackButton);
        CustomTypefaceableObserverButton helpButton = view.findViewById(R.id.settingsHomeLayoutHelpButton);
        LinearLayout authorInfoLayout = view.findViewById(R.id.authorInfoLayout);

        // ----------------------------------------------------------------------
        // set key and value of each individual SettingsLayout
        // ----------------------------------------------------------------------

        mNarrationSettingsLayout.setKey(getString(R.string.settings_title_narration));
        mBackgroundSettingsLayout.setKey(getString(R.string.settings_title_background));
        mRoleTimerSettingsLayout.setKey(getString(R.string.settings_title_roletimer));

        // ----------------------------------------------------------------------
        // click sound
        // ----------------------------------------------------------------------

        addSoundToPlayOnButtonClick(mNarrationSettingsLayout.getEditButton());
        addSoundToPlayOnButtonClick(mBackgroundSettingsLayout.getEditButton());
        addSoundToPlayOnButtonClick(mRoleTimerSettingsLayout.getEditButton());
        addSoundToPlayOnButtonClick(privacyButton);
        addSoundToPlayOnButtonClick(backButton);
        addSoundToPlayOnButtonClick(helpButton);

        // ----------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // ----------------------------------------------------------------------

        backButton.addOnClickObserver(this::navigateUp);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                backButton.performClick();
            }
        });

        // ----------------------------------------------------------------------
        // navigation to external web browser
        // ----------------------------------------------------------------------

        authorInfoLayout.setOnClickListener(v -> navigateToAuthorWebsite());

        // -----------------------------------------------------------------------------------------
        // auto-sizing TextViews
        // -----------------------------------------------------------------------------------------

        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(view.findViewById(R.id.authorWebsiteTextView));
    }

    @Override
    public void onResume()
    {
        super.onResume();

        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        if (mNarrationSettingsLayout != null)
        {
            mNarrationSettingsLayout.setValue("Vol " + Math.round(viewModel.getNarrationVolume() * 10));
        }

        if (mBackgroundSettingsLayout != null)
        {
            mBackgroundSettingsLayout.setValue(viewModel.getBackgroundSoundName() + ", Vol " + Math.round(viewModel.getBackgroundSoundVolume() * 10));
        }

        if (mRoleTimerSettingsLayout != null)
        {
            mRoleTimerSettingsLayout.setValue(TimeDisplay.fromMilliseconds(viewModel.getPauseDurationInMilliSecs()));
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        mNarrationSettingsLayout = null;
        mBackgroundSettingsLayout = null;
        mRoleTimerSettingsLayout = null;
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

    private void navigateUp()
    {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }

    private void navigateToAuthorWebsite()
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://liweiyap.github.io"));
        startActivity(browserIntent);
    }

    private SettingsLayout mNarrationSettingsLayout;
    private SettingsLayout mBackgroundSettingsLayout;
    private SettingsLayout mRoleTimerSettingsLayout;
}