# **یونیت تست در اندروید با کاتلین**

ابتدا لایبراری های زیر را اضافه کنید:

    //test libraries
    testImplementation 'junit:junit:4.13'
    testImplementation 'org.mockito:mockito-core:3.3.3'


***

* Junit
> .برای نوشتن تست هایی که فقط به جاوا یا کاتلین نیاز دارند و روی خود کامپایلر قابل اجرا هستند و نیازی به هیچ دستگاه دیگر برای اجرای تست ها ندارند
>
> دارای انوتیشن های زیر است:
> 1. @Test
> 1. @Before
> 1. @After


* Mockito
> .برا یاجرای تست های نوشته شده به یک دستگاه نیاز دارند
>
> دارای انوتیشن های زیر است:
> 1. @Mock


***

> برای شروع یک اپلیکیشن جدید بسازید
>
> تمام این مراحل رو به ترتیب انجام بدید و با هم جلو بریم
>
> میخوایم یه اپ ساده درست کنیم که نام و تاریخ تولد و ایمیل رو میگیره و ذخیره میکنه
>
> این رو به صفحه اصلی اضافه کنید 


<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginBottom="16dp"
            android:text="@string/settings_title"
            android:textAppearance="?android:attr/textAppearanceLarge" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="start"
            android:layout_marginTop="16dp"
            android:text="نام"
            android:textAppearance="?android:attr/textAppearanceMedium" />

        <EditText
            android:id="@+id/editTextName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:hint="نام..." />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="start"
            android:layout_marginTop="16dp"
            android:text="تاریخ تولد"
            android:textAppearance="?android:attr/textAppearanceMedium" />

        <DatePicker
            android:id="@+id/datePickerBirthday"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:calendarViewShown="false"
            android:datePickerMode="spinner"
            android:inputType="text|date"
            tools:targetApi="lollipop" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="start"
            android:layout_marginTop="8dp"
            android:text="ایمیل"
            android:textAppearance="?android:attr/textAppearanceMedium" />

        <EditText
            android:id="@+id/editTextEmail"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:hint="ایمیل..."
            android:inputType="textEmailAddress" />

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="16dp"
            android:orientation="horizontal">

            <Button
                android:id="@+id/buttonSave"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center_horizontal"
                android:layout_marginEnd="16dp"
                android:layout_marginStart="16dp"
                android:onClick="onSaveClick"
                android:text="ذخیره"
                android:textSize="16sp" />

            <Button
                android:id="@+id/buttonRevert"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center_horizontal"
                android:onClick="onRevertClick"
                android:text="پاک کردن"
                android:textSize="16sp" />

        </LinearLayout>

    </LinearLayout>
</ScrollView>



***


کد های صفحه اصلی:



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



***

مدل ذخیره شونده
SharedPreferenceEntry


    import java.util.*
    
    data class SharedPreferenceEntry(
        val name: String,
        val dateOfBirth: Calendar,
        val email: String
    )


***



کلاس ذخیره ساز
SharedPreferencesHelper


    
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


***


چک کننده ایمیل
EmailValidator

    
    import android.text.Editable
    import android.text.TextWatcher
    import java.util.regex.Pattern
    
    
    /**
     * An Email format validator for {@link android.widget.EditText}.
     */
    class EmailValidator: TextWatcher {
    
        companion object {
            /**
             * Email validation pattern.
             */
            private val EMAIL_PATTERN: Pattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
            )
    
            /**
             * Validates if the given input is a valid email address.
             *
             * @param email        The email to validate.
             * @return `true` if the input is a valid email. `false` otherwise.
             */
            fun isValidEmail(email: CharSequence?): Boolean {
                return email != null && EMAIL_PATTERN.matcher(email).matches()
            }
        }
    
        private var mIsValid = false
    
        fun isValid(): Boolean {
            return mIsValid
        }
        
        override fun afterTextChanged(editableText: Editable?) {
            mIsValid = isValidEmail(editableText)
        }
    
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }
    
        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }
    
    }


***


خب حالا بریم سر
### اصل مطلب

> حالا دوجا میشه تست نوشت
> 1. androidTest
> 1. test
>
> اولی برای تست های اندروید و دومی برای تست های جاوا و کاتلین
>
> که ما ابتدا در جای دوم ینی
>
> test
> app\src\test\java\{yourPackageName}\
>
> شروع میکنیم
>

کلاس های زیر رو برای تست نویسی میسازیم

***

EmailValidatorTest


    import junit.framework.TestCase.assertFalse
    import org.junit.Assert.assertTrue
    import org.junit.Test
    
    /**
     * Unit tests for the EmailValidator logic.
     */
    class EmailValidatorTest {
    
        @Test
        fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
            assertTrue(EmailValidator.isValidEmail("name@email.com"))
        }
    
        @Test
        fun emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
            assertTrue(EmailValidator.isValidEmail("name@email.co.uk"))
        }
    
        @Test
        fun emailValidator_InvalidEmailNoTld_ReturnsFalse() {
            assertFalse(EmailValidator.isValidEmail("name@email"))
        }
    
        @Test
        fun emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
            assertFalse(EmailValidator.isValidEmail("name@email..com"))
        }
    
        @Test
        fun emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
            assertFalse(EmailValidator.isValidEmail("@email.com"))
        }
    
        @Test
        fun emailValidator_EmptyString_ReturnsFalse() {
            assertFalse(EmailValidator.isValidEmail(""))
        }
    
        @Test
        fun emailValidator_NullEmail_ReturnsFalse() {
            assertFalse(EmailValidator.isValidEmail(null))
        }
    }

با دقت چندبار به محتوی این کلاس نگاه کن که داره چیکار میکنه
1. کلاس بررسی کننده درستی ایمیل رو نگاه کن یه متد داشت که میگفت این ایمیل درسته یا غلط
1. حالا اینجارو ببین در متد اول 1 آدرس ایمیل درست داده و میخواد تست کنه که اون متد میگه این آدرس درست هست یا نه
1. دوباره تو متد دوم یه آدرس ایمیل درست دیگه داده که ظاهرش یکم عجیب تر از قبلی هست و اینجا هم انتظار داره که اون متد بگه این آدرس هم درسته
1. در 3تا متد بعدی آدرس غلط داده و دقت کنید که منتظره این متد بگه غلطه این آدرس ها
> 
> توجه کنید که این تست وقتی ÷اس میشه که مقدار غلط یا فالس برگرده
> 
> assertFalse
>
> یعنی انتظار داره که بگه فالس
1. در دو متد بعدی هم اومده آدرس خالی و نال داده و منتظره بگه غلطه نه این که برنامه کرش کنه!!!


***

> تست نویسی به همین سادگی بود
>
> چندبار نگاهش کنو برو برای یکی از کلاسات بنویس تا قشنگ دستت بیاد
>
> دقت کن به نام گذاری کلاس ها و متد ها
>
> کلاس ها هم نام با کلاسی که براش تست مینویسیم فقط تهش یه کلمه تست اضافه میکنیم و متد ها هم که نگاه کنید اسم گذاریشونو


***


حالا برای کلاس ذخیره ساز هم بنویسیم یکم سخت تر


***


SharedPreferencesHelperTest
> این یکی به ماکیاتو نیاز داره 

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

دیگه این طولانی بود حال ندارم بنویسم میدونم که خودت بخونیش میفهمی

`موفق باشید`

**میتونید از گیت کلون بگیرید و پروژه رو خوش آب و رنگتر توی اندروید استدیو ببینید**


***

> حالا بریم سراغ تست های اندروید
> 1. androidTest
> 1. test
>
> یعنی اولی
>
> androidTest
> app\src\androidTest\java\{yourPackageName}\
>
> شروع میکنیم
>
> برای تست اتفاقات صفحه اصلی
>
>
> ابتدا روی گوشی که قراره تست ها روش انجام بشه یه کاری انجام بدید
>
> به قسمت توسعه دهنده ها توی تنظیمات برید و تمام زمان نمایش انیمیشن ها رو صفر کنید

یک کلاس برای تست صفحه اصلی میسازیم

MainActivityTest 

    
    import android.app.Activity
    import android.content.Context
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
    import org.junit.Before
    import org.junit.Rule
    import org.junit.Test
    import org.junit.runner.RunWith
    import org.hamcrest.Matchers.`is`
    import org.hamcrest.Matchers.not
    
    /**
     * Instrumented test, which will execute on an Android device.
     *
     * See [testing documentation](http://d.android.com/tools/testing).
     */
    @RunWith(AndroidJUnit4::class)
    class MainActivityTest {
    
        private lateinit var appContext: Context
        private lateinit var activity: Activity
        private lateinit var editTextName: ViewInteraction
        private lateinit var editTextEmail: ViewInteraction
        private lateinit var buttonSave: ViewInteraction
        private lateinit var datePickerBirthday: ViewInteraction
        private lateinit var validName: String
        private lateinit var validEmail: String
        private lateinit var inValidEmail: String
    
        @get:Rule
        var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    
        @Before
        fun initValidString() {
            appContext = InstrumentationRegistry.getInstrumentation().targetContext
            activity = activityRule.activity
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
            datePickerBirthday.perform(PickerActions.setDate(1993, 5, 2))
            editTextEmail.perform(clearText())
            editTextEmail.perform(typeText(validEmail), closeSoftKeyboard())
            buttonSave.perform(click())
    
            editTextName.check(matches(hasErrorText(appContext.getString(R.string.NAME_ERROR))))
        }
    
        @Test
        fun saveUserInfo_dateError() {
            editTextName.perform(clearText())
            editTextName.perform(typeText(validName), closeSoftKeyboard())
            datePickerBirthday.perform(PickerActions.setDate(2900, 7, 16))
            editTextEmail.perform(clearText())
            editTextEmail.perform(typeText(validEmail), closeSoftKeyboard())
            buttonSave.perform(click())
    
            onView(withText(appContext.getString(R.string.BIRTHDAY_ERROR)))
                .inRoot(withDecorView(not(`is`(activity.window.decorView))))
                .check(matches(isDisplayed()))
        }
    
        @Test
        fun saveUserInfo_emailError() {
            editTextName.perform(clearText())
            editTextName.perform(typeText(validName), closeSoftKeyboard())
            datePickerBirthday.perform(PickerActions.setDate(1993, 5, 2))
            editTextEmail.perform(clearText())
            editTextEmail.perform(typeText(inValidEmail), closeSoftKeyboard())
            buttonSave.perform(click())
    
            editTextEmail.check(matches(hasErrorText(appContext.getString(R.string.EMAIL_ERROR))))
        }
    
        @Test
        fun saveUserInfo_success() {
            editTextName.perform(clearText())
            editTextName.perform(typeText(validName), closeSoftKeyboard())
            datePickerBirthday.perform(PickerActions.setDate(1993, 5, 2))
            editTextEmail.perform(clearText())
            editTextEmail.perform(typeText(validEmail), closeSoftKeyboard())
            buttonSave.perform(click())
    
            onView(withText(appContext.getString(R.string.TOAST_STRING_SAVED)))
                .inRoot(withDecorView(not(`is`(activity.window.decorView))))
                .check(matches(isDisplayed()))
        }
    }


> در ابتدا به کلاس انوتیشین زیر را میدهیم
>
> @RunWith(AndroidJUnit4::class)
>
> بعد این رو توی کلاس اضافه کردیم که بدونه کدوم اکتیویتی داره تست میشه
>


    @get:Rule
        var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

>
> حالا ویو های صفحه اصلی را ‍یدا میکنیم و مقدار میدیم

        @Before
        fun initValidString() {
            appContext = InstrumentationRegistry.getInstrumentation().targetContext
            activity = activityRule.activity
            validName = "Shahriyar Aghajani"
            validEmail = "Shahriyar@Aghajani.com"
            inValidEmail = "Shahriyar@Aghajani"
            editTextName = onView(withId(R.id.editTextName))
            editTextEmail = onView(withId(R.id.editTextEmail))
            buttonSave = onView(withId(R.id.buttonSave))
            datePickerBirthday = onView(withId(R.id.datePickerBirthday))
        }


>
>


    buttonSave.check(matches(isDisplayed()))
    buttonSave.check(matches(withText("Save")))



> اینجا از چند دستور ساده استفاده شده
>
* check
> میاد چیزی که بهش میگیمو توی اون ویو امتحان میکنه که درسته یا نه
* matches
> از اسمش واضحه که مثل مساوی میمونه
* isDisplayed
> آیا ویزیبل هست
* withText
> آیا متنش اینه: --- ؟


    editTextName.perform(clearText())


* perform
>  یک کاری رو روی اون ویو اعمال میکنه
* clearText()
> مانند دستور زیر عمل میکنه
>
> editTextName.setText("")


    editTextName.perform(typeText(validName), closeSoftKeyboard())


* typeText
> مقدار آرگومانی که بهش میدیمو داخل ادیت تکست تایپ میکنه
* closeSoftKeyboard()
> صفحه کلید رو میبنده بعد تایپ کردن


* click()
> کلیک میکنه

    onView(withText(appContext.getString(R.string.TOAST_STRING_SAVED)))
                .inRoot(withDecorView(not(`is`(activity.window.decorView))))
                .check(matches(isDisplayed()))

متن توست نمایش داده شده رو بررسی میکنه


    editTextEmail.check(matches(hasErrorText(appContext.getString(R.string.EMAIL_ERROR))))


متن ارور نشان داده شده روی این ویو رو بررسی میکنه
