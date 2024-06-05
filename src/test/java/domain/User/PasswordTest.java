package domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void checkPasswords_shouldReturnTrue_WhenPasswordsAreTheSame() {
        try {
            // When
            Password password = new Password("spengergasse".toCharArray());
            // Then
            assertTrue(Password.checkPasswords("spengergasse".toCharArray(), password.getHashedPassword()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void checkPasswords_shouldReturnFalse_WhenPasswordsAreNotTheSame() {
        try {
            // When
            Password password = new Password("spengergasse".toCharArray());
            // Then
            assertFalse(Password.checkPasswords("spenggergasse".toCharArray(), password.getHashedPassword()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void isPasswordStrong_shouldReturnFalse_WhenPasswordIsWeak() {
        try {
            // When
            // Then
            assertFalse(Password.isPasswordStrong("test".toCharArray()));
            assertFalse(Password.isPasswordStrong("password".toCharArray()));
            assertFalse(Password.isPasswordStrong("admin".toCharArray()));
            assertFalse(Password.isPasswordStrong("Pa$$w0rd123!".toCharArray()));
            assertFalse(Password.isPasswordStrong("Test1234!".toCharArray()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void isPasswordStrong_shouldReturnTrue_WhenPasswordIsStrong() {
        try {
            // When
            // Then
            assertTrue(Password.isPasswordStrong("spengergasse".toCharArray()));
            assertTrue(Password.isPasswordStrong("wizueggwehuiweiodfgvhn".toCharArray()));
            assertTrue(Password.isPasswordStrong("G7y!8@kL2#9Bx^dQ".toCharArray()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void hashPassword_shouldNotBeSameHash_WhenCreatingTheSamePasswordMultipleTimes() {
        try {
            // When
            char[] password1 = new Password("spengergasse".toCharArray()).getHashedPassword();
            char[] password2 = new Password("spengergasse".toCharArray()).getHashedPassword();
            char[] password3 = new Password("spengergasse".toCharArray()).getHashedPassword();
            // Then
            assertNotEquals(password1, password2);
            assertNotEquals(password1, password3);
            assertNotEquals(password2, password3);
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void passwordClearing_shouldBeCleared_WhenInstancingNewPassword() {
        try {
            // When
            char[] rawPassword = "wizueggwehuiweiodfgvhn!".toCharArray();
            Password password = new Password(rawPassword);

            // Then
            for (char c : rawPassword) {
                assertEquals(0, c);
            }
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }

    @Test
    void passwordClearing_shouldBeCleared_WhenCallingSetHashedPassword() {
        try {
            // When
            Password password = new Password("G7y!8@kL2#9Bx^dQ".toCharArray());
            char[] rawPassword2 = "G7y!8@kL2#9Bx^dQ".toCharArray();
            password.setHashedPassword(rawPassword2);

            // Then
            for (char c : rawPassword2) {
                assertEquals(0, c);
            }
        } catch (Exception e) {
            System.out.println("Unexpected Exception: " + e.getMessage());
            fail();
        }
    }
}