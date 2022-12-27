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

import com.liweiyap.narradir.avalon.AvalonCharacterName;
import com.liweiyap.narradir.avalon.AvalonControlGroup;
import com.liweiyap.narradir.ui.ControlFragment;
import com.liweiyap.narradir.ui.ObserverImageButton;
import com.liweiyap.narradir.ui.ObserverListener;
import com.liweiyap.narradir.ui.TextViewAutosizeHelper;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.util.NarradirControl;
import com.liweiyap.narradir.util.NarradirViewModel;

import java.util.ArrayList;

public class AvalonCharacterSelectionFragment extends ControlFragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_character_selection_avalon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        LinearLayout playerNumberSelectionLayout = view.findViewById(R.id.playerNumberSelectionLayout);
        CustomTypefaceableObserverButton gameSwitcherButton = view.findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        CustomTypefaceableObserverButton playButton = view.findViewById(R.id.characterSelectionLayoutPlayButton);
        ObserverImageButton settingsButton = view.findViewById(R.id.characterSelectionLayoutSettingsButton);

        // -----------------------------------------------------------------------------------------
        // Avalon is the default game but another game might have been the last selected.
        // If last selected game is not the default game, then switch game.
        // -----------------------------------------------------------------------------------------

        gameSwitcherButton.setText(getString(R.string.game_switcher_button_secrethitler));
        gameSwitcherButton.addOnClickObserver(this::navigateToSecretHitlerCharacterSelectionFragment);

        NarradirViewModel viewModel = getViewModel();
        if ( (viewModel != null) && (!viewModel.isAvalonLastSelected()) )
        {
            gameSwitcherButton.performClick();
        }

        // -----------------------------------------------------------------------------------------
        // character image button array, player number selection layout, character selection layouts
        // -----------------------------------------------------------------------------------------

        mAvalonControlGroup = new AvalonControlGroup(
            requireActivity(),
            playerNumberSelectionLayout,
            view.findViewById(R.id.p5Button), view.findViewById(R.id.p6Button), view.findViewById(R.id.p7Button),
            view.findViewById(R.id.p8Button), view.findViewById(R.id.p9Button), view.findViewById(R.id.p10Button),
            view.findViewById(R.id.merlinButton), view.findViewById(R.id.percivalButton), view.findViewById(R.id.loyal0Button), view.findViewById(R.id.loyal1Button),
            view.findViewById(R.id.loyal2Button), view.findViewById(R.id.loyal3Button), view.findViewById(R.id.loyal4Button), view.findViewById(R.id.loyal5Button),
            view.findViewById(R.id.assassinButton), view.findViewById(R.id.morganaButton), view.findViewById(R.id.mordredButton), view.findViewById(R.id.oberonButton),
            view.findViewById(R.id.minion0Button), view.findViewById(R.id.minion1Button), view.findViewById(R.id.minion2Button), view.findViewById(R.id.minion3Button));

        applyPreferences();

        // -----------------------------------------------------------------------------------------
        // sounds
        // -----------------------------------------------------------------------------------------

        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.ASSASSIN));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED));
        addSoundToPlayOnButtonClick(mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON));
        addSoundToPlayOnButtonClick(gameSwitcherButton);
        addSoundToPlayOnButtonClick(playButton);
        addSoundToPlayOnButtonClick(settingsButton);

        // -----------------------------------------------------------------------------------------
        // navigation bar (of fragment, not of phone)
        // -----------------------------------------------------------------------------------------

        playButton.addOnClickObserver(this::navigateToPlayIntroductionFragment);

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

        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(view.findViewById(R.id.gameHintTextView));
        TextViewAutosizeHelper.minimiseAutoSizeTextSizeRange(gameSwitcherButton);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mAvalonControlGroup == null)
        {
            return;
        }
        mAvalonControlGroup.resumeCharacterDescriptionMediaPlayer();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        savePreferences();  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873

        if (mAvalonControlGroup == null)
        {
            return;
        }
        mAvalonControlGroup.pauseCharacterDescriptionMediaPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mAvalonControlGroup != null)
        {
            mAvalonControlGroup.destroy();
            mAvalonControlGroup = null;
        }
    }

    private void addSoundToPlayOnButtonClick(final ObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

        btn.addOnClickObserver(() -> {
            if (mAvalonControlGroup != null)
            {
                mAvalonControlGroup.stopCharacterDescriptionMediaPlayer();
            }

            NarradirControl narradirControl = getNarradirControl();
            if (narradirControl != null)
            {
                narradirControl.playClickSound();
            }
        });
    }

    private void navigateToSecretHitlerCharacterSelectionFragment()
    {
        NarradirViewModel viewModel = getViewModel();
        if (viewModel != null)
        {
            viewModel.saveSecretHitlerLastSelected();
        }

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.secretHitlerCharacterSelectionFragment);
    }

    private void navigateToPlayIntroductionFragment()
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionFragment::navigateToPlayIntroductionFragment(): mAvalonControlGroup is NULL");
        }

        ArrayList<String> introSegmentArrayList = new ArrayList<>();

        introSegmentArrayList.add(getString(R.string.avalonintrosegment0_key));

        introSegmentArrayList.add(getString(mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).isChecked() ?
            R.string.avalonintrosegment1withoberon_key :
            R.string.avalonintrosegment1nooberon_key));

        introSegmentArrayList.add(getString(R.string.avalonintrosegment2_key));

        if (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked())
        {
            introSegmentArrayList.add(getString(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).isChecked() ?
                R.string.avalonintrosegment3withmordred_key :
                R.string.avalonintrosegment3nomordred_key));

            introSegmentArrayList.add(getString(R.string.avalonintrosegment4_key));

            if (mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).isChecked())
            {
                final boolean isMorganaChecked = mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).isChecked();

                introSegmentArrayList.add(getString(isMorganaChecked ?
                    R.string.avalonintrosegment5withpercivalwithmorgana_key :
                    R.string.avalonintrosegment5withpercivalnomorgana_key));

                introSegmentArrayList.add(getString(isMorganaChecked ?
                    R.string.avalonintrosegment6withpercivalwithmorgana_key :
                    R.string.avalonintrosegment6withpercivalnomorgana_key));

                introSegmentArrayList.add(getString(R.string.avalonintrosegment7_key));
            }
            else
            {
                introSegmentArrayList.add(getString(R.string.avalonintrosegment5nopercival_key));
            }
        }
        else
        {
            introSegmentArrayList.add(getString(R.string.avalonintrosegment3nomerlin_key));
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(getString(R.string.intro_segments_key), introSegmentArrayList);
        bundle.putBoolean(getString(R.string.is_started_from_avalon_key), true);

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.playIntroductionFragment, bundle);
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private void savePreferences()
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionFragment::savePreferences(): mAvalonControlGroup is NULL");
        }

        if (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked() != mAvalonControlGroup.getCharacter(AvalonCharacterName.ASSASSIN).isChecked())
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionFragment::savePreferences(): " +
                    "Checked state of Assassin should be identical to that of Merlin");
        }

        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        viewModel.setAvalonExpectedGoodTotal(mAvalonControlGroup.getExpectedGoodTotal());
        viewModel.setAvalonExpectedEvilTotal(mAvalonControlGroup.getExpectedEvilTotal());
        viewModel.setMerlinChecked(mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked());
        viewModel.setPercivalChecked(mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).isChecked());
        viewModel.setMorganaChecked(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).isChecked());
        viewModel.setMordredChecked(mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).isChecked());
        viewModel.setOberonChecked(mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).isChecked());
        viewModel.savePreferences();
    }

    private void applyPreferences()
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("AvalonCharacterSelectionFragment::applyPreferences(): mAvalonControlGroup is NULL");
        }

        if ( (mAvalonControlGroup.getCharacterImageButtonArray() == null) || (mAvalonControlGroup.getPlayerNumberButtonArray() == null) )
        {
            return;
        }

        NarradirViewModel viewModel = getViewModel();
        if (viewModel == null)
        {
            return;
        }

        mAvalonControlGroup.selectPlayerNumberButton(viewModel.getAvalonExpectedGoodTotal() + viewModel.getAvalonExpectedEvilTotal());

        // default: Merlin is checked
        // saved preferences: if Merlin is not checked
        if ( (!viewModel.isMerlinChecked()) && (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).performClick();
        }

        // default: Percival is not checked
        // saved preferences: if Percival is checked
        if ( (viewModel.isPercivalChecked()) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.PERCIVAL).performClick();
        }

        // default: Morgana is not checked
        // saved preferences: if Morgana is checked
        if ( (viewModel.isMorganaChecked()) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.MORGANA).performClick();
        }

        // default: Mordred is not checked
        // saved preferences: if Mordred is checked
        if ( (viewModel.isMordredChecked()) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.MORDRED).performClick();
        }

        // default: Oberon is not checked
        // saved preferences: if Oberon is checked
        if ( (viewModel.isOberonChecked()) && (!mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).isChecked()) )
        {
            mAvalonControlGroup.getCharacter(AvalonCharacterName.OBERON).performClick();
        }

        mAvalonControlGroup.checkPlayerComposition("AvalonCharacterSelectionFragment::applyPreferences()");

        if (mAvalonControlGroup.getCharacter(AvalonCharacterName.MERLIN).isChecked() != mAvalonControlGroup.getCharacter(AvalonCharacterName.ASSASSIN).isChecked())
        {
            throw new RuntimeException(
                "AvalonCharacterSelectionFragment::applyPreferences(): " +
                    "Checked state of Assassin should be identical to that of Merlin");
        }
    }

    private AvalonControlGroup mAvalonControlGroup;
}