package com.teamvallartas.autodue;

/**
 * Created by billyandika on 12/4/15.
 * Edited by John Senar
 */
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.core.IsNull.notNullValue;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class RecyclerActivityTest {


    @Rule
    public ActivityTestRule<RecyclerViewDemoActivity> mActivityRule =
            new ActivityTestRule<>(RecyclerViewDemoActivity.class);

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ensureCancelWorksDataEntered() {

        //Given a user clicks add task and inputs information
        onView(withId(R.id.fab_add)).perform(click());
        pauseTestFor(500);
        onView(withId(R.id.task_name_message)).perform(typeText("Do lab work"), closeSoftKeyboard());
        pauseTestFor(500);
        onView(withId(R.id.description_name_message)).perform(typeText("Parts 1 and 2"), closeSoftKeyboard());
        pauseTestFor(500);
        onView(withId(R.id.duration_time)).perform(typeText("2"), closeSoftKeyboard());
        pauseTestFor(500);
        onView(withId(R.id.duedate)).perform(replaceText("12/12/2015"));
        pauseTestFor(500);
        onView(withId(R.id.duetime_hour)).perform(replaceText("00:00"));
        pauseTestFor(500);

        //When they hit the cancel button
        onView(withId(R.id.cancelButton)).perform(click());
        //Then no task is created and they go back to the task list screen
        onView(withId(R.id.fab_add)).check(matches(isDisplayed()));

        //Checks if add button is displayed since it is on the task list screen
    }

    @Test
    public void ensureDoneWorksDataEntered() {
        //Given a user clicks add task and inputs information
        onView(withId(R.id.fab_add)).perform(click());
        pauseTestFor(500);
        onView(withId(R.id.task_name_message)).perform(typeText("Do lab work"), closeSoftKeyboard());
        pauseTestFor(500);
        onView(withId(R.id.description_name_message)).perform(typeText("Parts 1 and 2"), closeSoftKeyboard());
        pauseTestFor(500);
        onView(withId(R.id.duration_time)).perform(typeText("1"), closeSoftKeyboard());
        pauseTestFor(500);
        onView(withId(R.id.duedate)).perform(replaceText("12/12/2015"));
        pauseTestFor(500);
        onView(withId(R.id.duetime_hour)).perform(replaceText("00:00"));
        pauseTestFor(500);

        //When they hit the done button
        onView(withId(R.id.doneButton)).perform(click());
        pauseTestFor(500);

        //Then user is given the option to generate an event
        //Checks if the generate button is displayed
        onView(withId(R.id.generate_button)).check(matches(isDisplayed()));

        // Extra: generating the event should bring the user back to the main task screen
        onView(withId(R.id.generate_button)).perform(click());
        onView(withId(R.id.fab_add)).check(matches(isDisplayed()));
    }

    @Test
    public void ensureDoneWorksNoDataEntered() {
        //Given a user clicks add task and inputs no information
        onView(withId(R.id.fab_add)).perform(click());
        pauseTestFor(500);


        //When they hit the done button
        onView(withId(R.id.doneButton)).perform(click());
        //Then app stays on the add task page and no task is created
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()));

        //Checks if the done button is still displayed

    }


}


