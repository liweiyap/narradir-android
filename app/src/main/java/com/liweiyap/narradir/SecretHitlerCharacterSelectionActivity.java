package com.liweiyap.narradir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RawRes;

import com.liweiyap.narradir.utils.ActiveFullScreenPortraitActivity;
import com.liweiyap.narradir.utils.ObserverImageButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableCheckableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;

import org.jetbrains.annotations.NotNull;

public class SecretHitlerCharacterSelectionActivity extends ActiveFullScreenPortraitActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection_secrethitler);

        initialiseCharacterImageButtonArray();

        // ------------------------------------------------------------
        // player number selection layout
        // ------------------------------------------------------------

        addSingleTargetSelectionToPlayerNumberSelectionLayout();
        adaptAvailableCharactersAccordingToPlayerNumber();
        loadPreferences();

        // ------------------------------------------------------------
        // navigation bar (of activity, not of phone)
        // ------------------------------------------------------------

        CustomTypefaceableObserverButton gameSwitcherButton = findViewById(R.id.characterSelectionLayoutGameSwitcherButton);
        gameSwitcherButton.setText(getString(R.string.game_switcher_button_avalon));
        gameSwitcherButton.addOnClickObserver(() -> navigateToAvalonCharacterSelectionActivity(gameSwitcherButton));
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        savePreferences();  // https://stackoverflow.com/a/32576942/12367873; https://stackoverflow.com/a/14756816/12367873
    }

    private void initialiseCharacterImageButtonArray()
    {
        mCharacterImageButtonArray = new ObserverImageButton[SecretHitlerCharacterName.getNumberOfCharacters()];
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL0] = findViewById(R.id.liberal0Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL1] = findViewById(R.id.liberal1Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL2] = findViewById(R.id.liberal2Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3] = findViewById(R.id.liberal3Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4] = findViewById(R.id.liberal4Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5] = findViewById(R.id.liberal5Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.HITLER] = findViewById(R.id.hitlerButton);
        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST0] = findViewById(R.id.fascist0Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1] = findViewById(R.id.fascist1Button);
        mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2] = findViewById(R.id.fascist2Button);
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
            mExpectedGoodTotal = 3;
            mExpectedEvilTotal = 2;

            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2].setVisibility(View.INVISIBLE);

            int actualGoodTotal = getActualGoodTotal();
            if (actualGoodTotal != mExpectedGoodTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p5Button.onClick(): " +
                        "expected good player total is " + mExpectedGoodTotal +
                        " but actual good player total is " + actualGoodTotal);
            }

            int actualEvilTotal = getActualEvilTotal();
            if (actualEvilTotal != mExpectedEvilTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p5Button.onClick(): " +
                        "expected evil player total is " + mExpectedEvilTotal +
                        " but actual evil player total is " + actualEvilTotal);
            }
        });

        p6Button.addOnClickObserver(() -> {
            mExpectedGoodTotal = 4;
            mExpectedEvilTotal = 2;

            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2].setVisibility(View.INVISIBLE);

            int actualGoodTotal = getActualGoodTotal();
            if (actualGoodTotal != mExpectedGoodTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p6Button.onClick(): " +
                        "expected good player total is " + mExpectedGoodTotal +
                        " but actual good player total is " + actualGoodTotal);
            }

            int actualEvilTotal = getActualEvilTotal();
            if (actualEvilTotal != mExpectedEvilTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p6Button.onClick(): " +
                        "expected evil player total is " + mExpectedEvilTotal +
                        " but actual evil player total is " + actualEvilTotal);
            }
        });

        p7Button.addOnClickObserver(() -> {
            mExpectedGoodTotal = 4;
            mExpectedEvilTotal = 3;

            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2].setVisibility(View.INVISIBLE);

            int actualGoodTotal = getActualGoodTotal();
            if (actualGoodTotal != mExpectedGoodTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p7Button.onClick(): " +
                        "expected good player total is " + mExpectedGoodTotal +
                        " but actual good player total is " + actualGoodTotal);
            }

            int actualEvilTotal = getActualEvilTotal();
            if (actualEvilTotal != mExpectedEvilTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p7Button.onClick(): " +
                        "expected evil player total is " + mExpectedEvilTotal +
                        " but actual evil player total is " + actualEvilTotal);
            }
        });

        p8Button.addOnClickObserver(() -> {
            mExpectedGoodTotal = 5;
            mExpectedEvilTotal = 3;

            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2].setVisibility(View.INVISIBLE);

            int actualGoodTotal = getActualGoodTotal();
            if (actualGoodTotal != mExpectedGoodTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p8Button.onClick(): " +
                        "expected good player total is " + mExpectedGoodTotal +
                        " but actual good player total is " + actualGoodTotal);
            }

            int actualEvilTotal = getActualEvilTotal();
            if (actualEvilTotal != mExpectedEvilTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p8Button.onClick(): " +
                        "expected evil player total is " + mExpectedEvilTotal +
                        " but actual evil player total is " + actualEvilTotal);
            }
        });

        p9Button.addOnClickObserver(() -> {
            mExpectedGoodTotal = 5;
            mExpectedEvilTotal = 4;

            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5].setVisibility(View.INVISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2].setVisibility(View.VISIBLE);

            int actualGoodTotal = getActualGoodTotal();
            if (actualGoodTotal != mExpectedGoodTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p9Button.onClick(): " +
                        "expected good player total is " + mExpectedGoodTotal +
                        " but actual good player total is " + actualGoodTotal);
            }

            int actualEvilTotal = getActualEvilTotal();
            if (actualEvilTotal != mExpectedEvilTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p9Button.onClick(): " +
                        "expected evil player total is " + mExpectedEvilTotal +
                        " but actual evil player total is " + actualEvilTotal);
            }
        });

        p10Button.addOnClickObserver(() -> {
            mExpectedGoodTotal = 6;
            mExpectedEvilTotal = 4;

            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL3].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL4].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.LIBERAL5].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST1].setVisibility(View.VISIBLE);
            mCharacterImageButtonArray[SecretHitlerCharacterName.FASCIST2].setVisibility(View.VISIBLE);

            int actualGoodTotal = getActualGoodTotal();
            if (actualGoodTotal != mExpectedGoodTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p10Button.onClick(): " +
                        "expected good player total is " + mExpectedGoodTotal +
                        " but actual good player total is " + actualGoodTotal);
            }

            int actualEvilTotal = getActualEvilTotal();
            if (actualEvilTotal != mExpectedEvilTotal)
            {
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::p10Button.onClick(): " +
                        "expected evil player total is " + mExpectedEvilTotal +
                        " but actual evil player total is " + actualEvilTotal);
            }
        });
    }

    public int getActualGoodTotal()
    {
        int actualGoodTotal = 0;
        for (int idx = SecretHitlerCharacterName.LIBERAL0; idx <= SecretHitlerCharacterName.LIBERAL5; ++idx)
        {
            if (mCharacterImageButtonArray[idx].getVisibility() == View.VISIBLE)
            {
                ++actualGoodTotal;
            }
        }

        return actualGoodTotal;
    }

    public int getActualEvilTotal()
    {
        int actualEvilTotal = 0;
        for (int idx = SecretHitlerCharacterName.HITLER; idx <= SecretHitlerCharacterName.FASCIST2; ++idx)
        {
            if (mCharacterImageButtonArray[idx].getVisibility() == View.VISIBLE)
            {
                ++actualEvilTotal;
            }
        }

        return actualEvilTotal;
    }

    private void navigateToAvalonCharacterSelectionActivity(@NotNull View view)
    {
        Intent intent = new Intent(view.getContext(), CharacterSelectionActivity.class);
        finish();
        view.getContext().startActivity(intent);
    }

    /**
     * https://stackoverflow.com/a/24822131/12367873
     * https://stackoverflow.com/a/10991785/12367873
     */
    private void savePreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        sharedPrefEditor.putInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        sharedPrefEditor.putFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        sharedPrefEditor.putFloat(getString(R.string.narration_volume_key), mNarrationVolume);
        sharedPrefEditor.putInt(getString(R.string.good_player_number_key), mExpectedGoodTotal);
        sharedPrefEditor.putInt(getString(R.string.evil_player_number_key), mExpectedEvilTotal);
        sharedPrefEditor.apply();
    }

    private void loadPreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        mPauseDurationInMilliSecs = sharedPref.getLong(getString(R.string.pause_duration_key), mPauseDurationInMilliSecs);
        mBackgroundSoundRawResId = sharedPref.getInt(getString(R.string.background_sound_key), mBackgroundSoundRawResId);
        mBackgroundSoundVolume = sharedPref.getFloat(getString(R.string.background_volume_key), mBackgroundSoundVolume);
        mNarrationVolume = sharedPref.getFloat(getString(R.string.narration_volume_key), mNarrationVolume);

        int expectedGoodTotal = sharedPref.getInt(getString(R.string.good_player_number_key), mExpectedGoodTotal);
        int expectedEvilTotal = sharedPref.getInt(getString(R.string.evil_player_number_key), mExpectedEvilTotal);

        switch (expectedGoodTotal + expectedEvilTotal)
        {
            case 5:
                CustomTypefaceableCheckableObserverButton p5Button = findViewById(R.id.p5Button);
                p5Button.performClick();
                break;
            case 6:
                CustomTypefaceableCheckableObserverButton p6Button = findViewById(R.id.p6Button);
                p6Button.performClick();
                break;
            case 7:
                CustomTypefaceableCheckableObserverButton p7Button = findViewById(R.id.p7Button);
                p7Button.performClick();
                break;
            case 8:
                CustomTypefaceableCheckableObserverButton p8Button = findViewById(R.id.p8Button);
                p8Button.performClick();
                break;
            case 9:
                CustomTypefaceableCheckableObserverButton p9Button = findViewById(R.id.p9Button);
                p9Button.performClick();
                break;
            case 10:
                CustomTypefaceableCheckableObserverButton p10Button = findViewById(R.id.p10Button);
                p10Button.performClick();
                break;
            default:
                throw new RuntimeException(
                    "SecretHitlerCharacterSelectionActivity::loadPreferences(): " +
                        "Invalid no of players: " + (expectedGoodTotal + expectedEvilTotal));
        }
    }

    private ObserverImageButton[] mCharacterImageButtonArray;
    private int mExpectedGoodTotal = 3;
    private int mExpectedEvilTotal = 2;

    private long mPauseDurationInMilliSecs = 5000;
    private @RawRes int mBackgroundSoundRawResId;
    private float mBackgroundSoundVolume = 1f;
    private float mNarrationVolume = 1f;
}