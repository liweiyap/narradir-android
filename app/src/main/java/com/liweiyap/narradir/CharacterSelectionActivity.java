package com.liweiyap.narradir;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.liweiyap.narradir.utils.CheckableObserverImageButton;
import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverImageButton;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;

public class CharacterSelectionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
        mClickSoundMediaPlayer = MediaPlayer.create(this, R.raw.clicksound);
        addSoundToPlayOnButtonClick();

        addSingleTargetSelectionToPlayerNumberSelectionLayout();

        mMerlinButton = findViewById(R.id.merlinButton);
        mPercivalButton = findViewById(R.id.percivalButton);

        addSelectionRules();
        addCharacterDescriptions();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mClickSoundMediaPlayer.release();
        mClickSoundMediaPlayer = null;
    }

    private void addSoundToPlayOnButtonClick()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        LinearLayout goodCharactersLinearLayoutTop = findViewById(R.id.goodCharactersLinearLayoutTop);
        for (int childIdx = 0; childIdx < goodCharactersLinearLayoutTop.getChildCount(); ++childIdx)
        {
            ObserverImageButton btn = (ObserverImageButton) goodCharactersLinearLayoutTop.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }
    }

    private void addSoundToPlayOnButtonClick(ObserverListener btn)
    {
        btn.addOnClickObserver(() -> {
            if (mClickSoundMediaPlayer != null)
            {
                mClickSoundMediaPlayer.start();
            }
        });
    }

    private void addSingleTargetSelectionToPlayerNumberSelectionLayout()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            int i = childIdx;
            btn.addOnClickObserver(() -> {
                btn.check();
                for (int j = 0; j < playerNumberSelectionLayout.getChildCount(); ++j)
                {
                    if (i != j)
                    {
                        CustomTypefaceableCheckableObserverButton tmp = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(j);
                        tmp.uncheck();
                    }
                }
            });
        }
    }

    private void addCharacterDescriptions()
    {
        mMerlinButton.addOnLongClickObserver(() -> playCharacterDescription(R.raw.merlindescription));
        mPercivalButton.addOnLongClickObserver(() -> playCharacterDescription(R.raw.percivaldescription));
    }

    private void playCharacterDescription(int descriptionId)
    {
        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.stop();
        }

        try
        {
            mGeneralMediaPlayer = MediaPlayer.create(this, descriptionId);
            mGeneralMediaPlayer.start();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }

    private void addSelectionRules()
    {
        mMerlinButton.addOnClickObserver(() -> {
            if (mMerlinButton.isChecked())
            {
                mMerlinButton.uncheck();
            }
            else
            {
                mMerlinButton.check();
            }
        });

        mPercivalButton.addOnClickObserver(() -> {
            if (mPercivalButton.isChecked())
            {
                mPercivalButton.uncheck();
            }
            else
            {
                mPercivalButton.check();
            }
        });
    }

    private MediaPlayer mClickSoundMediaPlayer;
    private MediaPlayer mGeneralMediaPlayer;

    private CheckableObserverImageButton mMerlinButton;
    private CheckableObserverImageButton mPercivalButton;
}