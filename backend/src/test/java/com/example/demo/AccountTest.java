package com.example.demo;

import com.example.demo.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setBalance(100.0f);
        account.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("Should decrease balance when funds are sufficient")
    void testDebit_Success() {
        boolean result = account.debit(40.0f);
        assertTrue(result, "Debit should return true");
        assertEquals(60.0f, account.getBalance(), "Balance should be 60.0");
    }

    @Test
    @DisplayName("Should return false when funds are insufficient")
    void testDebit_InsufficientBalance() {
        boolean result = account.debit(150.0f);
        assertFalse(result, "Debit should return false");
        assertEquals(100.0f, account.getBalance(), "Balance should remain unchanged");
    }

    @Test
    @DisplayName("Should increase balance on credit")
    void testCredit_Success() {
        account.credit(50.0f);
        assertEquals(150.0f, account.getBalance());
    }
}