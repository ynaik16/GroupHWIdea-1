package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.widget.ListView;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest extends AndroidJUnitRunner {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.test", appContext.getPackageName());
    }

    @Test
    public void addContactTest() {
        onView(withId(R.id.btnXMLAddContact)).perform(click());

        onView(withId(R.id.editTextName)).perform(typeText("John Woods"),closeSoftKeyboard());
        onView(withId(R.id.editTextPhone)).perform(typeText("5555555555"),closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText("jwoods5@gmail.com"),closeSoftKeyboard());
        onView(withId(R.id.btnXMLConfirm)).perform(click());

        //if it can click the item, it's successful
        onData(anything()).inAdapterView(withId(R.id.lvContacts)).atPosition(0).perform(click());
        pressBack();
    }

    @Test
    public void deleteContactTest() {
        //just in case there are no contacts
        onView(withId(R.id.btnXMLAddContact)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("."),closeSoftKeyboard());
        onView(withId(R.id.editTextPhone)).perform(typeText("."),closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText("."),closeSoftKeyboard());
        onView(withId(R.id.btnXMLConfirm)).perform(click());

        //simulate deleting contact
        onData(anything()).inAdapterView(withId(R.id.lvContacts)).atPosition(0).perform(click());
        onView(withId(R.id.btnPopupDelete)).perform(click());

        onView(withId(R.id.textView)).check(matches(withText("Contacts 1")));
    }
}
