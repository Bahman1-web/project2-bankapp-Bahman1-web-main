package edu.brooklyn.project2;

/*
 * JUnit tests work by performing some **action under test** and then
 * *asserting** that the reult of the action matches the expected outcome. If all
 * assertions succeed and no other exceptions are thrown, the test will pass. If
 * the assertion fails, an AssertionError will be thrown, and the test will be
 * marked as failing.
 *
 * JUnit provides a number of custom assertion methods that include detailed
 * information about why the assertion failed in their message.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordUtilsTests {
    private final String password = "password";

    // Every method marked with the @Test annotation will be **gathered** as a test and run by JUnit.
    @Test
    public void hashIsNotPassword() {
        assertNotEquals(password, PasswordUtils.getPasswordHash(password));
    }

    @Test
    public void getDeterministicHash() {
        assertEquals(PasswordUtils.getPasswordHash(password),
            PasswordUtils.getPasswordHash(password));
    }

    @Test
    public void uniqueHash() {
        assertNotEquals(PasswordUtils.getPasswordHash("password"),
            PasswordUtils.getPasswordHash("Password"));
    }

    @Test
    @DisplayName("alphanumericHash: Expect hash to only contain alphanumeric characters")
    public void alphanumericHash() {
        final String hash = PasswordUtils.getPasswordHash(password);
        // String.matches performs a regular expression match
        assertTrue(hash.matches("[A-Za-z0-9]+"));
    }

    @Test
    public void readPasswordFromScanner() {
        final Scanner testInput = new Scanner(password);
        assertEquals(password, PasswordUtils.readPassword(testInput));
    }
}
