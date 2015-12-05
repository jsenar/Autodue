package com.teamvallartas.autodue;

/**
 * Created by billyandika on 12/4/15.
 * Edited by John Senar
 */
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.widget.DatePicker;
import android.widget.Toast;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;
import com.teamvallartas.autodue.RecyclerViewDemoActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;


import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;

@RunWith(AndroidJUnit4.class)
public class RecyclerActivityTest {


    @Rule
    public ActivityTestRule<RecyclerViewDemoActivity> mActivityRule =
            new ActivityTestRule<>(RecyclerViewDemoActivity.class);


    @Test
    public void ensureCancelWorksNoDataEntered() {
        //Given a user clicks add task and doesn't input any info
        onView(withId(R.id.fab_add)).perform(click());
        //When they hit the cancel button
        onView(withId(R.id.cancelButton)).perform(click());
        //No task is created and they go back to the task list screen
        onView(withId(R.id.fab_add)).check(matches(isDisplayed()));

        //Checks if add button is displayed since it is on the task list screen
    }

    @Test
    public void ensureDoneWorksNoDataEntered() {
        //Given a user clicks add task and doesn't input any info
        onView(withId(R.id.fab_add)).perform(click());
        //When they hit the done button
        onView(withId(R.id.doneButton)).perform(click());
        //No task is created and they stay on the add task screen
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()));

        //Checks if done button is still displayed since it's on the add task screen

    }
//    @Test
//    public void changeText_newActivity() {
//        // Type text and then press the button.
//        onView(withId(R.id.inputField)).perform(typeText("NewText"),
//                closeSoftKeyboard());
//        onView(withId(R.id.switchActivity)).perform(click());
//
//        // This view is in a different Activity, no need to tell Espresso.
//        onView(withId(R.id.resultView)).check(matches(withText("NewText")));
//    }
}


