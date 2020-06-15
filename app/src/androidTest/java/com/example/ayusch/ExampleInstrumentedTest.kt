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
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.ayusch", appContext.packageName)
    }

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private lateinit var editTextName: ViewInteraction
    private lateinit var editTextEmail: ViewInteraction
    private lateinit var buttonSave: ViewInteraction
    private lateinit var datePickerBirthday: ViewInteraction
    private lateinit var validName: String
    private lateinit var validEmail: String
    private lateinit var inValidEmail: String

    @Before
    fun initValidString() {
        validName = "Shahriyar Aghajani"
        validEmail = "Shahriyar@Aghajani.com"
        inValidEmail = "Shahriyar@Aghajani"
        editTextName = onView(withId(R.id.editTextName))
        editTextEmail = onView(withId(R.id.editTextEmail))
        buttonSave = onView(withId(R.id.buttonSave))
        datePickerBirthday = onView(withId(R.id.datePickerBirthday))
    }

    @Test
    fun saveUserInfo_availableSaveButton() {
        buttonSave.check(matches(isDisplayed()))
        buttonSave.check(matches(withText("Save")))
    }

    @Test
    fun saveUserInfo_nameError() {
        editTextName.perform(clearText())
        datePickerBirthday.perform(PickerActions.setDate(1993,5,2))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText(validEmail), closeSoftKeyboard())
        buttonSave.perform(click())

        editTextName.check(matches(hasErrorText(activityRule.activity.getString(R.string.NAME_ERROR))))
    }

    @Test
    fun saveUserInfo_dateError() {
        editTextName.perform(clearText())
        editTextName.perform(typeText(validName), closeSoftKeyboard())
        datePickerBirthday.perform(PickerActions.setDate(2900,7,16))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText(validEmail), closeSoftKeyboard())
        buttonSave.perform(click())

        onView(withText(activityRule.activity.getString(R.string.BIRTHDAY_ERROR)))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun saveUserInfo_emailError() {
        editTextName.perform(clearText())
        editTextName.perform(typeText(validName), closeSoftKeyboard())
        datePickerBirthday.perform(PickerActions.setDate(1993,5,2))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText(inValidEmail), closeSoftKeyboard())
        buttonSave.perform(click())

        editTextEmail.check(matches(hasErrorText(activityRule.activity.getString(R.string.EMAIL_ERROR))))
    }

    @Test
    fun saveUserInfo_success() {
        editTextName.perform(clearText())
        editTextName.perform(typeText(validName), closeSoftKeyboard())
        datePickerBirthday.perform(PickerActions.setDate(1993,5,2))
        editTextEmail.perform(clearText())
        editTextEmail.perform(typeText(validEmail), closeSoftKeyboard())
        buttonSave.perform(click())

        onView(withText(activityRule.activity.getString(R.string.TOAST_STRING_SAVED)))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }
}