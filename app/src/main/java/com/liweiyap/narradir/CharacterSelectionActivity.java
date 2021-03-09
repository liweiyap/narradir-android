package com.liweiyap.narradir;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Pair;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.liweiyap.narradir.fontutil.CustomTypefaceableObserverButton;

import java.util.Objects;

public class CharacterSelectionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
        clickSoundMediaPlayer = MediaPlayer.create(this, R.raw.clicksound);

        setPlayerNumberSelectionButtonBackgrounds();
        addSingleTargetSelectionToPlayerNumberSelectionLayout();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        clickSoundMediaPlayer.release();
    }

    private void setPlayerNumberSelectionButtonBackgrounds()
    {
        playerNumberSelectionButtonBackgrounds.put(
            R.id.p5Button, new Pair<>(R.drawable.p5_button_checked_background, R.drawable.p5_button_unchecked_background));
        playerNumberSelectionButtonBackgrounds.put(
            R.id.p6Button, new Pair<>(R.drawable.p6_button_checked_background, R.drawable.p6_button_unchecked_background));
        playerNumberSelectionButtonBackgrounds.put(
            R.id.p7Button, new Pair<>(R.drawable.p7_button_checked_background, R.drawable.p7_button_unchecked_background));
        playerNumberSelectionButtonBackgrounds.put(
            R.id.p8Button, new Pair<>(R.drawable.p8_button_checked_background, R.drawable.p8_button_unchecked_background));
        playerNumberSelectionButtonBackgrounds.put(
            R.id.p9Button, new Pair<>(R.drawable.p9_button_checked_background, R.drawable.p9_button_unchecked_background));
        playerNumberSelectionButtonBackgrounds.put(
            R.id.p10Button, new Pair<>(R.drawable.p10_button_checked_background, R.drawable.p10_button_unchecked_background));
    }

    private void addSingleTargetSelectionToPlayerNumberSelectionLayout()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableObserverButton btn = (CustomTypefaceableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            int i = childIdx;
            btn.addOnClickObserver(() -> {
                clickSoundMediaPlayer.start();
                btn.setAlpha(1.f);
                btn.setBackgroundDrawable(ContextCompat.getDrawable(this, Objects.requireNonNull(playerNumberSelectionButtonBackgrounds.get(btn.getId())).first));

                for (int j = 0; j < playerNumberSelectionLayout.getChildCount(); ++j)
                {
                    if (i != j)
                    {
                        CustomTypefaceableObserverButton tmp = (CustomTypefaceableObserverButton) playerNumberSelectionLayout.getChildAt(j);
                        tmp.setAlpha(0.5f);
                        tmp.setBackgroundDrawable(ContextCompat.getDrawable(this, Objects.requireNonNull(playerNumberSelectionButtonBackgrounds.get(tmp.getId())).second));
                    }
                }
            });
        }
    }

    /**
     * Key is an Integer representing the ID of the button.
     * Value is a Pair of Integers.
     *  - First Integer in Pair represents the ID of the background drawable of the button in the checked state.
     *  - Second Integer in Pair represents the ID of the background drawable of the button in the unchecked state.
     */
    private final ArrayMap<Integer, Pair<Integer,Integer>> playerNumberSelectionButtonBackgrounds = new ArrayMap<>();
    private MediaPlayer clickSoundMediaPlayer;
}