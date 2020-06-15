package com.example.ayusch

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
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

    private lateinit var stringToBeTyped: String

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private lateinit var editTextName: ViewInteraction
    private lateinit var editTextEmail: ViewInteraction
    private lateinit var buttonSave: ViewInteraction
    private lateinit var datePickerBirthday: ViewInteraction

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBeTyped = "Save"
        editTextName = onView(withId(R.id.editTextName))
        editTextEmail = onView(withId(R.id.editTextEmail))
        buttonSave = onView(withId(R.id.buttonSave))
        datePickerBirthday = onView(withId(R.id.datePickerBirthday))
    }

    @Test
    fun saveUserInfo_success() {
        // Type text and then press the button.
        editTextName.perform(clearText())
        editTextName.perform(typeText("Shahriyar"), closeSoftKeyboard())
        datePickerBirthday.perform(PickerActions.setDate(1993,5,2))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText("shahriyar@aghajani.com"), closeSoftKeyboard())
        buttonSave.perform(click())
        buttonSave.check(matches(isDisplayed()))
        buttonSave.check(matches(withText(stringToBeTyped)))

        onView(withText(activityRule.activity.getString(R.string.TOAST_STRING_SAVED)))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun saveUserInfo_nameError() {
        // Type text and then press the button.
        editTextName.perform(clearText())
        datePickerBirthday.perform(PickerActions.setDate(1993,5,2))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText("shahriyar@aghajani.com"), closeSoftKeyboard())
        buttonSave.perform(click())
        buttonSave.check(matches(isDisplayed()))
        buttonSave.check(matches(withText(stringToBeTyped)))

        editTextName.check(matches(hasErrorText(activityRule.activity.getString(R.string.NAME_ERROR))))
    }

    @Test
    fun saveUserInfo_dateError() {
        // Type text and then press the button.
        editTextName.perform(clearText())
        editTextName.perform(typeText("Shahriyar"), closeSoftKeyboard())
        datePickerBirthday.perform(PickerActions.setDate(2900,7,16))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText("shahriyar@aghajani.com"), closeSoftKeyboard())
        buttonSave.perform(click())
        buttonSave.check(matches(isDisplayed()))
        buttonSave.check(matches(withText(stringToBeTyped)))

        onView(withText(activityRule.activity.getString(R.string.BIRTHDAY_ERROR)))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun saveUserInfo_emailError() {
        // Type text and then press the button.
        editTextName.perform(clearText())
        editTextName.perform(typeText("Shahriyar"), closeSoftKeyboard())
        datePickerBirthday.perform(PickerActions.setDate(1993,5,2))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText("shahriyar@aghajani"), closeSoftKeyboard())
        buttonSave.perform(click())
        buttonSave.check(matches(isDisplayed()))
        buttonSave.check(matches(withText(stringToBeTyped)))

        editTextEmail.check(matches(hasErrorText(activityRule.activity.getString(R.string.EMAIL_ERROR))))
    }
}
