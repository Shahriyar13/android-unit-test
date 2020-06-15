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

        // نوشته هارا از ورودی ها بگیر
        val name = editTextName.text.toString()
        val dateOfBirth = Calendar.getInstance()
        dateOfBirth[datePickerBirthday.year, datePickerBirthday.month] = datePickerBirthday.dayOfMonth
        val email = editTextEmail.text.toString()

        // اگر مقدار نام اشتباه بودن ارور نشون بده
        if (name.isEmpty()) {
            editTextName.error = getString(R.string.NAME_ERROR)
            Log.w(TAG, "Not saving personal information: Invalid name")
            return
        }

        // اگر مقدار تاریخ تولد بزرگتر از تاریخ الان بود ارور نشون بده
        val calendarNow = Calendar.getInstance()
        if (datePickerBirthday.year >= calendarNow.get(Calendar.YEAR)) {
            //اگه سال بزرگتر از امسال بود
            if (datePickerBirthday.year > calendarNow.get(Calendar.YEAR)) {
                Toast.makeText(this, getString(R.string.BIRTHDAY_ERROR), Toast.LENGTH_LONG).show()
                Log.w(TAG, "Not saving personal information: Invalid birthday year")
                return
                //اگه سال یکی بود ولی ماه بزرگتر بود
            } else if (datePickerBirthday.month >= calendarNow.get(Calendar.MONTH)) {
                if (datePickerBirthday.month > calendarNow.get(Calendar.MONTH)) {
                    Toast.makeText(this, getString(R.string.BIRTHDAY_ERROR), Toast.LENGTH_LONG).show()
                    Log.w(TAG, "Not saving personal information: Invalid birthday month")
                    return
                    //اگه سال و ماه یکی بود ولی روز بزرگتر بود
                } else if (datePickerBirthday.dayOfMonth > calendarNow.get(Calendar.DAY_OF_MONTH)) {
                    Toast.makeText(this, getString(R.string.BIRTHDAY_ERROR), Toast.LENGTH_LONG).show()
                    Log.w(TAG, "Not saving personal information: Invalid birthday day")
                    return
                }
            }
        }

        // اگر مقدار ایمیل اشتباه بودن ارور نشون بده
        if (!mEmailValidator.isValid()) {
            editTextEmail.error = getString(R.string.EMAIL_ERROR)
            Log.w(TAG, "Not saving personal information: Invalid email")
            return
        }

        // مدل ذخیره سازی بساز
        val sharedPreferenceEntry =
                SharedPreferenceEntry(name, dateOfBirth, email)

        // ذخیره کن و چک کن که ذخیره شده
        val isSuccess: Boolean =
                mSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess) {
            Toast.makeText(this, getString(R.string.TOAST_STRING_SAVED), Toast.LENGTH_LONG).show()
            Log.i(TAG, getString(R.string.TOAST_STRING_SAVED))
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    /**
     * وقتی روی دکمه پاک کردن کلیک شود این متد صدا زده میشود
     */
    fun onRevertClick(view: View) {
        populateUi()
        Toast.makeText(this, getString(R.string.TOAST_STRING_REVERTED), Toast.LENGTH_LONG).show()
        Log.i(TAG, getString(R.string.TOAST_STRING_REVERTED))
    }
}