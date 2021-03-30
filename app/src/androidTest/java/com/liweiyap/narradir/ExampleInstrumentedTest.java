package com.liweiyap.narradir;

import android.content.Context;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.liweiyap.narradir.utils.CheckableObserverImageButton;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.liweiyap.narradir", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule<AvalonCharacterSelectionActivity> mActivityRule = new ActivityScenarioRule<>(AvalonCharacterSelectionActivity.class);

    @Test
    public void dummyTest()
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