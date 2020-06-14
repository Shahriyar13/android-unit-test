package com.example.ayusch

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

/**
 * Unit tests for the {@link SharedPreferencesHelper} that mocks {@link SharedPreferences}.
 */
@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {

    companion object {

        private const val TEST_NAME = "Test name"

        private const val TEST_EMAIL = "test@email.com"

        private val TEST_DATE_OF_BIRTH: Calendar = Calendar.getInstance()

        private var mSharedPreferenceEntry: SharedPreferenceEntry? = null

        private var mMockSharedPreferencesHelper: SharedPreferencesHelper? = null

        private var mMockBrokenSharedPreferencesHelper: SharedPreferencesHelper? = null

    }

    @Mock
    var mMockSharedPreferences: SharedPreferences? = null

    @Mock
    var mMockBrokenSharedPreferences: SharedPreferences? = null

    @Mock
    var mMockEditor: Editor? = null

    @Mock
    var mMockBrokenEditor: Editor? = null

    @Before
    fun initMocks() {
        TEST_DATE_OF_BIRTH.set(1980, 1, 1)

        // Create SharedPreferenceEntry to persist.
        mSharedPreferenceEntry = SharedPreferenceEntry(
            TEST_NAME, TEST_DATE_OF_BIRTH,
            TEST_EMAIL
        )
        // Create a mocked SharedPreferences.
        mMockSharedPreferencesHelper = createMockSharedPreference()
        // Create a mocked SharedPreferences that fails at saving data.
        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference()
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences
        val success =
            mMockSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        assertThat(
            "Checking that SharedPreferenceEntry.save... returns true",
            success, `is`(true)
        )
        // Read personal information from SharedPreferences
        val (name, dateOfBirth, email) = mMockSharedPreferencesHelper!!.getPersonalInfo()
        // Make sure both written and retrieved personal information are equal.
        assertThat(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            mSharedPreferenceEntry!!.name,
            `is`(equalTo(name))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.dateOfBirth,
            `is`(equalTo(dateOfBirth))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.email has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry!!.email,
            `is`(equalTo(email))
        )
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        // Read personal information from a broken SharedPreferencesHelper
        val success =
            mMockBrokenSharedPreferencesHelper!!.savePersonalInfo(mSharedPreferenceEntry!!)
        assertThat(
            "Makes sure writing to a broken SharedPreferencesHelper returns false", success,
            `is`(false)
        )
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private fun createMockSharedPreference(): SharedPreferencesHelper? {
        // Mocking reading the SharedPreferences as if mMockSharedPreferences was previously written
        // correctly.
        `when`(
            mMockSharedPreferences!!.getString(
                eq(SharedPreferencesHelper.KEY_NAME),
                anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.name)
        `when`(
            mMockSharedPreferences!!.getString(
                eq(SharedPreferencesHelper.KEY_EMAIL),
                anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.email)
        `when`(
            mMockSharedPreferences!!.getLong(
                eq(SharedPreferencesHelper.KEY_DOB),
                anyLong()
            )
        )
            .thenReturn(mSharedPreferenceEntry!!.dateOfBirth.timeInMillis)
        // Mocking a successful commit.
        `when`(mMockEditor!!.commit()).thenReturn(true)
        // Return the MockEditor when requesting it.
        `when`(mMockSharedPreferences!!.edit()).thenReturn(mMockEditor)
        return SharedPreferencesHelper(mMockSharedPreferences!!)
    }

    /**
     * Creates a mocked SharedPreferences that fails when writing.
     */
    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper? {
        // Mocking a commit that fails.
        `when`(mMockBrokenEditor!!.commit()).thenReturn(false)
        // Return the broken MockEditor when requesting it.
        `when`(mMockBrokenSharedPreferences!!.edit()).thenReturn(mMockBrokenEditor)
        return SharedPreferencesHelper(mMockBrokenSharedPreferences!!)
    }

}