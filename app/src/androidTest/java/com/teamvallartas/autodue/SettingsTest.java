package com.teamvallartas.autodue;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

/**
 * Created by billyandika on 12/5/15.
 */

@RunWith(AndroidJUnit4.class)
public class SettingsTest {


    @Rule
    public ActivityTestRule<Settings> mActivityRule =
            new ActivityTestRule<>(Settings.class);

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* Settings scenario (across 2 methods):
        Given a user is in the settings screen
        When the user specifies the start time and end time
        The app stores that information and reflects it the next time
     */
    @Test
    public void ensureOffLimitTimesSet() {
        // Given a user is in the settings screen
        onView(withId(R.id.set_button)).check(matches(isDisplayed()));
        pauseTestFor(500);
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));
        pauseTestFor(500);

        // When the user selects a start time and end time
        onView(withId(R.id.starttime)).perform(replaceText("01:00"));
        pauseTestFor(500);
        onView(withId(R.id.endtime)).perform(replaceText("09:00"));
        pauseTestFor(500);
        onView(withId(R.id.set_button)).perform(click());
        pauseTestFor(500);
    }
    @Test
    public void ensureOffLimitTimesPreserved() {
        // The app stores that information and reflects it the next time
        pauseTestFor(1000);
        onView(withId(R.id.current_offlimit_time)).check(matches(withText(" 01:00 - 09:00")));
    }
}