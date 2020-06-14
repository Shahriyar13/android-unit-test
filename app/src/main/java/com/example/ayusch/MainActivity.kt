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
        // برای استفاده در لاگ ها
        private const val TAG = "MainActivity"
    }

    // برای مدیریت ذخیره کردن متن ها
    private lateinit var mSharedPreferencesHelper: SharedPreferencesHelper

    // برای بررسی درست بودن آدرس ایمیل
    private lateinit var mEmailValidator: EmailValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // مقداردهی
        mEmailValidator = EmailValidator()
        editTextEmail.addTextChangedListener(mEmailValidator)

        // مقداردهی
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        mSharedPreferencesHelper = SharedPreferencesHelper(sharedPreferences)

        // اگر مقداری قبلا ذخیره شده بود نمایش میدهد
        populateUi();
    }

    private fun populateUi() {
        val sharedPreferenceEntry: SharedPreferenceEntry = mSharedPreferencesHelper.getPersonalInfo()

        editTextName.setText(sharedPreferenceEntry.name)
        val dateOfBirth: Calendar = sharedPreferenceEntry.dateOfBirth
        datePickerBirthday.init(
                dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH),
                dateOfBirth.get(Calendar.DAY_OF_MONTH), null
        )
        editTextEmail.setText(sharedPreferenceEntry.email)
    }

    /**
     * وقتی روی دکمه ذخیره کلیک شود این متد صدا زده میشود
     */
    fun onSaveClick(view: View) {
        // Don't save if the fields do not validate.
        if (!mEmailValidator.isValid()) {
            editTextEmail.error = "Invalid email"
            Log.w(TAG, "Not saving personal information: Invalid email")
            return
        }

        // نوشته هارا از ورودی ها بگیر
        val name = editTextName.text.toString()
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth[datePickerBirthday.year, datePickerBirthday.month] = datePickerBirthday.dayOfMonth
        val email = editTextEmail.text.toString()

        // مدل ذخیره سازی بساز
        val sharedPreferenceEntry =
                SharedPreferenceEntry(name, dateOfBirth, email)

        // ذخیره کن و چک کن که ذخیره شده
        val isSuccess: Boolean =
                mSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Personal information saved")
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    /**
     * وقتی روی دکمه پاک کردن کلیک شود این متد صدا زده میشود
     */
    fun onRevertClick(view: View) {
        populateUi()
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show()
        Log.i(TAG, "Personal information reverted")
    }
}