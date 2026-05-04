package edu.brooklyn.project2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BankAppAccountTests extends AbstractBankTests {
    class TestAccount {
        private final String ssn;
        private final String password; // keep test passwords in plaintext
        private final BigDecimal balance;

        TestAccount(final String ssn, final String password, final String balance) {
            this.ssn = ssn;
            this.password = password;
            this.balance = new BigDecimal(balance);

        }
    }

    private final TestAccount[] accounts = {
        new TestAccount("123456781", "pw781", "123456789.00"),
        new TestAccount("123456782", "pw782", "12345678.90"),
        new TestAccount("123456783", "pw783", "1234567.89"),
        new TestAccount("123456784", "pw785", "123456.78"),
        new TestAccount("123456785", "pw785", "12345.67"),
        new TestAccount("123456786", "pw786", "1234.56"),
        new TestAccount("123456787", "pw787", "123.45"),
        new TestAccount("123456788", "pw788", "12.34"),
        new TestAccount("123456789", "pw789", "1.23"),
    };

    @BeforeEach
    public void loadAccounts() throws IOException {
        final Path accountsFile = tmpDir.resolve("accounts.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(accountsFile)) {
            for (final TestAccount account : accounts) {
                writeAccount(writer, account);
            }
        }
    }

    @Test
    public void accountLoginInterface() {
        TestAccount account = accounts[0];

        responses = runMain(CHOOSE_ACCOUNT_LOGIN, account.ssn, account.password, CHOOSE_EXIT, CHOOSE_EXIT);
        assertEquals("%nSSN: ".formatted(), responses.get(1));
        assertEquals("Password: ".formatted(), responses.get(2));
    }

    @Test
    @DisplayName("accountLoginOneFailure: Login successfully after one failure")
    public void accountLoginOneFailure() {
        TestAccount account = accounts[0];
        responses = runMain(
            CHOOSE_ACCOUNT_LOGIN, account.ssn, "incorrectPassword",
            account.ssn, account.password,
            CHOOSE_EXIT, CHOOSE_EXIT);
        assertEquals("Login incorrect.", firstLineOf(responses.get(3)));
        assertTrue(responses.get(3).endsWith("SSN: "));
        assertEquals("Password: ", firstLineOf(responses.get(4)));
        assertEquals("1. Check balance", firstLineOf(responses.get(5)));
    }

    @Test
    @DisplayName("accountLoginThreeFailures: Expect return to main menu after three attempts")
    public void accountLoginThreeFailures() {
        TestAccount account = accounts[0];
        responses = runMain(
            CHOOSE_ACCOUNT_LOGIN, account.ssn, "incorrectPassword",
            account.ssn, "otherIncorrectPassword",
            account.ssn, "otherIncorrectPassword",
            CHOOSE_EXIT);
        List<String> loginSection = linesIn(responses.subList(1, 8))
            .filter(Predicate.not(String::isEmpty))
            .toList();
        assertEquals(
            List.of(
                "SSN: ",
                "Password: ",
                "Login incorrect.",
                "SSN: ",
                "Password: ",
                "Login incorrect.",
                "SSN: ",
                "Password: ",
                "Login incorrect.",
                "3 incorrect attempts.",
                "1. Log into bank account",
                "2. Create bank account",
                "0. Exit",
                "Enter option: "
                ), loginSection);
    }

    @Test
    public void accountMenuInterface() {
        TestAccount account = accounts[0];
        responses = runMain(CHOOSE_ACCOUNT_LOGIN, account.ssn, account.password, CHOOSE_EXIT, CHOOSE_EXIT);
        List<String> accountMenu = linesIn(responses.subList(3, 4))
            .filter(Predicate.not(String::isEmpty))
            .toList();
        assertEquals(
            List.of(
                "1. Check balance",
                "2. Withdraw",
                "3. Deposit",
                "0. Exit",
                "Enter option: "
            ), accountMenu);
    }


    @Test
    public void accountMenuExit() {
        TestAccount account = accounts[0];
        responses = runMain(CHOOSE_ACCOUNT_LOGIN, account.ssn, account.password, CHOOSE_EXIT, CHOOSE_EXIT);
        List<String> exitAccountMenu = linesIn(responses.subList(4, 5))
            .filter(Predicate.not(String::isEmpty))
            .toList();
        assertEquals(
            List.of(
                "Exit.",
                "1. Log into bank account"
            ), exitAccountMenu.subList(0,2));
    }

    @Test
    public void checkBalance() {
        TestAccount account = accounts[0];
        responses = runMain(CHOOSE_ACCOUNT_LOGIN, account.ssn, account.password,
            CHOOSE_BALANCE, CHOOSE_EXIT, CHOOSE_EXIT);
        String balanceResponse = firstLineOf(responses.get(4));
        assertEquals("The balance is $%,.2f".formatted(account.balance), balanceResponse);
    }

    @Test
    public void makeWithdrawal() {
        TestAccount account = accounts[0];
        BigDecimal expectedBalance = account.balance.subtract(new BigDecimal("100"));
        responses = runMain(
            CHOOSE_ACCOUNT_LOGIN, account.ssn, account.password,
            CHOOSE_WITHDRAWAL, "100",
            CHOOSE_BALANCE,
            CHOOSE_EXIT, CHOOSE_EXIT);
        String balanceResponse = firstLineOf(responses.get(6));
        assertEquals("The balance is $%,.2f".formatted(expectedBalance), balanceResponse);
    }

    @Test
    public void makeDeposit() {
        TestAccount account = accounts[0];
        BigDecimal expectedBalance = account.balance.add(new BigDecimal("100"));
        responses = runMain(
            CHOOSE_ACCOUNT_LOGIN, account.ssn, account.password,
            CHOOSE_DEPOSIT, "100",
            CHOOSE_BALANCE,
            CHOOSE_EXIT, CHOOSE_EXIT);
        String balanceResponse = firstLineOf(responses.get(6));
        assertEquals("The balance is $%,.2f".formatted(expectedBalance), balanceResponse);
    }

    private void writeAccount(final BufferedWriter writer, final TestAccount account) throws IOException {
        writer.write(String.format("%s %s %s\n", account.ssn,
                                   PasswordUtils.getPasswordHash(account.password),
                                   account.balance));
    }
}
