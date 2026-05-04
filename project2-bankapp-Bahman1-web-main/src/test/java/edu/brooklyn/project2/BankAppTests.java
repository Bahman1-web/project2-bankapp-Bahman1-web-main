package edu.brooklyn.project2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class BankAppTests extends AbstractBankTests {
    @Test
    @DisplayName("accountMenu: Expect account menu when started")
    public void accountMenu() {
        responses = runMain(CHOOSE_EXIT);
        assertTrue(responses.size() > 0, "Expected output from BankApp, but got none.");
        assertEquals(
            String.format(
                "1. Log into bank account%n" +
                "2. Create bank account%n" +
                "0. Exit%n" +
                "Enter option: "),
            responses.get(0));
    }

    @Test
    @DisplayName("exitProgram: Expect option 0 to exit")
    public void exitProgram() {
        responses = runMain(CHOOSE_EXIT);
        assertTrue(responses.size() < 3, "Expected program to exit after EXIT option.");
        assertTrue(responses.size() > 1, "Expected message on exit, but got none.");
        assertEquals("Exit.%n".formatted(), responses.get(1));
    }

    @Test
    @DisplayName("unknownOption: Expect error message on unknown option")
    public void unknownOption() {
        responses = runMain("5", CHOOSE_EXIT);
        assertTrue(responses.size() > 1, "Expected output after input, but got none.");
        String errorMessage = firstLineOf(responses.get(1));
        assertEquals("Unknown option 5.", errorMessage);
    }


    @Test
    public void createAccountInterface() {
        String ssn = "123456789";
        String passwd = "password";
        String balance = "100.00";


        responses = runMain(CHOOSE_CREATE_ACCOUNT, ssn, passwd, balance, CHOOSE_EXIT);

        assertEquals(("Create new bank account.%n" +
                      "%n" +
                      "SSN: ").formatted(),
            responses.get(1));
        assertEquals("Password: ", responses.get(2));
        assertEquals("Balance: ", responses.get(3));
    }

    @Test
    @DisplayName("createAccount: Attempt to create a valid account")
    public void createAccount() {
        String ssn = "123456789";
        String passwd = "password";
        String balance = "100.00";

        responses = runMain(CHOOSE_CREATE_ACCOUNT, ssn, passwd, balance,
            CHOOSE_ACCOUNT_LOGIN, ssn, passwd,
            CHOOSE_BALANCE,
            CHOOSE_EXIT, CHOOSE_EXIT);

        assertEquals("Create new bank account.", firstLineOf(responses.get(1)));
        assertEquals("1. Log into bank account", firstLineOf(responses.get(4)));
        assertTrue(responses.get(7).endsWith("Enter option: "),
            "Expect successful login after creating account.");
        assertEquals("The balance is $100.00", firstLineOf(responses.get(8)));
    }

}
