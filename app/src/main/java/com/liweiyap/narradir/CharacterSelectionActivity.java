package com.liweiyap.narradir;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        initialiseCharacterImageButtonArray();

        // ------------------------------------------------------------
        // player number selection layout
        // ------------------------------------------------------------

        addSingleTargetSelectionToPlayerNumberSelectionLayout();
        adaptAvailableCharactersAccordingToPlayerNumber();

        // ------------------------------------------------------------
        // character selection layouts
        // ------------------------------------------------------------

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

    private void initialiseCharacterImageButtonArray()
    {
        mCharacterImageButtonArray = new CheckableObserverImageButton[CharacterName.getNumberOfCharacters()];
        mCharacterImageButtonArray[CharacterName.MERLIN] = findViewById(R.id.merlinButton);
        mCharacterImageButtonArray[CharacterName.PERCIVAL] = findViewById(R.id.percivalButton);
        mCharacterImageButtonArray[CharacterName.LOYAL0] = findViewById(R.id.loyal0Button);
        mCharacterImageButtonArray[CharacterName.LOYAL1] = findViewById(R.id.loyal1Button);
        mCharacterImageButtonArray[CharacterName.LOYAL2] = findViewById(R.id.loyal2Button);
        mCharacterImageButtonArray[CharacterName.LOYAL3] = findViewById(R.id.loyal3Button);
        mCharacterImageButtonArray[CharacterName.LOYAL4] = findViewById(R.id.loyal4Button);
        mCharacterImageButtonArray[CharacterName.LOYAL5] = findViewById(R.id.loyal5Button);
        mCharacterImageButtonArray[CharacterName.ASSASSIN] = findViewById(R.id.assassinButton);
        mCharacterImageButtonArray[CharacterName.MORGANA] = findViewById(R.id.morganaButton);
        mCharacterImageButtonArray[CharacterName.MORDRED] = findViewById(R.id.mordredButton);
        mCharacterImageButtonArray[CharacterName.OBERON] = findViewById(R.id.oberonButton);
        mCharacterImageButtonArray[CharacterName.MINION0] = findViewById(R.id.minion0Button);
        mCharacterImageButtonArray[CharacterName.MINION1] = findViewById(R.id.minion1Button);
        mCharacterImageButtonArray[CharacterName.MINION2] = findViewById(R.id.minion2Button);
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

    private void adaptAvailableCharactersAccordingToPlayerNumber()
    {
        CustomTypefaceableCheckableObserverButton p5Button = findViewById(R.id.p5Button);
        CustomTypefaceableCheckableObserverButton p6Button = findViewById(R.id.p6Button);
        CustomTypefaceableCheckableObserverButton p7Button = findViewById(R.id.p7Button);
        CustomTypefaceableCheckableObserverButton p8Button = findViewById(R.id.p8Button);
        CustomTypefaceableCheckableObserverButton p9Button = findViewById(R.id.p9Button);
        CustomTypefaceableCheckableObserverButton p10Button = findViewById(R.id.p10Button);

        p5Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
        });

        p6Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.INVISIBLE);
        });

        p7Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
        });

        p8Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
        });

        p9Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
        });

        p10Button.addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.LOYAL5].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[CharacterName.MINION2].setVisibility(View.VISIBLE);
        });
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

    private void addCharacterDescriptions()
    {
        try
        {
            mCharacterImageButtonArray[CharacterName.MERLIN].addOnLongClickObserver(() -> playCharacterDescription(R.raw.merlindescription));
            mCharacterImageButtonArray[CharacterName.PERCIVAL].addOnLongClickObserver(() -> playCharacterDescription(R.raw.percivaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL0].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL1].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL2].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL3].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL4].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.LOYAL5].addOnLongClickObserver(() -> playCharacterDescription(R.raw.loyaldescription));
            mCharacterImageButtonArray[CharacterName.ASSASSIN].addOnLongClickObserver(() -> playCharacterDescription(R.raw.assassindescription));
            mCharacterImageButtonArray[CharacterName.MORGANA].addOnLongClickObserver(() -> playCharacterDescription(R.raw.morganadescription));
            mCharacterImageButtonArray[CharacterName.MORDRED].addOnLongClickObserver(() -> playCharacterDescription(R.raw.mordreddescription));
            mCharacterImageButtonArray[CharacterName.OBERON].addOnLongClickObserver(() -> playCharacterDescription(R.raw.oberondescription));
            mCharacterImageButtonArray[CharacterName.MINION0].addOnLongClickObserver(() -> playCharacterDescription(R.raw.miniondescription));
            mCharacterImageButtonArray[CharacterName.MINION1].addOnLongClickObserver(() -> playCharacterDescription(R.raw.miniondescription));
            mCharacterImageButtonArray[CharacterName.MINION2].addOnLongClickObserver(() -> playCharacterDescription(R.raw.miniondescription));
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
        mCharacterImageButtonArray[CharacterName.MERLIN].addOnClickObserver(this::addMerlinSelectionRules);

        mCharacterImageButtonArray[CharacterName.PERCIVAL].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.PERCIVAL].toggle();
        });

        mCharacterImageButtonArray[CharacterName.LOYAL0].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL0].toggle();
        });

        mCharacterImageButtonArray[CharacterName.LOYAL1].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL1].toggle();
        });

        mCharacterImageButtonArray[CharacterName.LOYAL2].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL2].toggle();
        });

        mCharacterImageButtonArray[CharacterName.LOYAL3].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL3].toggle();
        });

        mCharacterImageButtonArray[CharacterName.LOYAL4].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL4].toggle();
        });

        mCharacterImageButtonArray[CharacterName.LOYAL5].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.LOYAL5].toggle();
        });

        mCharacterImageButtonArray[CharacterName.ASSASSIN].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.ASSASSIN].toggle();
        });

        mCharacterImageButtonArray[CharacterName.MORGANA].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.MORGANA].toggle();
        });

        mCharacterImageButtonArray[CharacterName.MORDRED].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.MORDRED].toggle();
        });

        mCharacterImageButtonArray[CharacterName.OBERON].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.OBERON].toggle();
        });

        mCharacterImageButtonArray[CharacterName.MINION0].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.MINION0].toggle();
        });

        mCharacterImageButtonArray[CharacterName.MINION1].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.MINION1].toggle();
        });

        mCharacterImageButtonArray[CharacterName.MINION2].addOnClickObserver(() -> {
            mCharacterImageButtonArray[CharacterName.MINION2].toggle();
        });
    }

    /**
     * If Merlin is selected, then Assassin is auto-selected, one of the LOYAL is auto-unselected, and one of the MINIONS is auto-unselected.
     * If Merlin is unselected, then Assassin is auto-unselected, one of the LOYAL is auto-selected, and one of the MINIONS is auto-selected.
     */
    private void addMerlinSelectionRules()
    {
        if (mCharacterImageButtonArray[CharacterName.MERLIN].isChecked())
        {
            mCharacterImageButtonArray[CharacterName.MERLIN].uncheck();
            mCharacterImageButtonArray[CharacterName.ASSASSIN].uncheck();

            // Find the first LOYAL who is VISIBLE and not checked. Then, check him/her
            int currIdx = CharacterName.LOYAL0;
            int endIdx = CharacterName.LOYAL5;
            while (currIdx <= endIdx && mCharacterImageButtonArray[currIdx].getVisibility() == View.VISIBLE)
            {
                if (!mCharacterImageButtonArray[currIdx].isChecked())
                {
                    mCharacterImageButtonArray[currIdx].check();
                    break;
                }

                ++currIdx;
            }

            // Find the first MINION who is VISIBLE and not checked. Then, check him/her.
            currIdx = CharacterName.MINION0;
            endIdx = CharacterName.MINION2;
            while (currIdx <= endIdx && mCharacterImageButtonArray[currIdx].getVisibility() == View.VISIBLE)
            {
                if (!mCharacterImageButtonArray[currIdx].isChecked())
                {
                    mCharacterImageButtonArray[currIdx].check();
                    break;
                }

                ++currIdx;
            }
        }
        else
        {
            mCharacterImageButtonArray[CharacterName.MERLIN].check();
            mCharacterImageButtonArray[CharacterName.ASSASSIN].check();

            // Find the last LOYAL who is VISIBLE and checked. Then, uncheck him/her
            int currIdx = CharacterName.LOYAL5;
            int endIdx = CharacterName.LOYAL0;
            while (currIdx >= endIdx)
            {
                if (mCharacterImageButtonArray[currIdx].getVisibility() == View.INVISIBLE)
                {
                    --currIdx;
                    continue;
                }

                if (mCharacterImageButtonArray[currIdx].isChecked())
                {
                    mCharacterImageButtonArray[currIdx].uncheck();
                    break;
                }

                --currIdx;
            }

            // Find the last MINION who is VISIBLE and checked. Then, uncheck him/her
            currIdx = CharacterName.MINION2;
            endIdx = CharacterName.MINION0;
            while (currIdx >= endIdx)
            {
                if (mCharacterImageButtonArray[currIdx].getVisibility() == View.INVISIBLE)
                {
                    --currIdx;
                    continue;
                }

                if (mCharacterImageButtonArray[currIdx].isChecked())
                {
                    mCharacterImageButtonArray[currIdx].uncheck();
                    break;
                }

                --currIdx;
            }
        }
    }

    private MediaPlayer mClickSoundMediaPlayer;
    private MediaPlayer mGeneralMediaPlayer;
    private int mGeneralMediaPlayerCurrentLength;

    private CheckableObserverImageButton[] mCharacterImageButtonArray;
}