package com.teamvallartas.autodue;

/**
 * Created by billyandika on 12/4/15.
 */
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;
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
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;

@RunWith(AndroidJUnit4.class)
public class RecyclerActivityTest {


    @Rule
    public ActivityTestRule<RecyclerViewDemoActivity> mActivityRule =
            new ActivityTestRule<>(RecyclerViewDemoActivity.class);

    @Test
    public void ensureAddWorks() {

        onView(withId(R.id.fab_add)).perform(click());
        onView(withId(R.id.task_name_message)).perform(typeText("Do lab work"));
        onView(withId(R.id.description_name_message)).perform(typeText("Parts 1 and 2"));
        onView(withId(R.id.duration_time)).perform(typeText("2"), closeSoftKeyboard());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2015, 12, 25));
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15, 25));

        onView(withId(R.id.doneButton)).perform(click());
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
