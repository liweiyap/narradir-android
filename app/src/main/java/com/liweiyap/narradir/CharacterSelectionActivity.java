package com.liweiyap.narradir;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.liweiyap.narradir.utils.CheckableObserverImageButton;
import com.liweiyap.narradir.utils.FullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverListener;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;

public class CharacterSelectionActivity extends FullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        // ------------------------------------------------------------
        // player number selection layout
        // ------------------------------------------------------------

        addSingleTargetSelectionToPlayerNumberSelectionLayout();

        // ------------------------------------------------------------
        // character selection layouts
        // ------------------------------------------------------------

        initialiseCharacterImageButtonArray();

        // no need to call prepare(); create() does that for you (https://stackoverflow.com/a/59682667/12367873)
        mClickSoundMediaPlayer = MediaPlayer.create(this, R.raw.clicksound);
        addSoundToPlayOnButtonClick();

        for (CheckableObserverImageButton characterImageButton : mCharacterImageButtonArray)
        {
            characterImageButton.addOnClickObserver(() -> {
                if (mGeneralMediaPlayer != null)
                {
                    mGeneralMediaPlayer.stop();
                }
            });
        }

        addSelectionRules();
        addCharacterDescriptions();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.seekTo(mGeneralMediaPlayerCurrentLength);
            mGeneralMediaPlayer.start();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.pause();
            mGeneralMediaPlayerCurrentLength = mGeneralMediaPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mClickSoundMediaPlayer.release();
        mClickSoundMediaPlayer = null;

        if (mGeneralMediaPlayer != null)
        {
            mGeneralMediaPlayer.release();
            mGeneralMediaPlayer = null;
        }
    }

    private void addSoundToPlayOnButtonClick()
    {
        LinearLayout playerNumberSelectionLayout = findViewById(R.id.playerNumberSelectionLayout);
        for (int childIdx = 0; childIdx < playerNumberSelectionLayout.getChildCount(); ++childIdx)
        {
            CustomTypefaceableCheckableObserverButton btn = (CustomTypefaceableCheckableObserverButton) playerNumberSelectionLayout.getChildAt(childIdx);
            addSoundToPlayOnButtonClick(btn);
        }

        for (CheckableObserverImageButton characterImageButton : mCharacterImageButtonArray)
        {
            addSoundToPlayOnButtonClick(characterImageButton);
        }
    }

    private void addSoundToPlayOnButtonClick(ObserverListener btn)
    {
        if (btn == null)
        {
            return;
        }

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

    private void initialiseCharacterImageButtonArray()
    {
        mCharacterImageButtonArray = new CheckableObserverImageButton[3];
        mCharacterImageButtonArray[CharacterName.MERLIN] = findViewById(R.id.merlinButton);
        mCharacterImageButtonArray[CharacterName.PERCIVAL] = findViewById(R.id.percivalButton);
        mCharacterImageButtonArray[CharacterName.LOYAL0] = findViewById(R.id.loyal0Button);
    }

    private void addCharacterDescriptions()
    {
        try
        {
            mCharacterImageButtonArray[CharacterName.MERLIN].addOnLongClickObserver(() -> playCharacterDescription(R.raw.merlindescription));
            mCharacterImageButtonArray[CharacterName.PERCIVAL].addOnLongClickObserver(() -> playCharacterDescription(R.raw.percivaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL0].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        mCharacterImageButtonArray[CharacterName.MERLIN].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.MERLIN].toggle();
        });

        mCharacterImageButtonArray[CharacterName.PERCIVAL].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.PERCIVAL].toggle();
        });

        mCharacterImageButtonArray[CharacterName.LOYAL0].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL0].toggle();
        });
    }

    private MediaPlayer mClickSoundMediaPlayer;
    private MediaPlayer mGeneralMediaPlayer;
    private int mGeneralMediaPlayerCurrentLength;

    private CheckableObserverImageButton[] mCharacterImageButtonArray;
}