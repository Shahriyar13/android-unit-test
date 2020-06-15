package com.example.ayusch

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
         * @param email The email to validate.
         * @return `true` if the input is a valid email. `false` otherwise.
         */
        fun isValidEmail(email: CharSequence?) =
            email != null && EMAIL_PATTERN.matcher(email).matches()
    }

    private var mIsValid = false

    fun isValid() = mIsValid

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