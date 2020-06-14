package com.example.ayusch

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        // Logger for this class.
        private const val TAG = "MainActivity"
    }

    // The helper that manages writing to SharedPreferences.
    private lateinit var mSharedPreferencesHelper: SharedPreferencesHelper

    // The validator for the email input field.
    private lateinit var mEmailValidator: EmailValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup field validators.
        mEmailValidator = EmailValidator()
        emailInput.addTextChangedListener(mEmailValidator)

        // Instantiate a SharedPreferencesHelper.
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        mSharedPreferencesHelper = SharedPreferencesHelper(sharedPreferences)

        // Fill input fields from data retrieved from the SharedPreferences.
        populateUi();
    }

    private fun populateUi() {
        val sharedPreferenceEntry: SharedPreferenceEntry = mSharedPreferencesHelper.getPersonalInfo()

        userNameInput.setText(sharedPreferenceEntry.name)
        val dateOfBirth: Calendar = sharedPreferenceEntry.dateOfBirth
        dateOfBirthInput.init(
            dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH),
            dateOfBirth.get(Calendar.DAY_OF_MONTH), null
        )
        emailInput.setText(sharedPreferenceEntry.email)
    }

    /**
     * Called when the "Save" button is clicked.
     */
    fun onSaveClick(view: View) {
        // Don't save if the fields do not validate.
        if (!mEmailValidator.isValid()) {
            emailInput.error = "Invalid email"
            Log.w(Companion.TAG, "Not saving personal information: Invalid email")
            return
        }

        // Get the text from the input fields.
        val name = userNameInput.text.toString()
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth[dateOfBirthInput.year, dateOfBirthInput.month] = dateOfBirthInput.dayOfMonth
        val email = emailInput.text.toString()

        // Create a Setting model class to persist.
        val sharedPreferenceEntry =
            SharedPreferenceEntry(name, dateOfBirth, email)

        // Persist the personal information.
        val isSuccess: Boolean =
            mSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show()
            Log.i(Companion.TAG, "Personal information saved")
        } else {
            Log.e(Companion.TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    /**
     * Called when the "Revert" button is clicked.
     */
    fun onRevertClick(view: View) {
        populateUi();
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show();
        Log.i(Companion.TAG, "Personal information reverted");
    }
}
