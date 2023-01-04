package com.liweiyap.narradir

import android.os.Bundle

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.liweiyap.narradir.avalon.AvalonCharacterName

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To activate this, click on Build Variants tab at the bottom left corner and change to Debug variant.
 */
@RunWith(AndroidJUnit4::class)
class MorganaInNon5PlayerModeTest {
    @Test
    fun runTest() {
        val mockFragment = launchFragmentInContainer<MockAvalonCharacterSelectionFragment>(fragmentArgs = Bundle(), themeResId = R.style.AppTheme)

        onView(withId(R.id.merlinButton)).perform(click())

        mockFragment.onFragment {
            Assert.assertFalse(it.getCharacter(AvalonCharacterName.MERLIN).isChecked)
            Assert.assertFalse(it.getCharacter(AvalonCharacterName.ASSASSIN).isChecked)
            Assert.assertTrue(it.getCharacter(AvalonCharacterName.LOYAL0).isChecked)
            Assert.assertTrue(it.getCharacter(AvalonCharacterName.LOYAL1).isChecked)
            Assert.assertTrue(it.getCharacter(AvalonCharacterName.LOYAL2).isChecked)
            Assert.assertTrue(it.getCharacter(AvalonCharacterName.MINION0).isChecked)
            Assert.assertTrue(it.getCharacter(AvalonCharacterName.MINION1).isChecked)
        }

        onView(withId(R.id.p10Button)).perform(click())

        mockFragment.onFragment {
            Assert.assertFalse(it.getCharacter(AvalonCharacterName.MERLIN).isChecked)
            Assert.assertFalse(it.getCharacter(AvalonCharacterName.ASSASSIN).isChecked)
        }

        onView(withId(R.id.percivalButton)).perform(click())

        // Morgana should be checked only in 5-player mode.
        mockFragment.onFragment {
            Assert.assertFalse(it.getCharacter(AvalonCharacterName.MORGANA).isChecked)
        }
    }
}