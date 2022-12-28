package com.liweiyap.narradir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.liweiyap.narradir.avalon.AvalonControlGroup;
import com.liweiyap.narradir.ui.CheckableObserverImageButton;

/**
 * Gets rid of RuntimeException("ControlFragment::onAttach(): Programming error: Context is not NarradirControl.")
 * due to original Fragment not being attached to Activity that implements NarradirControl interface.
 */
public class MockAvalonCharacterSelectionFragment extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_character_selection_avalon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        LinearLayout playerNumberSelectionLayout = view.findViewById(R.id.playerNumberSelectionLayout);

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
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (mAvalonControlGroup != null)
        {
            mAvalonControlGroup.destroy();
            mAvalonControlGroup = null;
        }
    }

    public CheckableObserverImageButton[] getCharacterImageButtonArray()
    {
        if (mAvalonControlGroup == null)
        {
            throw new RuntimeException("MockAvalonCharacterSelectionFragment::getCharacterImageButtonArray(): mAvalonControlGroup is NULL");
        }

        return mAvalonControlGroup.getCharacterImageButtonArray();
    }

    private AvalonControlGroup mAvalonControlGroup;
}