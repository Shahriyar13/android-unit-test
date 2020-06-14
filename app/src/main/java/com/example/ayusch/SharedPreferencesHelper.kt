package com.example.ayusch

import android.content.SharedPreferences
import java.util.*

/**
 *  Helper class to manage access to {@link SharedPreferences}.
 */

/**
 * Constructor with dependency injection.
 *
 * @param sharedPreferences The [SharedPreferences] that will be used in this DAO.
 */
class SharedPreferencesHelper(private val sharedPreferences: SharedPreferences) {

    companion object {
        // Keys for saving values in SharedPreferences.
        const val KEY_NAME = "key_name"
        const val KEY_DOB = "key_dob_millis"
        const val KEY_EMAIL = "key_email"
    }

    /**
     * Saves the given [SharedPreferenceEntry] that contains the user's settings to
     * [SharedPreferences].
     *
     * @param sharedPreferenceEntry contains data to save to [SharedPreferences].
     * @return `true` if writing to [SharedPreferences] succeeded. `false`
     * otherwise.
     */
    fun savePersonalInfo(sharedPreferenceEntry: SharedPreferenceEntry): Boolean { // Start a SharedPreferences transaction.
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, sharedPreferenceEntry.name)
        editor.putLong(KEY_DOB, sharedPreferenceEntry.dateOfBirth.timeInMillis)
        editor.putString(KEY_EMAIL, sharedPreferenceEntry.email)
        // Commit changes to SharedPreferences.
        return editor.commit()
    }

    /**
     * Retrieves the [SharedPreferenceEntry] containing the user's personal information from
     * [SharedPreferences].
     *
     * @return the Retrieved [SharedPreferenceEntry].
     */
    fun getPersonalInfo(): SharedPreferenceEntry { // Get data from the SharedPreferences.
        val name = sharedPreferences.getString(KEY_NAME, "") ?: ""
        val dobMillis =
            sharedPreferences.getLong(KEY_DOB, Calendar.getInstance().timeInMillis)
        val dateOfBirth: Calendar = Calendar.getInstance()
        dateOfBirth.timeInMillis = dobMillis
        val email = sharedPreferences.getString(KEY_EMAIL, "") ?: ""
        // Create and fill a SharedPreferenceEntry model object.
        return SharedPreferenceEntry(name, dateOfBirth, email)
    }
}