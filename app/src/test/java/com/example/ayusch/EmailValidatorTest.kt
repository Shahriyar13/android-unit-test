package com.example.ayusch

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