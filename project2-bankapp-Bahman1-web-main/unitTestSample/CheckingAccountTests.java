package edu.brooklyn.project2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class CheckingAccountTests {
    private CheckingAccount account;
    private String ssn = "123456789";
    private String hash = "hash";
    private BigDecimal balance = new BigDecimal("5000");

    public CheckingAccountTests() {
        account = new CheckingAccount(ssn, hash, balance);
    }

    @Test
    public void belongsTo() {
        assertTrue(account.belongsTo(ssn));
    }

    @Test
    public void matchAccount() {
        assertTrue(account.matchAccount(ssn, hash));
    }

    @Test
    public void getBalance() {
        assertEquals(balance, account.getBalance());
    }

    @Test
    public void depositSmall() {
        BigDecimal amount = BigDecimal.valueOf(5.50);
        BigDecimal expectedBalance = balance.add(amount);
        account.deposit(amount);
        assertEquals("5.5", amount.toString());
        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    public void withdrawBalance() {
        BigDecimal amount = account.getBalance();
        BigDecimal expectedBalance = BigDecimal.ZERO;
        account.withdraw(amount);
        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    public void withdrawInsufficientFunds() {
        BigDecimal amount = account.getBalance().add(BigDecimal.ONE);
        BigDecimal expectedBalance = account.getBalance();
        assertFalse(account.withdraw(amount));
        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    public void depositZeroThrows() {
        BigDecimal amount = BigDecimal.ZERO;
        assertThrows(java.lang.IllegalArgumentException.class, () ->
            account.deposit(amount));
    }
}
