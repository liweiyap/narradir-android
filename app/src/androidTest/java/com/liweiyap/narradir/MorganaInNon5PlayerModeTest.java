package com.liweiyap.narradir;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.liweiyap.narradir.avalon.AvalonCharacterName;
import com.liweiyap.narradir.ui.CheckableObserverImageButton;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * To activate this, click on Build Variants tab at the bottom left corner and change to Debug variant.
 */
@RunWith(AndroidJUnit4.class)
public class MorganaInNon5PlayerModeTest {
    @Rule
    public ActivityScenarioRule<AvalonCharacterSelectionActivity> mActivityRule = new ActivityScenarioRule<>(AvalonCharacterSelectionActivity.class);

    @Test
    public void runTest()
    {
        onView(withId(R.id.merlinButton)).perform(click());

//        mActivityRule.getScenario().onActivity(activity -> {
//            CheckableObserverImageButton[] characterImageButtonArray = activity.getCharacterImageButtonArray();
//            assertFalse(characterImageButtonArray[AvalonCharacterName.MERLIN].isChecked());
//            assertFalse(characterImageButtonArray[AvalonCharacterName.ASSASSIN].isChecked());
//            assertTrue(characterImageButtonArray[AvalonCharacterName.LOYAL0].isChecked());
//            assertTrue(characterImageButtonArray[AvalonCharacterName.LOYAL1].isChecked());
//            assertTrue(characterImageButtonArray[AvalonCharacterName.LOYAL2].isChecked());
//            assertTrue(characterImageButtonArray[AvalonCharacterName.MINION0].isChecked());
//            assertTrue(characterImageButtonArray[AvalonCharacterName.MINION1].isChecked());
//        });

        onView(withId(R.id.p10Button)).perform(click());

//        mActivityRule.getScenario().onActivity(activity -> {
//            CheckableObserverImageButton[] characterImageButtonArray = activity.getCharacterImageButtonArray();
//            assertFalse(characterImageButtonArray[AvalonCharacterName.MERLIN].isChecked());
//            assertFalse(characterImageButtonArray[AvalonCharacterName.ASSASSIN].isChecked());
//        });

        onView(withId(R.id.percivalButton)).perform(click());

        // Morgana should be checked only in 5-player mode.
        mActivityRule.getScenario().onActivity(activity -> {
            CheckableObserverImageButton[] characterImageButtonArray = activity.getCharacterImageButtonArray();
            assertFalse(characterImageButtonArray[AvalonCharacterName.MORGANA].isChecked());
        });
    }
}