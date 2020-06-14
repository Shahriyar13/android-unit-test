package com.example.ayusch

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.ayusch", appContext.packageName)
    }

    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBetyped = "Save"
    }

    @Test
    fun saveUserInfo_success() {
        // Type text and then press the button.
        onView(withId(R.id.editTextName)).perform(clearText())
        onView(withId(R.id.editTextName))
            .perform(typeText("Shahriyar"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(clearText())
        onView(withId(R.id.editTextEmail))
            .perform(typeText("shahriyar@aghajani.com"), closeSoftKeyboard())
        onView(withId(R.id.buttonSave)).perform(click())
        onView(withId(R.id.buttonSave))
            .check(matches(isDisplayed()))
        onView(withId(R.id.buttonSave))
            .check(matches(withText(stringToBetyped)))

        onView(withText(activityRule.activity.getString(R.string.TOAST_STRING_SAVED)))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))

    }

    @Test
    fun saveUserInfo_emailError() {
        // Type text and then press the button.
        onView(withId(R.id.editTextName)).perform(clearText())
        onView(withId(R.id.editTextName)).perform(typeText("Shahriyar"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(clearText())
        onView(withId(R.id.editTextEmail)).perform(typeText("shahriyar@aghajani"), closeSoftKeyboard())
        onView(withId(R.id.buttonSave)).perform(click())
        onView(withId(R.id.buttonSave)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonSave)).check(matches(withText(stringToBetyped)))

        onView(withId(R.id.editTextEmail)).check(matches(hasErrorText(activityRule.activity.getString(R.string.EMAIL_ERROR))))
    }
}
