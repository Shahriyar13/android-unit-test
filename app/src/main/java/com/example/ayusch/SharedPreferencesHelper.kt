package com.example.ayusch

import android.content.SharedPreferences
import java.util.*

/**
 * Helper class to manage access to {@link SharedPreferences}.
 *
 * Constructor with dependency injection.
 * @param sharedPreferences The [SharedPreferences] that will be used in this DAO.
 */
class SharedPreferencesHelper(private val sharedPreferences: SharedPreferences) {

    enum class Key(val value: String) {
        KEY_NAME("key_name"),
        KEY_DOB("key_dob_millis"),
        KEY_EMAIL("key_email")
    }

    /**
     * Saves the given [SharedPreferenceEntry] that contains the user's settings to
     * [SharedPreferences].
     *
     * @param sharedPreferenceEntry contains data to save to [SharedPreferences].
     * @return `true` if writing to [SharedPreferences] succeeded. `false`
     * otherwise.
     */
    fun savePersonalInfo(sharedPreferenceEntry: SharedPreferenceEntry): Boolean {
        // Start a SharedPreferences transaction.
        val editor = sharedPreferences.edit()
        editor.putString(Key.KEY_NAME.value, sharedPreferenceEntry.name)
        editor.putLong(Key.KEY_DOB.value, sharedPreferenceEntry.dateOfBirth.timeInMillis)
        editor.putString(Key.KEY_EMAIL.value, sharedPreferenceEntry.email)
        // Commit changes to SharedPreferences.
        return editor.commit()
    }

    /**
     * Retrieves the [SharedPreferenceEntry] containing the user's personal information from
     * [SharedPreferences].
     *
     * @return the Retrieved [SharedPreferenceEntry].
     */
    fun getPersonalInfo(): SharedPreferenceEntry {
        // Get data from the SharedPreferences.
        val name = sharedPreferences.getString(Key.KEY_NAME.value, "") ?: ""
        val dobMillis =
            sharedPreferences.getLong(Key.KEY_DOB.value, Calendar.getInstance().timeInMillis)
        val dateOfBirth: Calendar = Calendar.getInstance()
        dateOfBirth.timeInMillis = dobMillis
        val email = sharedPreferences.getString(Key.KEY_EMAIL.value, "") ?: ""
        // Create and fill a SharedPreferenceEntry model object.
        return SharedPreferenceEntry(name, dateOfBirth, email)
    }
}