package com.liweiyap.narradir;

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

import com.liweiyap.narradir.secrethitler.SecretHitlerControlGroup;
import com.liweiyap.narradir.ui.NarradirFragmentBase;
import com.liweiyap.narradir.ui.ObserverImageButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.TextViewAutosizeHelper;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;

import java.util.ArrayList;

public class SecretHitlerCharacterSelectionFragment extends NarradirFragmentBase
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_character_selection_secrethitler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        LinearLayout playerNumberSelectionLayout = view.findViewById(R.id.playerNumberSelectionLayout);
        CustomTypefaceableObserverButton gameSwitcherButton = view.findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        CustomTypefaceableObserverButton playButton = view.findViewById(R.id.characterSelectionLayoutPlayButton);
        ObserverImageButton settingsButton = view.findViewById(R.id.characterSelectionLayoutSettingsButton);

        // -----------------------------------------------------------------------------------------
        // character image button array, player number selection layout, character selection layouts
        // -----------------------------------------------------------------------------------------

        mSecretHitlerControlGroup = new SecretHitlerControlGroup(
            requireActivity(),
            playerNumberSelectionLayout,
            view.findViewById(R.id.p5Button), view.findViewById(R.id.p6Button), view.findViewById(R.id.p7Button),
            view.findViewById(R.id.p8Button), view.findViewById(R.id.p9Button), view.findViewById(R.id.p10Button),
            view.findViewById(R.id.liberal0Button), view.findViewById(R.id.liberal1Button), view.findViewById(R.id.liberal2Button), view.findViewById(R.id.liberal3Button),
            view.findViewById(R.id.liberal4Button), view.findViewById(R.id.liberal5Button),
            view.findViewById(R.id.hitlerButton), view.findViewById(R.id.fascist0Button), view.findViewById(R.id.fascist1Button), view.findViewById(R.id.fascist2Button));

        applyPreferences();

        // -----------------------------------------------------------------------------------------
        // sounds
        // -----------------------------------------------------------------------------------------

        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        addSoundToPlayOnButtonClick(gameSwitcherButton);
        addSoundToPlayOnButtonClick(playButton);
        addSoundToPlayOnButtonClick(settingsButton);

        // -----------------------------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // -----------------------------------------------------------------------------------------

        gameSwitcherButton.setText(getString(R.string.game_switcher_button_avalon));
        gameSwitcherButton.addOnClickObserver(this::navigateToAvalonCharacterSelectionFragment);

        playButton.addOnClickObserver(this::navigateToPlayIntroductionFragment);
        settingsButton.addOnClickObserver(this::navigateToSettingsHomeFragment);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                NarradirControl narradirControl = getNarradirControl();
                if (narradirControl != null)
                {
                    narradirControl.navigateAwayFromApp();
                }
            }
        });

        // -----------------------------------------------------------------------------------------
        // auto-sizing TextViews
        // -----------------------------------------------------------------------------------------

        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(gameSwitcherButton);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mSecretHitlerControlGroup == null)
        {
            return;
        }
        mSecretHitlerControlGroup.resumeCharacterDescriptionMediaPlayer();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        savePreferences();  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873

        if (mSecretHitlerControlGroup == null)
        {
            return;
        }
        mSecretHitlerControlGroup.pauseCharacterDescriptionMediaPlayer();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (mSecretHitlerControlGroup != null)
        {
            mSecretHitlerControlGroup.destroy();
            mSecretHitlerControlGroup = null;
        }
    }

    @Override
    protected void addSoundToPlayOnButtonClick(final ObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            if (mSecretHitlerControlGroup != null)
            {
                mSecretHitlerControlGroup.stopCharacterDescriptionMediaPlayer();
            }

            NarradirControl narradirControl = getNarradirControl();
            if (narradirControl != null)
            {
                narradirControl.playClickSound();
            }
        });
    }

    private void navigateToAvalonCharacterSelectionFragment()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel != null)
        {
            viewModel.saveAvalonLastSelected();
        }

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.avalonCharacterSelectionFragment);
    }

    private void navigateToPlayIntroductionFragment()
    {
        if (mSecretHitlerControlGroup == null)
        {
            throw new RuntimeException("SecretHitlerCharacterSelectionFragment::navigateToPlayIntroductionFragment(): mSecretHitlerControlGroup is NULL");
        }

        ArrayList<String> introSegmentArrayList = new ArrayList<>();

        if (mSecretHitlerControlGroup.getExpectedGoodTotal() + mSecretHitlerControlGroup.getExpectedEvilTotal() < 7)
        {
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment0small_key));
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment1small_key));
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment2small_key));
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment3small_key));
        }
        else
        {
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment0large_key));
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment1large_key));
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment2large_key));
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment3large_key));
            introSegmentArrayList.add(getString(R.string.secrethitlerintrosegment4large_key));
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(getString(R.string.intro_segments_key), introSegmentArrayList);
        bundle.putBoolean(getString(R.string.is_started_from_avalon_key), false);

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.playIntroductionFragment, bundle);
    }

    private void navigateToSettingsHomeFragment()
    {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.settingsHomeFragment);
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private void savePreferences()
    {
        if (mSecretHitlerControlGroup == null)
        {
            throw new RuntimeException("SecretHitlerCharacterSelectionFragment::savePreferences(): mSecretHitlerControlGroup is NULL");
        }

        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setSecretHitlerExpectedGoodTotal(mSecretHitlerControlGroup.getExpectedGoodTotal());
        viewModel.setSecretHitlerExpectedEvilTotal(mSecretHitlerControlGroup.getExpectedEvilTotal());
        viewModel.savePreferences();
    }

    private void applyPreferences()
    {
        if (mSecretHitlerControlGroup == null)
        {
            throw new RuntimeException("SecretHitlerCharacterSelectionFragment::applyPreferences(): mSecretHitlerControlGroup is NULL");
        }

        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        mSecretHitlerControlGroup.selectPlayerNumberButton(viewModel.getSecretHitlerExpectedGoodTotal() + viewModel.getSecretHitlerExpectedEvilTotal());
    }

    private SecretHitlerControlGroup mSecretHitlerControlGroup;
}